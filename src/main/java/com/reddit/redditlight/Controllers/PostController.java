package com.reddit.redditlight.Controllers;

import com.reddit.redditlight.Models.ApplicationUser;
import com.reddit.redditlight.Models.Comment;
import com.reddit.redditlight.Models.Post;
import com.reddit.redditlight.Services.MessageService;
import com.reddit.redditlight.Services.PostService;
import com.reddit.redditlight.Services.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PostController {

  private PostService postService;
  private ApplicationUserService applicationUserService;
  private MessageService messageService;

  @Value("${HOST}")
  private String hostname;

  @Autowired
  public PostController(PostService postService, ApplicationUserService applicationUserService, MessageService messageService) {
    this.postService = postService;
    this.applicationUserService = applicationUserService;
    this.messageService = messageService;
  }

  @ModelAttribute
  public void addCurrentUser(Model model, HttpServletRequest request) {
    model.addAttribute("currentUser", applicationUserService.getCurrentUser(request));
    model.addAttribute("unreadMessages", messageService.countUnread(request));
    model.addAttribute("hostname", hostname);
  }

  @GetMapping("/")
  public String getIndex(Model model) {
    model.addAttribute("posts", postService.getAllPosts());
    return "allposts";
  }

  @GetMapping("/newpost")
  public String newPost(Model model) {
    model.addAttribute("newPost", new Post());
    return "newPost";
  }

  @PostMapping("/newpost")
  public String saveNewPost(@ModelAttribute(value = "title") String title, @ModelAttribute(value = "content") String content, HttpServletRequest request) {
    postService.save(new Post(content, title, applicationUserService.getCurrentUser(request)));
    return "redirect:/";
  }

  @GetMapping("/viewpost/{id}")
  public String viewPost(Model model, @PathVariable("id") long id) {
    model.addAttribute("post", postService.findById(id));
    model.addAttribute("newComment", new Comment());
    return "post";
  }

  @GetMapping("/upvote/{id}")
  public String upvote(@PathVariable(value = "id") long postId, HttpServletRequest request) {
    ApplicationUser user = applicationUserService.getCurrentUser(request);
    postService.upvote(postId, user);
    return "redirect:/";
  }

  @GetMapping("/downvote/{id}")
  public String downvote(@PathVariable(value = "id") long postId, HttpServletRequest request) {
    ApplicationUser user = applicationUserService.getCurrentUser(request);
    postService.downvote(postId, user);
    return "redirect:/";
  }

  @GetMapping("/save/{id}")
  public String savePost(@PathVariable(value = "id") long postId, HttpServletRequest request) {
    ApplicationUser user = applicationUserService.getCurrentUser(request);
    postService.addSavedPost(postId, user);
    return "redirect:/";
  }

  @GetMapping("/unsave/{id}")
  public String unsavePost(@PathVariable(value = "id") long postId, HttpServletRequest request) {
    ApplicationUser user = applicationUserService.getCurrentUser(request);
    postService.unsavePost(postId, user);
    return "redirect:/";
  }

  @GetMapping("/saved")
  public String savedPosts(HttpServletRequest request, Model model) {
    ApplicationUser user = applicationUserService.getCurrentUser(request);
    model.addAttribute("posts", user.getSavedPosts());
    return "allposts";
  }

  @GetMapping("/search")
  public String search(@RequestParam(value = "title", required = false) Boolean title,
                       @RequestParam(value = "content", required = false) Boolean content,
                       @RequestParam(value = "comment", required = false) Boolean comment,
                       @RequestParam(value = "term") String searchTerm, Model model) {
    model.addAttribute("posts", postService.search(searchTerm, title,content,comment));
    return "allposts";
  }

  @GetMapping("/postsBy/{username}")
  public String listsPostsByUser(@PathVariable(value = "username") String username, Model model) {
    model.addAttribute("posts", postService.findPostsByUser(username));
    return "allposts";
  }

  @GetMapping("/postsByDate/{date}")
  public String listsPostsByDate(@PathVariable(value = "date") String date, Model model) {
    model.addAttribute("posts", postService.findByDate(date));
    return "allposts";
  }

}
