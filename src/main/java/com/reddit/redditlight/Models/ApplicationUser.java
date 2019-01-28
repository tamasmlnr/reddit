package com.reddit.redditlight.Models;

import com.fasterxml.jackson.annotation.JsonValue;

import javax.persistence.*;
import java.util.List;

@Entity
public class ApplicationUser {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String username;
  private String password;
  @OneToMany(cascade = CascadeType.ALL)
  private List<Post> posts;
  @OneToMany(cascade = CascadeType.ALL)
  private List<Comment> comments;
  private boolean verified;
  private String email;
  @ManyToMany
  private List<Post> upvotedPosts;
  @ManyToMany
  private List<Post> downvotedPosts;
  @ManyToMany
  private List<Post> savedPosts;
  @ManyToMany
  private List<Message> messages;


  public ApplicationUser() {
  }

  public ApplicationUser(String username, String password, List<Post> posts, List<Comment> comments, boolean verified, String email, List<Post> upvotedPosts, List<Post> downvotedPosts, List<Post> savedPosts, List<Message> messages) {
    this.username = username;
    this.password = password;
    this.posts = posts;
    this.comments = comments;
    this.verified = verified;
    this.email = email;
    this.upvotedPosts = upvotedPosts;
    this.downvotedPosts = downvotedPosts;
    this.savedPosts = savedPosts;
    this.messages = messages;
  }

  public long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void addComment(Comment comment) {
    comments.add(comment);
  }

  public void addPost(Post post) {
    posts.add(post);
  }

  public String toString() {
    return this.username;
  }

  public boolean isVerified() {
    return verified;
  }

  public void setVerified(boolean verified) {
    this.verified = verified;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public List<Post> getUpvotedPosts() {
    return upvotedPosts;
  }

  public void addUpvotedPost(Post post) {
    upvotedPosts.add(post);
  }

  public void removeUpvotedPost(Post post) {
    upvotedPosts.remove(post);
  }

  public List<Post> getDownvotedPosts() {
    return downvotedPosts;
  }


  public void addDownvotedPost(Post post) {
    downvotedPosts.add(post);
  }


  public void removeDownvotedPost(Post post) {
    downvotedPosts.remove(post);
  }

  public List<Post> getSavedPosts() {
    return savedPosts;
  }

  public void addSavedPost(Post post) {
    savedPosts.add(post);
  }

  public void unsavePost(Post post) {
    savedPosts.remove(post);
  }

  public List<Message> getMessages() {
    return messages;
  }

  public void addMessage(Message message) {
    messages.add(message);
  }

  @JsonValue
  public String toPersonInfo() {
    return username;
  }
}
