package com.reddit.redditlight.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Post implements Comparable<Post> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String title;
  @Column(columnDefinition = "TEXT")
  private String content;
  private LocalDateTime date;
  private int score;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
  private List<Comment> comments;
  @ManyToOne
  private ApplicationUser user;

  public Post() {
  }

  public Post(String content, String title, ApplicationUser user) {
    this.content = content;
    this.title = title;
    this.date = LocalDateTime.now();
    this.score = 0;
    this.comments = new ArrayList<>();
    this.user=user;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  public long getId() {
    return id;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  @Override
  public int compareTo(Post o) {
    return o.getDate().compareTo(this.getDate());
  }

  public void setId(long id) {
    this.id = id;
  }

  public List<Comment> getComments() {
    return comments;
  }

  public int commentsSize(){
    return comments.size();
  }

  public void addComment(Comment comment) {
    comments.add(comment);
  }

  public void setComments(List<Comment> comments) {
    this.comments = comments;
  }

  public ApplicationUser getUser() {
    return user;
  }

  public ApplicationUser setUser(ApplicationUser user) {
    return this.user = user;
  }
}

