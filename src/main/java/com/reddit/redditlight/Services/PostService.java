package com.reddit.redditlight.Services;

import com.reddit.redditlight.Models.ApplicationUser;
import com.reddit.redditlight.Models.Post;
import com.reddit.redditlight.Models.PostDTO;
import javafx.application.Application;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public interface PostService {
  List<Post> getAllPosts();
  PostDTO getAllPostsDTO();
  Post save(Post post);
  Post upvote(long postId, ApplicationUser user);
  Post downvote(long postId, ApplicationUser user);
  Post findById(long id);
  List<Post> findPostsByUser(String username);
  List<Post> findByDate(String date);
  void addSavedPost(long postId, ApplicationUser user);
  void unsavePost(long postId, ApplicationUser user);
  List<Post> search(String searchTerm, Boolean title, Boolean content, Boolean comment);
  List<Post> searchByTitle(String searchTerm);
  List<Post> searchByContent(String searchTerm);
  List<Post> searchByComment(String searchTerm);
  void savePostsFromDTO(PostDTO postDTO, ApplicationUser user);
  void setDate(Post post);
  ApplicationUser setUser(Post post, HttpServletRequest req);
}
