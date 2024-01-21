package sk.babik.fantasyarchive.configuration;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sk.babik.fantasyarchive.Role;
import sk.babik.fantasyarchive.persistence.model.FantasyUser;
import sk.babik.fantasyarchive.service.FantasyUserService;

@Component
public class PrivilegedUser {

    @Autowired
    private FantasyUserService userService;

    public void createPrivilegedUser(String email, String pass, String username, Role role) {
        FantasyUser fantasyUser = new FantasyUser();
        fantasyUser.setEmail(email);
        fantasyUser.setPassword(pass);
        fantasyUser.setUsername(username);
        userService.createFantasyUser(fantasyUser, role);
    }

    @PostConstruct
    public void initialLoad() {
        createPrivilegedUser("admin@fa.com", "admin", "admin", Role.ADMIN);
        createPrivilegedUser("moderator@fa.com", "moderator", "moderator", Role.MODERATOR);
    }
}
