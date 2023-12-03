package sk.babik.fantasyarchive.persistence.model;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name="comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="author")
    private String author;

    @Column(name="text")
    private String text;

    @Column(name="date_added")
    private OffsetDateTime date_added;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fantasyUser_id")
    private FantasyUser fantasyUser;

}
