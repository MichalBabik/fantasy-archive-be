package sk.babik.fantasyarchive.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@Entity
@Table(name="tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name")
    private String name;

    // @ManyToMany(mappedBy = "articleTag")
    @ManyToMany(mappedBy = "articleTag", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    Set<Article> tagArticle;

    @PreRemove
    private void removeTagFromArticles() {
        for (Article article : tagArticle) {
            article.getTags().remove(this);
        }
    }


}
