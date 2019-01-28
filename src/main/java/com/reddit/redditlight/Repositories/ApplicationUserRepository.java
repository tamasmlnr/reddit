package com.reddit.redditlight.Repositories;

import com.reddit.redditlight.Models.ApplicationUser;
import org.springframework.data.repository.CrudRepository;

public interface ApplicationUserRepository extends CrudRepository<ApplicationUser, Long> {
  ApplicationUser findByUsername(String username);

  boolean existsByUsername(String username);

  ApplicationUser findOneByUsername(String username);

  ApplicationUser findOneByEmail(String email);
}
