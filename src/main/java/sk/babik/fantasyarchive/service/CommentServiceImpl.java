package sk.babik.fantasyarchive.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.babik.fantasyarchive.persistence.model.Comment;
import sk.babik.fantasyarchive.persistence.model.FantasyUser;
import sk.babik.fantasyarchive.persistence.repository.CommentRepository;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl implements CommentService {


    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Comment createComment(Comment commentDto) {
        Comment newComment = new Comment();
        newComment.setDate_added(LocalDateTime.now());
        newComment.setText(commentDto.getText());
        newComment.setFantasyUser(commentDto.getFantasyUser());
        System.out.println("Comment successfully created");
        return commentRepository.save(newComment);
    }

    @Override
    public Comment getComment(Long id) {
        boolean isExistingComment = commentRepository.existsById(id);
        if (isExistingComment) {
            System.out.println("Comment successfully returned!");
            return commentRepository.findById(id).get();
        } else {
            System.out.println("Comment with id: " + id + ", doesn't exist!");
            return null;
        }
    }

    @Override
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @Override
    public boolean deleteCommentById(Long id) {
        boolean isExistingComment = commentRepository.existsById(id);
        if (isExistingComment) {
            commentRepository.deleteById(id);
            System.out.println("Comment successfully deleted!");
            return true;
        } else {
            System.out.println("Cannot delete because comment with id: " + id + ", doesn't exist!");
            return false;
        }
    }

    @Override
    public boolean deleteAllComments() {
        List<Comment> comments = commentRepository.findAll();
        for (Comment comment : comments) {
            commentRepository.deleteById(comment.getId());
        }
        return true;
    }

    @Override
    public Map<Long, String> getAllCommentsUsernames() {
        Map<Long, String> commentUsernames = new HashMap<>();
        List<Comment> comments = commentRepository.findAll();

        for (Comment comment : comments) {
            Long commentId = comment.getId();
            String username = comment.getFantasyUser().getUsername();
            commentUsernames.put(commentId, username);
        }

        return commentUsernames;
    }
}
