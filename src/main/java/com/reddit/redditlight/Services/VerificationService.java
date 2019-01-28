package com.reddit.redditlight.Services;

import com.reddit.redditlight.Models.ApplicationUser;
import com.reddit.redditlight.Models.VerificationToken;
import org.springframework.stereotype.Service;

@Service
public interface VerificationService {
  void startValidation(ApplicationUser user);

  void sendMessage(ApplicationUser user, VerificationToken verificationToken);

  String getVerificationMessage(ApplicationUser user);

  String getVerificationError();

  boolean tokenExists(String token);

  VerificationToken verify(String token);

  void deleteToken(String token);
}

