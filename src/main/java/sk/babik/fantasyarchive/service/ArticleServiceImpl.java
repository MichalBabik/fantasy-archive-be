package sk.babik.fantasyarchive.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.babik.fantasyarchive.persistence.model.Article;
import sk.babik.fantasyarchive.persistence.model.Comment;
import sk.babik.fantasyarchive.persistence.model.FantasyUser;
import sk.babik.fantasyarchive.persistence.model.Tag;
import sk.babik.fantasyarchive.persistence.repository.ArticleRepository;


import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService{

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public Article createArticle(Article articleDto) {
        Article newArticle = new Article();
        newArticle.setTitle(articleDto.getTitle());
        newArticle.setDescription(articleDto.getDescription());
        newArticle.setImage(articleDto.getImage());
        newArticle.setDate_added(articleDto.getDate_added());
        newArticle.setFantasyUser(articleDto.getFantasyUser());
        newArticle.setText(articleDto.getText());
        System.out.println("Article with Title: " + newArticle.getTitle() + ", successfully created");
        return articleRepository.save(newArticle);
    }

    @Override
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    @Override
    public List<Article> getSortedArticles() {
        List<Article> articles = articleRepository.findAll();
        List<Article> sortedArticlesByDateAdded = articles.stream()
                .sorted(Comparator.comparing(Article::getDate_added).reversed())
                .collect(Collectors.toList());

        return sortedArticlesByDateAdded;
    }

    @Override
    public List<Article> getArticlesHomepage() {
        List<Article> articles = getSortedArticles();
        articles = articles.subList(0, Math.min(articles.size(), 4));
        return articles;
    }

    @Override
    public Article getArticle(Long id) {
        boolean isExistingArticle = articleRepository.existsById(id);
        if (isExistingArticle) {
            System.out.println("Article successfully returned!");
            return articleRepository.findById(id).get();
        } else {
            System.out.println("Article with id: " + id + ", doesn't exist!");
            return null;
        }
    }

    @Override
    public FantasyUser getArticleAuthorDetails(Long articleId) {
        boolean isExistingArticle = articleRepository.existsById(articleId);
        if (isExistingArticle) {
            FantasyUser fantasyUser = articleRepository.findById(articleId).get().getFantasyUser();

            FantasyUser fantasyUserDetails = new FantasyUser();
            fantasyUserDetails.setUsername(fantasyUser.getUsername());
            fantasyUserDetails.setBio(fantasyUser.getBio());
            fantasyUserDetails.setAvatar(fantasyUser.getAvatar());

            return fantasyUserDetails;
        } else {
            System.out.println("Article with id: " + articleId + " doesn't exist!");
            return null;
        }
    }

    @Override
    public boolean addTag(Long articleId, Tag tag) {
        Optional<Article> optionalArticle = articleRepository.findById(articleId);
        if (optionalArticle.isPresent()) {
            Article article = optionalArticle.get();
            article.getArticleTag().add(tag);
            articleRepository.save(article);
            System.out.println("Tag successfully added!");
            return true;
        } else {
            System.out.println("Article with id: " + articleId + ", doesn't exist!");
            return false;
        }
    }

    @Override
    public boolean hasTagAlready(Long articleId, Long tagId) {
        Optional<Article> optionalArticle = articleRepository.findById(articleId);
        if (optionalArticle.isPresent()) {
            Article article = optionalArticle.get();
            Set<Tag> articleTags = article.getArticleTag();
            for (Tag tag : articleTags) {
                if (tag.getId().equals(tagId)) {
                    return true;
                }
            }
        }
        return false;
    }


}
