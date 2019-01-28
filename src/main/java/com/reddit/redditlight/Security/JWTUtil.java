package com.reddit.redditlight.Security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static com.reddit.redditlight.Security.SecurityConstants.HEADER_STRING;
import static com.reddit.redditlight.Security.SecurityConstants.SECRET;
import static com.reddit.redditlight.Security.SecurityConstants.TOKEN_PREFIX;

@Component
public class JWTUtil {
  public String getUserFromRequest(HttpServletRequest req) {
    if (req.getHeader(HEADER_STRING) != null) {
      String token = req.getHeader(HEADER_STRING)
          .replace(TOKEN_PREFIX, "");
      String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
          .build()
          .verify(token.replace(TOKEN_PREFIX, ""))
          .getSubject();
      return user;
    }
    return null;
  }
}
