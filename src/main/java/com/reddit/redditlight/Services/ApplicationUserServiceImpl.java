package com.reddit.redditlight.Services;

import com.reddit.redditlight.Models.ApplicationUser;
import com.reddit.redditlight.Repositories.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicationUserServiceImpl implements ApplicationUserService {

  private ApplicationUserRepository applicationUserRepository;

  @Autowired
  public ApplicationUserServiceImpl(ApplicationUserRepository applicationUserRepository) {
    this.applicationUserRepository = applicationUserRepository;
  }

  @Override
  public ApplicationUser getCurrentUser(HttpServletRequest request) {
    String username = SecurityContextHolder.getContext()
        .getAuthentication()
        .getName();
    return applicationUserRepository.findByUsername(username);
  }

  @Override
  public ApplicationUser findByUsername(String username) {
    return applicationUserRepository.findByUsername(username);
  }

  @Override
  public ApplicationUser save(ApplicationUser user) {
    return applicationUserRepository.save(user);
  }

  @Override
  public List<String> validateSignup(ApplicationUser user, String confirm) {
    List<String> messages = new ArrayList<>();
    System.out.println(findOneByEmail(user.getEmail()));
    if (!user.getPassword().equals(confirm)){
      messages.add("The passwords do not match!");
    }
    if(user.getEmail()==null||user.getEmail().length()==0){
      messages.add("No e-mail address added!");
    }
    else if (findOneByEmail(user.getEmail())!=null){
      messages.add("This e-mail address is already registered!");
    }
    if (user.getPassword()
        .length() <= 5) {
      messages.add("The password is too short! It should be 5 characters at least.");
    }
    if (existByUsername(user.getUsername())) {
      messages.add("The username already exists!");
    }
    return messages;
  }

  public boolean existByUsername(String username) {
    return applicationUserRepository.existsByUsername(username);
  }

  @Override
  public ApplicationUser findOneByEmail(String email) {
    if (email == null || email.isEmpty()) {
      return null;
    } else {
      return applicationUserRepository.findOneByEmail(email);
    }
  }

  @Override
  public ApplicationUser findOneByUsername(String username) {
    if (username == null || username.isEmpty()) {
      //TODO custom error
      return null;
    } else {
      return applicationUserRepository.findOneByUsername(username);
    }
  }

}
