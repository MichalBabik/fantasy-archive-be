package sk.babik.fantasyarchive.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Table;

@Table(name="article_tag")
public class ArticleTag {

    @Column(name="article_id")
    private Long articleId;

    @Column(name="tag_id")
    private Long tagId;

}
