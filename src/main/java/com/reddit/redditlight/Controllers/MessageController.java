package com.reddit.redditlight.Controllers;

import com.reddit.redditlight.Models.Message;
import com.reddit.redditlight.Services.ApplicationUserService;
import com.reddit.redditlight.Services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class MessageController {

  private MessageService messageService;
  private ApplicationUserService applicationUserService;

  @Autowired
  public MessageController(MessageService messageService, ApplicationUserService applicationUserService) {
    this.messageService = messageService;
    this.applicationUserService = applicationUserService;
  }

  @ModelAttribute
  public void addCurrentUser(Model model, HttpServletRequest request) {
    model.addAttribute("currentUser", applicationUserService.getCurrentUser(request));
    model.addAttribute("unreadMessages", messageService.countUnread(request));
  }

  @GetMapping("/messages")
  public String viewMessages(Model model, HttpServletRequest request){
    model.addAttribute("sentMessages", messageService.findSentMessages(request));
    List<Message> receivedMessages= messageService.findReceivedMessages(request);
    model.addAttribute("receivedMessages", receivedMessages);
    List<Message> newMessages= messageService.findNewMessages(receivedMessages);
    model.addAttribute("newMessages",newMessages);
    return "messages";
  }

  @GetMapping("/readall")
  public String markAllAsRead(HttpServletRequest request){
    messageService.markAllAsRead(messageService.findReceivedMessages(request));
    return "redirect:/messages";
  }

  @GetMapping("/message/{user}")
  public String renderSendMessage(Model model, @PathVariable("user") String targetUser){
    model.addAttribute("targetUser", targetUser);
    return "sendMessage";
  }

  @PostMapping("/sendMessage/{user}")
  public String sendMessage(@PathVariable("user") String targetUser, @ModelAttribute(value = "content") String content, HttpServletRequest request){
    messageService.sendMessage(content,targetUser,applicationUserService.getCurrentUser(request));
    return "redirect:/messages";
  }


}
