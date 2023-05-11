package com.example.posts.service;

import com.example.posts.model.Post;
import com.example.posts.model.User;
import com.example.posts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;
    public List<User> findAll(){
        return repository.findAll();
    }
    public User save(User user) throws Exception {
        if (!StringUtils.hasText(user.getName())) {
            throw new Exception("Title is required");
        }
        if (!StringUtils.hasText(user.getEmail())) {
            throw new Exception("Content is required");
        }
        return repository.save(user);
    }
    public void update(User user) throws Exception {
        if (!StringUtils.hasText(user.getName())) {
            throw new Exception("Name is required");
        }
        if (!StringUtils.hasText(user.getEmail())) {
            throw new Exception("Email is required");
        }
        repository.save(user);
    }

    public User findById(Long id) {
        return repository.findById(id).orElse(null);
    }
    public void deleteById(long id) throws Exception {
        if (!repository.existsById(id)) {
            throw new Exception("Cannot find User with id: " + id);
        }
        else {
            repository.deleteById(id);
        }
    }
}
