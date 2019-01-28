package com.reddit.redditlight.Security;

import com.reddit.redditlight.Models.ApplicationUser;
import com.reddit.redditlight.Repositories.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service("userDetailsService")
@Transactional
public class ApplicationUserDetailsService implements UserDetailsService {

  @Autowired
  ApplicationUserRepository applicationUserRepository;

  public User loadUserByUsername(String username)
      throws UsernameNotFoundException {

    boolean accountNonExpired = true;
    boolean credentialsNonExpired = true;
    boolean accountNonLocked = true;
    try {
      ApplicationUser user = applicationUserRepository.findByUsername(username);
      if (user == null) {
        throw new UsernameNotFoundException(
            "No user found with username: " + username);
      }

      List< GrantedAuthority > authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE.ADMIN"));
      System.out.println(username);
      System.out.println(user.isVerified());

      return new org.springframework.security.core.userdetails.User(
          user.getUsername(),
          user.getPassword(),
          user.isVerified(),
          accountNonExpired,
          credentialsNonExpired,
          accountNonLocked,
          authorities);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
