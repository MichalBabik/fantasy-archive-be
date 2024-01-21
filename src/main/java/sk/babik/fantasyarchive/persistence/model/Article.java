package sk.babik.fantasyarchive.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name="article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="title")
    private String title;

    @Column(name="description", columnDefinition = "TEXT")
    private String description;

    @Column(name="image")
    private String image;

    @Column(name="text", columnDefinition = "TEXT")
    private String text;

    @Column(name="date_added")
    private OffsetDateTime date_added;

    @ManyToMany
    @JoinTable(
            name = "article_tag",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> articleTag = new HashSet<>();

    /*
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "article", cascade = CascadeType.ALL)
    private List<Comment> commentList;

     */

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fantasyUser_id")
    @JsonIgnore
    private FantasyUser fantasyUser;

    public Set<Tag> getTags() {
        return articleTag;
    }



}
