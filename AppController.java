package com.example.posts.controller;

import com.example.posts.model.Post;
import com.example.posts.model.User;
import com.example.posts.service.PostService;
import com.example.posts.service.UserService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AppController {
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;

    @GetMapping(value = {"/", "/users"})
    public String getUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users";
    }
    @GetMapping(value = "/users/{id}/posts")
    public String getPosts(Model model, @PathVariable long id) {
        model.addAttribute("posts", postService.findAll(id));
        model.addAttribute("user", userService.findById(id));
        return "posts";
    }

    @GetMapping(value = "/users/{id}")
    public String getUserById(Model model, @PathVariable long id) {
        User user = null;
        try {
            user = userService.findById(id);
            model.addAttribute("allowDelete", false);
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
        }
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping(value = "/users/{userid}/posts/{id}")
    public String getPostById(Model model, @PathVariable(name = "userid") long userId, @PathVariable long id) {
        Post post = null;
        try {
            post = postService.findById(id);
            model.addAttribute("allowDelete", false);
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
        }
        model.addAttribute("user", userService.findById(userId));
        model.addAttribute("post", post);
        return "post";
    }

    @GetMapping(value = {"/users/new"})
    public String showNewUser(Model model) {
        User user = new User();
        model.addAttribute("add", true);
        model.addAttribute("user", user);
        return "user-form";
    }

    @PostMapping(value = "/users/new")
    public String addUser(Model model, @ModelAttribute("user") User user) {
        try {
            userService.save(user);
            return "redirect:/users";
        } catch (Exception ex) {
            String errorMessage = ex.getMessage();
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("add", true);
            return "user-form";
        }
    }

    @GetMapping(value = {"/users/{id}/posts/new"})
    public String showNewPost(Model model, @PathVariable long id) {
        Post post = new Post();
        model.addAttribute("add", true);
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("post", post);
        return "post-form";
    }

    @PostMapping(value = "/users/{userid}/posts/new")
    public String addPost(Model model, @PathVariable(name = "userid") long userId, @ModelAttribute("post") Post post) {
        try {
            post.setUser(userService.findById(userId));
            postService.save(post);
            return "redirect:/users/"+ userId +"/posts";
        } catch (Exception ex) {
            String errorMessage = ex.getMessage();
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("add", true);
            return "post-form";
        }
    }

    @GetMapping(value = {"/users/{id}/edit"})
    public String showEditUser(Model model, @PathVariable Long id) {
        User user = null;
        try {
            user = userService.findById(id);
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
        }
        model.addAttribute("add", false);
        model.addAttribute("user", user);
        return "user-form";
    }

    @PostMapping(value = {"/users/{id}/edit"})
    public String updateUser(Model model,
                             @PathVariable long id,
                             @ModelAttribute("user") User user) {
        try {
            user.setId(id);
            userService.update(user);
            return "redirect:/users/" + user.getId();
        } catch (Exception ex) {
            String errorMessage = ex.getMessage();
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("add", false);
            return "user-form";
        }
    }

    @GetMapping(value = {"/users/{userid}/posts/{id}/edit"})
    public String showEditPost(Model model, @PathVariable(name = "userid") long userId, @PathVariable Long id) {
        Post post = null;
        try {
            post = postService.findById(id);
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
        }
        model.addAttribute("user", userService.findById(userId));
        model.addAttribute("add", false);
        model.addAttribute("post", post);
        return "post-form";
    }

    @PostMapping(value = {"/users/{userid}/posts/{id}/edit"})
    public String updatePost(Model model,
                             @PathVariable(name = "userid") long userId,
                             @PathVariable long id,
                             @ModelAttribute("post") Post post) {
        try {
            post.setId(id);
            post.setUser(userService.findById(userId));
            postService.update(post);
            return "redirect:/users/" + userId + "/posts";
        } catch (Exception ex) {
            String errorMessage = ex.getMessage();
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("add", false);
            return "post-form";
        }
    }

    @GetMapping(value = {"/users/{id}/delete"})
    public String showDeleteUserById(Model model, @PathVariable long id) {
        User user = null;
        try {
            user = userService.findById(id);
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
        }

        model.addAttribute("allowDelete", true);
        model.addAttribute("user", user);
        return "user";
    }

    @PostMapping(value = {"/users/{id}/delete"})
    public String deleteUserById(Model model, @PathVariable long id) {
        try {
            userService.deleteById(id);
            return "redirect:/users";
        } catch (Exception ex) {
            String errorMessage = ex.getMessage();
            model.addAttribute("errorMessage", errorMessage);
            return "user";
        }
    }

    @GetMapping(value = {"/users/{userid}/posts/{id}/delete"})
    public String showDeletePostById(Model model,
                                     @PathVariable(name = "userid") long userId,
                                     @PathVariable long id) {
        Post post = null;
        try {
            post = postService.findById(id);
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
        }
        model.addAttribute("user", userService.findById(userId));
        model.addAttribute("allowDelete", true);
        model.addAttribute("post", post);
        return "post";
    }

    @PostMapping(value = {"/users/{userid}/posts/{id}/delete"})
    public String deletePostById(Model model,
                                 @PathVariable(name = "userid") long userId,
                                 @PathVariable long id) {
        try {
            postService.deleteById(id);
            return "redirect:/users/" + userId + "/posts";
        } catch (Exception ex) {
            String errorMessage = ex.getMessage();
            model.addAttribute("errorMessage", errorMessage);
            return "post";
        }
    }
}
