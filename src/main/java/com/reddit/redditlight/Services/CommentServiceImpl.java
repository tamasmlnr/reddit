package com.reddit.redditlight.Services;

import com.reddit.redditlight.Models.Comment;
import com.reddit.redditlight.Models.Post;
import com.reddit.redditlight.Repositories.CommentRepository;
import com.reddit.redditlight.Models.ApplicationUser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

  private PostService postService;
  private CommentRepository commentRepository;
  private ApplicationUserService applicationUserService;

  public CommentServiceImpl(PostService postService, CommentRepository commentRepository, ApplicationUserService applicationUserService) {
    this.postService = postService;
    this.commentRepository = commentRepository;
    this.applicationUserService = applicationUserService;
  }

  @Override
  public Comment save(long id, Comment comment, ApplicationUser user) {
    Post post = postService.findById(id);
    comment.setDate(LocalDateTime.now());
    comment.setPost(post);
    comment.setUser(user);
     return commentRepository.save(comment);
  }

  @Override
  public List<Comment> findByContent(String searchTerm) {
    return commentRepository.findByContent(searchTerm);
  }

  @Override
  public void addReply(long postId, Comment comment, long commentId, ApplicationUser currentUser) {
    Comment originalComment = commentRepository.findById(commentId).orElse(null);
    if (originalComment!=null){
      comment.setReplyTo(originalComment);
      save(postId,comment,currentUser);
      originalComment.addReply(comment);
      commentRepository.save(originalComment);
    }
  }





}
