package com.reddit.redditlight.Services;

import com.reddit.redditlight.Models.ApplicationUser;
import com.reddit.redditlight.Models.Message;
import com.reddit.redditlight.Repositories.MessageRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

  private ApplicationUserService applicationUserService;
  private MessageRepository messageRepository;

  public MessageServiceImpl(ApplicationUserService applicationUserService, MessageRepository messageRepository) {
    this.applicationUserService = applicationUserService;
    this.messageRepository = messageRepository;
  }

  public void sendMessage(String content, String targetUser, ApplicationUser currentUser) {
    ApplicationUser receivingUser = applicationUserService.findByUsername(targetUser);
    Message newMessage = new Message();
    newMessage.setContent(content);
    newMessage.setDate(LocalDateTime.now());
    newMessage.setReceivingUser(receivingUser);
    newMessage.setSendingUser(currentUser);
    newMessage.setUserRead(false);
    messageRepository.save(newMessage);
  }

  @Override
  public List<Message> findSentMessages(HttpServletRequest request) {
    ApplicationUser user = applicationUserService.getCurrentUser(request);
    List<Message> sentMessages=messageRepository.findAllBySendingUser(user);
    Collections.sort(sentMessages);
    return sentMessages;
  }

  @Override
  public List<Message> findReceivedMessages(HttpServletRequest request) {
    ApplicationUser user = applicationUserService.getCurrentUser(request);
    List<Message> receivedMessages=messageRepository.findAllByReceivingUser(user);
    Collections.sort(receivedMessages);
    return receivedMessages;
  }

  @Override
  public int countUnread(HttpServletRequest request) {
    ApplicationUser user = applicationUserService.getCurrentUser(request);
    return messageRepository.countAllByUserReadIsFalseAndReceivingUserIs(user);
  }

  @Override
  public List<Message> findNewMessages(List<Message> receivedMessages) {
    List<Message> unreadMessages =receivedMessages.stream()
        .filter(m -> !m.isUserRead())
        .collect(Collectors.toList());
 return unreadMessages;
  }

  @Override
  public void markAllAsRead(List<Message> newMessages) {
    for (Message message:newMessages){
      message.setUserRead(true);
      messageRepository.save(message);
    }
  }
}
