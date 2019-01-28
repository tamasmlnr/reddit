package com.reddit.redditlight.Services;

import com.reddit.redditlight.Models.ApplicationUser;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public interface ApplicationUserService {
  ApplicationUser getCurrentUser(HttpServletRequest request);
  ApplicationUser findByUsername(String username);
  ApplicationUser save(ApplicationUser user);
  List<String> validateSignup(ApplicationUser user, String confirm);
  ApplicationUser findOneByEmail(String email);
  ApplicationUser findOneByUsername(String email);
}
