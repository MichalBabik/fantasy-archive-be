package sk.babik.fantasyarchive.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name="comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="text")
    private String text;

    @Column(name="date_added")
    private LocalDateTime date_added;

    /*
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "article_id")
    private Article article;
        */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fantasyUser_id")
    @JsonIgnore
    private FantasyUser fantasyUser;

}
