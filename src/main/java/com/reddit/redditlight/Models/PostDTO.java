package com.reddit.redditlight.Models;

import java.util.List;

public class PostDTO {

  private List<Post> posts;

  public PostDTO(List<Post> posts) {
    this.posts = posts;
  }

  public PostDTO() {
  }

  public List<Post> getPosts() {
    return posts;
  }

  public void setPosts(List<Post> posts) {
    this.posts = posts;
  }
}
