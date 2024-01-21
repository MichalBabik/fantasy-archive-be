package sk.babik.fantasyarchive.service;

import org.springframework.web.bind.annotation.PathVariable;
import sk.babik.fantasyarchive.Role;
import sk.babik.fantasyarchive.persistence.model.FantasyUser;

import java.util.List;

public interface FantasyUserService {

    FantasyUser createFantasyUser(FantasyUser userDto, Role role);

    boolean checkLogin(String inputPassword, String salt, String hashedPassword);

    FantasyUser getFantasyUser(Long id);

    FantasyUser getFantasyUserByEmail(String email);

    List<FantasyUser> getAllFantasyUsers();

    Long getIdByEmail(String email);

    boolean isEmailAvailable(String email);

    boolean deleteFantasyUserById(Long id);

    boolean deleteFantasyUserByEmail(String email, FantasyUser pfantasyUser);

    FantasyUser updateFantasyUserById(Long id, FantasyUser pFantasyUser);

    FantasyUser updateFantasyUserByEmail(String email, FantasyUser pFantasyUser);

    boolean hasPrivilege(Long id);


}
