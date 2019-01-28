package com.reddit.redditlight.Services;

import com.reddit.redditlight.Models.ApplicationUser;
import com.reddit.redditlight.Models.VerificationToken;
import com.reddit.redditlight.Repositories.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.UUID;

@Service
public class VerificationServiceImpl implements VerificationService {

  private ApplicationUserService applicationUserService;
  private VerificationTokenRepository verificationTokenRepository;
  @Value("${VERIFICATION_EMAIL_LOGIN}")
  private String v_email;
  @Value("${VERIFICATION_EMAIL_PASSWORD}")
  private String v_password;
  @Value("${HOST}")
  private String hostname;

  @Autowired
  public VerificationServiceImpl(ApplicationUserService applicationUserService, VerificationTokenRepository verificationTokenRepository) {
    this.applicationUserService = applicationUserService;
    this.verificationTokenRepository = verificationTokenRepository;
  }

  public void startValidation(ApplicationUser user) {
    VerificationToken token = generateToken(user);
    sendMessage(user, token);
  }

  public VerificationToken generateToken(ApplicationUser user) {
    String tokenString = UUID.randomUUID()
        .toString();
    VerificationToken token = new VerificationToken(tokenString, user);
    verificationTokenRepository.save(token);
    return token;
  }

  public void sendMessage(ApplicationUser user, VerificationToken verificationToken) {
    String m_subject = "Verify your RedditLight registration";
    Properties props = GetEmailProps();
    try {
      Authenticator auth = new SMTPAuthenticator();
      Session session = Session.getInstance(props, auth);
      MimeMessage msg = new MimeMessage(session);
      msg.setText(getEmailContent(user, verificationToken));
      msg.setSubject(m_subject);
      msg.setFrom(new InternetAddress(v_email));
      msg.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
      Transport.send(msg);
    } catch (Exception mex) {
      mex.printStackTrace();
    }
  }

  private String getEmailContent(ApplicationUser user, VerificationToken verificationToken) {
    return ("Hi " + user.getUsername() + "," + "\n" + "Click on the link below to complete your registration on RedditLight:" + "\n" +
        "http://" + hostname + "/users/confirm/" + verificationToken.getToken());
  }

  private Properties GetEmailProps() {
    String port = "465";
    String host = "smtp.gmail.com";
    Properties props = new Properties();
    props.put("mail.smtp.user", v_email);
    props.put("mail.smtp.host", host);
    props.put("mail.smtp.port", port);
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.socketFactory.port", port);
    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    props.put("mail.smtp.socketFactory.fallback", "false");
    return props;
  }

  private class SMTPAuthenticator extends javax.mail.Authenticator {
    public PasswordAuthentication getPasswordAuthentication() {
      return new PasswordAuthentication(v_email, v_password);
    }
  }

  @Override
  public boolean tokenExists(String token) {
    if (!token.isEmpty() && token != null) {
      VerificationToken verificationToken = findTokenByName(token);
      return verificationToken != null;
    }
    return false;
  }

  @Override
  public VerificationToken verify(String token) {
    VerificationToken verificationToken = findTokenByName(token);
    ApplicationUser user = verificationToken.getUser();
    user.setVerified(true);
    applicationUserService.save(user);
    return verificationToken;
  }

  @Override
  public void deleteToken(String token) {
    verificationTokenRepository.deleteByToken(token);
  }

  public VerificationToken findTokenByName(String token) {
    return verificationTokenRepository.findByToken(token);
  }

  @Override
  public String getVerificationMessage(ApplicationUser user) {
    if (!user.isVerified()) {
      return "Thank you for registering! We sent an activation email to " + user.getEmail() + ".";
    } else {
      return user.getEmail() + " successfully verified. You may now log in below.";
    }
  }

  @Override
  public String getVerificationError() {
    return "Invalid token!";
  }

}
