package sk.babik.fantasyarchive.service;

import sk.babik.fantasyarchive.persistence.model.Comment;
import sk.babik.fantasyarchive.persistence.model.FantasyUser;

import java.util.List;
import java.util.Map;

public interface CommentService {

    Comment createComment(Comment commentDto);

    Comment getComment(Long id);

    List<Comment> getAllComments();

    boolean deleteCommentById(Long id);

    Map<Long, String> getAllCommentsUsernames();

    boolean deleteAllComments();
}
