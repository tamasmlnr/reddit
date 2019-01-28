package com.reddit.redditlight.Repositories;

import com.reddit.redditlight.Models.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {
  List<Post> findAll();

  List<Post> findByContentContainingIgnoreCase(String content);

  List<Post> findByTitleContainingIgnoreCase(String searchTerm);

  List<Post> findByUserId(Long userid);


  List<Post> findByDateBetween(LocalDateTime start, LocalDateTime end);
}

