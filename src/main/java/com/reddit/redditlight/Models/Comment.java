package com.reddit.redditlight.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Comment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String content;
  private LocalDateTime date;
  private int score;
  @ManyToOne(fetch = FetchType.EAGER)
  @JsonIgnore
  private Post post;
  @ManyToOne
  private ApplicationUser user;
  @ManyToMany
  @JsonIgnore
  private List<Comment> replies;
  @ManyToOne
  private Comment replyTo;

  public Comment() {
  }

  public Comment(String content) {
    this.content = content;
    this.date = LocalDateTime.now();
    this.score = 0;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Post getPost() {
    return post;
  }

  public void setPost(Post post) {
    this.post = post;
  }

  public ApplicationUser getUser() {
    return user;
  }

  public void setUser(ApplicationUser user) {
    this.user = user;
  }

  public void addReply(Comment reply) {
    replies.add(reply);
  }

  public List<Comment> getReplies() {
  return replies;
  }

  public Comment getReplyTo() {
    return replyTo;
  }

  public void setReplyTo(Comment replyTo) {
    this.replyTo = replyTo;
  }
}
