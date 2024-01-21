package sk.babik.fantasyarchive.persistence.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import sk.babik.fantasyarchive.Role;

import java.time.LocalDateTime;
import java.util.List;


@Setter
@Getter
@Entity
@Table(name="fantasy_user")
public class FantasyUser  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="username")
    private String username;

    @Column(name = "password", columnDefinition = "TEXT")
    private String password;

    @Column(name = "salt", columnDefinition = "TEXT")
    private String salt;

    @Column(name="email")
    private String email;

    @Column(name="avatar")
    private String avatar;

    @Column(name="bio", columnDefinition = "TEXT")
    private String bio;

    @Column(name="role_id")
    private Role role;

    @Column(name="Registration_date")
    private LocalDateTime date;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "fantasyUser", cascade = CascadeType.ALL)
    private List<Article> articleList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "fantasyUser", cascade = CascadeType.ALL)
    private List<Comment> commentList;

}
