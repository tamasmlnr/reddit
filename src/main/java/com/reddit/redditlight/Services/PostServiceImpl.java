package com.reddit.redditlight.Services;

import com.reddit.redditlight.Models.Comment;
import com.reddit.redditlight.Models.Post;
import com.reddit.redditlight.Models.PostDTO;
import com.reddit.redditlight.Repositories.CommentRepository;
import com.reddit.redditlight.Repositories.PostRepository;
import com.reddit.redditlight.Models.ApplicationUser;
import com.reddit.redditlight.Security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
  private PostRepository postRepository;
  private ApplicationUserService applicationUserService;
  private CommentRepository commentRepository;
  private JWTUtil jwtUtil;

  @Autowired
  public PostServiceImpl(PostRepository postRepository, ApplicationUserService applicationUserService, CommentRepository commentRepository, JWTUtil jwtUtil) {
    this.postRepository = postRepository;
    this.applicationUserService = applicationUserService;
    this.commentRepository = commentRepository;
    this.jwtUtil = jwtUtil;
  }

  @Override
  public List<Post> getAllPosts() {
    List<Post> posts = postRepository.findAll();
    Collections.sort(posts);
    return posts;
  }

  @Override
  public PostDTO getAllPostsDTO() {
    List<Post> posts = postRepository.findAll();
    Collections.sort(posts);
    return new PostDTO(posts);
  }


  @Override
  public Post save(Post post) {
    return postRepository.save(post);
  }

  @Override
  public Post upvote(long postId, ApplicationUser user) {
    Post post = postRepository.findById(postId)
        .orElse(null);
    if (post != null && user.getUpvotedPosts()
        .contains(post)) {
      removeUpvote(user, post);
      return postRepository.save(post);
    }
    if (post != null && !user.getUpvotedPosts()
        .contains(post)) {
      if (user.getDownvotedPosts()
          .contains(post)) {
        removeDownvote(user, post);
      }
      post.setScore(post.getScore() + 1);
      user.addUpvotedPost(post);
    }
    applicationUserService.save(user);
    return postRepository.save(post);
  }

  @Override
  public Post downvote(long postId, ApplicationUser user) {
    Post post = postRepository.findById(postId)
        .orElse(null);
    if (post != null && user.getDownvotedPosts()
        .contains(post)) {
      removeDownvote(user, post);
      return postRepository.save(post);
    }
    if (post != null && !user.getDownvotedPosts()
        .contains(post)) {
      if (user.getUpvotedPosts()
          .contains(post)) {
        removeUpvote(user, post);
      }
      post.setScore(post.getScore() - 1);
      user.addDownvotedPost(post);
    }
    applicationUserService.save(user);
    return postRepository.save(post);
  }

  public void removeDownvote(ApplicationUser user, Post post) {
    user.removeDownvotedPost(post);
    post.setScore(post.getScore() + 1);
    applicationUserService.save(user);
  }

  public void removeUpvote(ApplicationUser user, Post post) {
    user.removeUpvotedPost(post);
    post.setScore(post.getScore() - 1);
    applicationUserService.save(user);
  }

  @Override
  public Post findById(long id) {
    return postRepository.findById(id)
        .orElse(null);
  }

  @Override
  public List<Post> search(String searchTerm, Boolean title, Boolean content, Boolean comment) {
    List<Post> results = new ArrayList<>();
    if (searchTerm != null) {
      if (title != null && title == true) {
        results.addAll(searchByTitle(searchTerm));
      }
      if (content != null && content == true) {
        results.addAll(searchByContent(searchTerm));
      }
      if (comment != null && comment == true) {
        results.addAll(searchByComment(searchTerm));
      }
    }
    return results.stream()
        .distinct()
        .collect(Collectors.toList());
  }

  @Override
  public List<Post> searchByTitle(String searchTerm) {
    List<Post> result = postRepository.findByTitleContainingIgnoreCase(searchTerm);
    Collections.sort(result);
    return result;
  }

  @Override
  public List<Post> searchByContent(String searchTerm) {
    List<Post> result = postRepository.findByContentContainingIgnoreCase(searchTerm);
    Collections.sort(result);
    return result;
  }

  @Override
  public List<Post> searchByComment(String searchTerm) {
    List<Comment> comments = commentRepository.findByContent(searchTerm);
    List<Post> result = new ArrayList<>();
    for (Comment comment : comments) {
      result.add(comment.getPost());
    }
    return result;
  }

  @Override
  public void savePostsFromDTO(PostDTO postDTO, ApplicationUser user) {
    for (Post post:postDTO.getPosts()){
      post.setDate(LocalDateTime.now());
      post.setUser(user);
    }
    postRepository.saveAll(postDTO.getPosts());
  }

  @Override
  public void setDate(Post post) {
    post.setDate(LocalDateTime.now());
  }

  @Override
  public ApplicationUser setUser(Post post, HttpServletRequest req) {
    String username = jwtUtil.getUserFromRequest(req);
    ApplicationUser user = applicationUserService.findByUsername(username);
    if (user != null) {
      post.setUser(user);
    }
    return user;
  }

  @Override
  public boolean deletePost(Long id, HttpServletRequest req) {
    if (req!=null&&id!=null) {
      String username = jwtUtil.getUserFromRequest(req);
      Post post = postRepository.findById(id).orElse(null);
      if (post!=null&&post.getUser().getUsername().equals(username)){
        postRepository.delete(post);
        return true;
      }
    }
    return false;
  }


  @Override
  public List<Post> findPostsByUser(String username) {
    ApplicationUser user = applicationUserService.findByUsername(username);
    return postRepository.findByUserId(user.getId());
  }

  @Override
  public List<Post> findByDate(String date) {
    LocalDateTime start = LocalDate.parse(date)
        .atStartOfDay();
    LocalDateTime end = LocalDate.parse(date)
        .atTime(LocalTime.MAX);
    return postRepository.findByDateBetween(start, end);
  }

  @Override
  public void addSavedPost(long postId, ApplicationUser user) {
    Post post = findById(postId);
    if (!user.getSavedPosts()
        .contains(post)) {
      user.addSavedPost(post);
    }
    applicationUserService.save(user);
  }

  @Override
  public void unsavePost(long postId, ApplicationUser user) {
    Post post = findById(postId);
    if (user.getSavedPosts()
        .contains(post)) {
      user.unsavePost(post);
    }
    applicationUserService.save(user);
  }
}
