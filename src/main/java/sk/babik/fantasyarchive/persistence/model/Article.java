package sk.babik.fantasyarchive.persistence.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;


@Entity
@Table(name="article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="title")
    private String title;

    @Column(name="text")
    private String text;

    @Column(name="author")
    private String author;

    @Column(name="date_added")
    private OffsetDateTime date_added;

    @ManyToMany
    @JoinTable(
            name = "article_tag",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    Set<Tag> articleTag;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "article", cascade = CascadeType.ALL)
    private List<Comment> commentList;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fantasyUser_id")
    private FantasyUser fantasyUser;

}
