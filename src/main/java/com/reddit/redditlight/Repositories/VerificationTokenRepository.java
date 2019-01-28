package com.reddit.redditlight.Repositories;

import com.reddit.redditlight.Models.ApplicationUser;
import com.reddit.redditlight.Models.VerificationToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface VerificationTokenRepository extends CrudRepository<VerificationToken, Long> {

  VerificationToken findByToken(String token);

  VerificationToken findByUser(ApplicationUser user);

  @Transactional
  void deleteByToken(String token);
}
