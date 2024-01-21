package sk.babik.fantasyarchive.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import sk.babik.fantasyarchive.Role;
import sk.babik.fantasyarchive.persistence.model.FantasyUser;
import sk.babik.fantasyarchive.persistence.repository.FantasyUserRepository;
import sk.babik.fantasyarchive.configuration.PasswordHashing;



import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FantasyUserServiceImpl implements FantasyUserService {

    @Autowired
    private FantasyUserRepository userRepository;

    @Override
    public FantasyUser createFantasyUser(FantasyUser userDto, Role role) {
        if (isEmailAvailable(userDto.getEmail())) {
            FantasyUser newUser = new FantasyUser();
            newUser.setUsername(userDto.getUsername());
            newUser.setEmail(userDto.getEmail());
            newUser.setRole(role);
            newUser.setDate(LocalDateTime.now());
            newUser.setAvatar(userDto.getAvatar());
            newUser.setBio(userDto.getBio());

            PasswordHashing passwordHashing = new PasswordHashing();
            String salt = passwordHashing.generateSalt();
            newUser.setSalt(salt);

            String hashedPassword = passwordHashing.hashPassword(userDto.getPassword(), salt );
            newUser.setPassword(hashedPassword);

            System.out.println("User with email: " + newUser.getEmail() + ", successfully created");
            return userRepository.save(newUser);
        }
        return null;
    }

    @Override
    public boolean checkLogin(String inputPassword, String salt, String hashedPassword) {
        PasswordHashing passwordHashing = new PasswordHashing();
        return passwordHashing.checkPassword(inputPassword, salt, hashedPassword);
    }

    @Override
    public FantasyUser getFantasyUser(Long id) {
        boolean isExistingUser = userRepository.existsById(id);
        if (isExistingUser) {
            System.out.println("User successfully returned!");
            return userRepository.findById(id).get();
        } else {
            System.out.println("User with id: " + id + ", doesn't exist!");
            return null;
        }
    }

    @Override
    public FantasyUser getFantasyUserByEmail(String email) {
        for (FantasyUser fantasyUser : getAllFantasyUsers()) {
            if (fantasyUser.getEmail().equals(email)) {
                return fantasyUser;
            }
        }
        return null;
    }

    @Override
    public List<FantasyUser> getAllFantasyUsers() {
        return userRepository.findAll();
    }

    @Override
    public Long getIdByEmail(String email) {
        for (FantasyUser fantasyUser : getAllFantasyUsers()) {
            if (fantasyUser.getEmail() != null && fantasyUser.getEmail().equals(email)) {
                return fantasyUser.getId();
            }
        }
        return null;
    }

    @Override
    public boolean isEmailAvailable(String email) {
        for (FantasyUser fantasyUser : getAllFantasyUsers()) {
            if (fantasyUser.getEmail().equals(email)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean deleteFantasyUserById(Long id) {
        boolean isExistingUser = userRepository.existsById(id);
        if (isExistingUser) {
            userRepository.deleteById(id);
            System.out.println("User successfully deleted!");
            return true;
        } else {
            System.out.println("Cannot delete because user with id: " + id + ", doesn't exist!");
            return false;
        }
    }

    @Override
    public boolean deleteFantasyUserByEmail(String email, FantasyUser pfantasyUser) {
        FantasyUser fantasyUser = getFantasyUser(getIdByEmail(email));
        if (fantasyUser.getPassword().equals(pfantasyUser.getPassword())) {
            deleteFantasyUserById(fantasyUser.getId());
            System.out.println("User successfully deleted!");
            return true;
        } else {
            System.out.println("Wrong password cannot delete!");
            return false;
        }
    }

    @Override
    public FantasyUser updateFantasyUserById(Long id, FantasyUser pFantasyUser) {
        Optional<FantasyUser> optionalFantasyUser = userRepository.findById(id);
        if (optionalFantasyUser.isPresent()) {
            FantasyUser fantasyUser = optionalFantasyUser.get();
            fantasyUser.setAvatar(pFantasyUser.getAvatar());
            fantasyUser.setBio(pFantasyUser.getBio());
            System.out.println("User successfully updated!");
            return  userRepository.save(fantasyUser);
        }
        System.out.println("Cannot update because user with id: " + id + ", doesn't exist!");
        return null;
    }

    @Override
    public FantasyUser updateFantasyUserByEmail(String email, FantasyUser pFantasyUser) {
        Long id;
        if ((id = getIdByEmail(email)) != null) {
            if (pFantasyUser.getPassword().equals(getFantasyUser(id).getPassword())) {
                System.out.println("Updating user with email: " + email);
                pFantasyUser.setEmail(email);
                System.out.println("New username: " + pFantasyUser.getUsername());
                return updateFantasyUserById(id, pFantasyUser);
            } else {
                System.out.println("Wrong password!");
            }
        } else {
            System.out.println("Cannot update because user with email: " + email + ", doesn't exist!");
        }
        return null;
    }

    @Override
    public boolean hasPrivilege(Long id) {
        Role role = getFantasyUser(id).getRole();
        if (role == Role.MODERATOR || role == Role.ADMIN  ) {
            return true;
        }
        return false;
    }
}
