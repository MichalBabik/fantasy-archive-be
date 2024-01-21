package sk.babik.fantasyarchive.controller;

import lombok.RequiredArgsConstructor;

import org.apache.catalina.util.ToStringUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.babik.fantasyarchive.Role;
import sk.babik.fantasyarchive.persistence.model.Article;
import sk.babik.fantasyarchive.persistence.model.Comment;
import sk.babik.fantasyarchive.persistence.model.FantasyUser;
import sk.babik.fantasyarchive.persistence.model.Tag;
import sk.babik.fantasyarchive.service.*;


import java.util.*;


@org.springframework.web.bind.annotation.RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
@CrossOrigin
public class RestController {

    private final FantasyUserService userService;
    private final ArticleService articleService;
    private final TagService tagService;
    private final CommentService commentService;

    //*****************************************************************************************
    //Users

    @PostMapping("/user/create")
    @ResponseStatus(HttpStatus.CREATED)
    public FantasyUser createFantasyUser(@RequestBody FantasyUser userDto) {
        return userService.createFantasyUser(userDto, Role.USER);
    }

    @PostMapping("/user/signin")
    public ResponseEntity<Long> signIn(@RequestBody SignInRequest signInRequest) {
        FantasyUser fantasyUser = userService.getFantasyUserByEmail(signInRequest.getEmail());

        if (fantasyUser == null) {
            //System.out.println("Not found");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else if (userService.checkLogin(signInRequest.getPassword(), fantasyUser.getSalt(), fantasyUser.getPassword())) {
            return new ResponseEntity<>(fantasyUser.getId(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<FantasyUser> getFantasyUser(@PathVariable Long id){
        FantasyUser user = userService.getFantasyUser(id);
        if (user != null) {
            return  new ResponseEntity<>(userService.getFantasyUser(id), HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/getRole/{id}")
    public ResponseEntity<Role> getFantasyUserRole(@PathVariable Long id){
        FantasyUser user = userService.getFantasyUser(id);
        if (user != null) {
            return  new ResponseEntity<>(user.getRole(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/check/{email}")
    public ResponseEntity<String> isFantasyUserWithEmail(@PathVariable String email){
        boolean isEmailAvailable = userService.isEmailAvailable(email);
        if (isEmailAvailable) {
            return new ResponseEntity<>("Email is available", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Email is already in use", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/allUsers")
    public List<FantasyUser> getAllFantasyUsers() {
        return userService.getAllFantasyUsers();
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> tryDeleteFantasyUserByID(@PathVariable Long id) {
            boolean isDeleted = userService.deleteFantasyUserById(id);
        if (isDeleted) {
            return new ResponseEntity<>("User deleted", HttpStatus.OK);
        } else
            return  new ResponseEntity<>("User not found",HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/user/deleteAccount/{email}")
    public ResponseEntity<String> tryDeleteFantasyUserByEmail(@PathVariable String email, @RequestBody FantasyUser fantasyUser) {
        boolean isDeleted = userService.deleteFantasyUserByEmail(email, fantasyUser);
        if (isDeleted) {
            return new ResponseEntity<>("User deleted", HttpStatus.OK);
        } else
            return  new ResponseEntity<>("User not found",HttpStatus.NOT_FOUND);
    }

    @PutMapping("/user/update/{id}")
    public ResponseEntity<FantasyUser> updateFantasyUserById(@PathVariable Long id, @RequestBody FantasyUser fantasyUser) {
        FantasyUser user = userService.updateFantasyUserById(id, fantasyUser);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/user/updateUsername/{email}")
    public ResponseEntity<FantasyUser> updateFantasyUserByEmail(@PathVariable String email, @RequestBody FantasyUser fantasyUser) {
        FantasyUser user = userService.updateFantasyUserByEmail(email, fantasyUser);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    //*********************************************************************************************************************************
    //Articles

    @PostMapping("/article/publish/{userId}")
    public ResponseEntity<String> publishArticle(@PathVariable Long userId, @RequestBody Article article) {
        FantasyUser user = userService.getFantasyUser(userId);
        if (userService.hasPrivilege(userId ) && user != null) {
            article.setFantasyUser(user);
            articleService.createArticle(article);
            return new ResponseEntity<>("Article created", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Article couldnt be created, you don have authorization", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/article/getAll")
    public List<Article> getAllArticles() {
        return articleService.getAllArticles();
    }

    @GetMapping("/article/getHomepageArticles")
    public List<Article> getHomepageArticles() {
        return articleService.getArticlesHomepage();
    }

    @GetMapping("/article/getArticleCreatorDetails/{articleId}")
    public FantasyUser getAuthorsDetails(@PathVariable Long articleId) {
        return articleService.getArticleAuthorDetails(articleId);
    }

    @GetMapping("/article/getArticle/{articleId}")
    public Article getArticleById(@PathVariable Long articleId) {
        return articleService.getArticle(articleId);
    }

    @GetMapping("/article/getArticleCreatorId/{articleId}")
    public Long getArticleCreatorId(@PathVariable Long articleId) {
        return articleService.getArticle(articleId).getFantasyUser().getId();
    }

    @GetMapping("/article/getArticleCreatorUsername/{articleId}")
    public String getArticleCreatorUsername(@PathVariable Long articleId) {
        return articleService.getArticle(articleId).getFantasyUser().getUsername();
    }

    @GetMapping("/article/getArticleTags/{articleId}")
    public Set<Tag> getArticleTags(@PathVariable Long articleId) {
        return articleService.getArticle(articleId).getArticleTag();
    }

    @PutMapping("/article/updateTags/{articleId}/{tagId}")
    public ResponseEntity<String> updateArticleTags(@PathVariable Long articleId, @PathVariable Long tagId) {

        if (articleService.hasTagAlready(articleId, tagId)) {
            return new ResponseEntity<>("This tag is already added to this article", HttpStatus.CONFLICT);
        }
        if (articleService.addTag(articleId, tagService.getTag(tagId))) {
            return new ResponseEntity<>("Tag added", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Tag couldnt be added, article with " + articleId + " not found", HttpStatus.NOT_FOUND);
        }
    }

    //**********************************************************************************
    //tags

    @PostMapping("/tag/create/{tagName}")
    public ResponseEntity<Map<String, String>> createTag(@PathVariable String tagName) {
        Map<String, String> response = new HashMap<>();

        if (tagService.isTagAvailable(tagName)) {
            tagService.createTag(tagName);
            response.put("message", "Tag created");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("message", "Tag with this name already exists");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/tag/allTags")
    public List<Tag> getAllTags() {
        return tagService.getAllTags();
    }

    @GetMapping("/tag/exist/{tagName}")
    public ResponseEntity<Boolean> existTag(@PathVariable String tagName) {
        boolean tagExists = tagService.isTagAvailable(tagName);
        return ResponseEntity.ok(tagExists);
    }

    @DeleteMapping("/tag/{id}")
    public ResponseEntity<String> tryDeleteTagByID(@PathVariable Long id) {
        try {
            boolean isDeleted = tagService.deleteTagById(id);
            if (isDeleted) {
                return new ResponseEntity<>("Tag deleted", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Tag not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error deleting tag: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //**************************************************************************************
    //Comments

    @PostMapping("/comment/create/{userId}")
    public ResponseEntity<List<Comment>> createComment(@PathVariable Long userId, @RequestBody Comment comment) {
        if (userService.getFantasyUser(userId) != null) {
            comment.setFantasyUser(userService.getFantasyUser(userId));
            commentService.createComment(comment);

            List<Comment> allComments = commentService.getAllComments(); // Fetch all comments after creation
            return new ResponseEntity<>(allComments, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/comment/getAll")
    public List<Comment> getAllComments() {
        return commentService.getAllComments();
    }

    @GetMapping("/comment/getUsersId/{commentId}")
    public ResponseEntity<Long> getCommentUsersId(@PathVariable Long commentId){
        Comment comment = commentService.getComment(commentId);
        if (comment != null) {
            return  new ResponseEntity<>(commentService.getComment(commentId).getFantasyUser().getId(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

//    @DeleteMapping("/comment/{commentId}")
//    public ResponseEntity<String> tryDeleteComment(@PathVariable Long commentId) {
//
//        boolean isDeleted = commentService.deleteCommentById(commentId);
//        if (isDeleted) {
//            return new ResponseEntity<>("Comment deleted", HttpStatus.OK);
//        } else
//            return  new ResponseEntity<>("Comment not found",HttpStatus.NOT_FOUND);
//    }

    @DeleteMapping("/comment/deleteAll/")
    public ResponseEntity<String> deleteComments(@RequestParam Long userId) {
        if (userService.hasPrivilege(userId)) {
            if (commentService.deleteAllComments()) {
                return new ResponseEntity<>("Comments deleted", HttpStatus.OK);
            } else
                return  new ResponseEntity<>("Comments not found",HttpStatus.NOT_FOUND);
        } else {
            return  new ResponseEntity<>("Only admins and moderators can use this function!",HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/comment/getListOfUsernames")
    public ResponseEntity<Map<Long, String>> getFantasyUserUsernames() {
        return new ResponseEntity<>(commentService.getAllCommentsUsernames(), HttpStatus.OK);
    }
}





