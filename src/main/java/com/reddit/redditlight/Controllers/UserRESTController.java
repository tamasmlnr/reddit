package com.reddit.redditlight.Controllers;

import com.reddit.redditlight.Models.ApplicationUser;
import com.reddit.redditlight.Services.ApplicationUserService;
import com.reddit.redditlight.Services.VerificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserRESTController {

  private ApplicationUserService applicationUserService;
  private BCryptPasswordEncoder bCryptPasswordEncoder;
  private VerificationService verificationService;

  public UserRESTController(ApplicationUserService applicationUserService, BCryptPasswordEncoder bCryptPasswordEncoder, VerificationService verificationService) {
    this.applicationUserService = applicationUserService;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.verificationService = verificationService;
  }

  @PostMapping("/sign-up")
  public ResponseEntity<?> signUp(@RequestBody ApplicationUser user) {
    List<String> errorMessages = applicationUserService.validateSignup(user, user.getPassword());
    if (errorMessages.size() > 0) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
          .body(errorMessages);
    } else {
      applicationUserService.save(user);
      user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
      verificationService.startValidation(user);
      return ResponseEntity.status(HttpStatus.OK)
          .body("Successful registration");
    }
  }
}

