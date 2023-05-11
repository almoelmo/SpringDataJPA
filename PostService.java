package com.example.posts.service;

import com.example.posts.model.Post;
import com.example.posts.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepository repository;

    public List<Post> findAll(long id){
        return repository.findAllByUserId(id);
    }

    public Page<Post> findAllPage(Pageable p){
        return repository.findAll(p);
    }
    public Post save(Post post) throws Exception {
        if (!StringUtils.hasText(post.getTitle())) {
            throw new Exception("Title is required");
        }
        if (!StringUtils.hasText(post.getContent())) {
            throw new Exception("Content is required");
        }
        post.setCreatedOn(LocalDateTime.now());
        post.setUpdatedOn(LocalDateTime.now());
        return repository.save(post);
    }

    public void update(Post post) throws Exception {
        if (!StringUtils.hasText(post.getTitle())) {
            throw new Exception("Title is required");
        }
        if (!StringUtils.hasText(post.getContent())) {
            throw new Exception("Content is required");
        }
        System.out.println(post);
        post.setUpdatedOn(LocalDateTime.now());
        repository.save(post);
        System.out.println("hello from end");
    }

    public Post findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void deleteById(long id) throws Exception {
        if (!repository.existsById(id)) {
            throw new Exception("Cannot find Post with id: " + id);
        }
        else {
            repository.deleteById(id);
        }
    }

    public long count() {
        return repository.count();
    }
}
