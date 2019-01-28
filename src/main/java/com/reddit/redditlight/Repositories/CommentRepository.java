package com.reddit.redditlight.Repositories;

import com.reddit.redditlight.Models.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {
  List<Comment> findByContent(String searchTerm);
}