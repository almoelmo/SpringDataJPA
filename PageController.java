package com.example.posts.controller;

import com.example.posts.service.PostService;
import com.example.posts.service.UserService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @GetMapping(value = {"/users/{id}/posts-all"})
    public String getPostsAll(Model model, @PathVariable long id){
        Pageable elems = PageRequest.of(0, Math.toIntExact(postService.count()));
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("posts", postService.findAllPage(elems).getContent());
        return "posts";
    }

    @GetMapping(value = {"/users/{id}/posts-sorted"})
    public String getPostsSorted(Model model, @PathVariable long id){
        Pageable elems = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "createdOn"));
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("posts", postService.findAllPage(elems).getContent());
        return "posts";
    }

    @GetMapping(value = {"/users/{id}/posts-expressions"})
    public String getPostsOnPages(Model model,
                                  @PathVariable long id,
                                  @RequestParam(value = "offset", defaultValue = "0") @Min(0) int offset,
                                  @RequestParam(value = "limit", defaultValue = "5") @Min(1) @Max(20) int limit){
        Pageable elems = PageRequest.of(offset, limit);
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("posts", postService.findAllPage(elems).getContent());
        return "posts";
    }
}
