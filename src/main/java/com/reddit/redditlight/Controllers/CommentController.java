package com.reddit.redditlight.Controllers;


import com.reddit.redditlight.Models.Comment;
import com.reddit.redditlight.Services.CommentService;
import com.reddit.redditlight.Services.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CommentController {

  private CommentService commentService;
  private ApplicationUserService applicationUserService;

  @Autowired
  public CommentController(CommentService commentService, ApplicationUserService applicationUserService) {
    this.commentService = commentService;
    this.applicationUserService = applicationUserService;
  }

  @PostMapping("/comment/{id}")
  public String postComment(@ModelAttribute("newComment") Comment comment, @RequestParam("id") long id, HttpServletRequest request) {
    commentService.save(id, comment, applicationUserService.getCurrentUser(request));
    return "redirect:/viewpost/{id}";
  }

  @PostMapping("/reply/{postId}/{commentId}")
  public String replyToComment(@ModelAttribute("newComment") Comment comment,@PathVariable("postId") long postId, @PathVariable("commentId") long commentId, HttpServletRequest request){
    commentService.addReply(postId, comment,commentId,applicationUserService.getCurrentUser(request));
    return "redirect:/viewpost/"+postId;
  }

}
