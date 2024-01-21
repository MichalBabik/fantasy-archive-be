package sk.babik.fantasyarchive.service;

import sk.babik.fantasyarchive.persistence.model.Article;
import sk.babik.fantasyarchive.persistence.model.Comment;
import sk.babik.fantasyarchive.persistence.model.FantasyUser;
import sk.babik.fantasyarchive.persistence.model.Tag;

import java.util.List;

public interface ArticleService {

    Article createArticle(Article articleDto);

    List<Article> getAllArticles();

    List<Article> getSortedArticles();

    List<Article> getArticlesHomepage();

    Article getArticle(Long id);

    public FantasyUser getArticleAuthorDetails(Long articleId);

    boolean addTag(Long articleId, Tag tag);


    boolean hasTagAlready(Long articleId, Long tagId);


}
