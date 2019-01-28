package com.reddit.redditlight.Services;

import com.reddit.redditlight.Models.Comment;
import com.reddit.redditlight.Models.ApplicationUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {
  Comment save(long id, Comment comment, ApplicationUser user);
  List<Comment> findByContent(String searchTerm);
  void addReply(long postId, Comment comment, long commentId, ApplicationUser currentUser);
}
