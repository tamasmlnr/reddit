package com.reddit.redditlight.Models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Message implements Comparable<Message>{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private LocalDateTime date;
  @OneToOne
  private ApplicationUser sendingUser;
  @OneToOne
  private ApplicationUser receivingUser;
  private String content;
  private boolean userRead;

  public Message() {
  }

  public Message(LocalDateTime date, ApplicationUser sendingUser, ApplicationUser receivingUser, String content, boolean userRead) {
    this.date = date;
    this.sendingUser = sendingUser;
    this.receivingUser = receivingUser;
    this.content = content;
    this.userRead = userRead;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  public ApplicationUser getSendingUser() {
    return sendingUser;
  }

  public void setSendingUser(ApplicationUser sendingUser) {
    this.sendingUser = sendingUser;
  }

  public ApplicationUser getReceivingUser() {
    return receivingUser;
  }

  public void setReceivingUser(ApplicationUser receivingUser) {
    this.receivingUser = receivingUser;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public boolean isUserRead() {
    return userRead;
  }

  public void setUserRead(boolean userRead) {
    this.userRead = userRead;
  }

  @Override
  public int compareTo(Message o) {
    return o.getDate().compareTo(this.getDate());
  }
}
