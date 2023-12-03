package sk.babik.fantasyarchive.persistence.model;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Table(name="tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name")
    private String name;

    @ManyToMany(mappedBy = "articleTag")
    Set<Article> tagArticle;
}
