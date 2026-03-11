package com.azerpost.app.service;

import com.azerpost.app.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    User getUser(Long id);

    List<User> getAllUsers();
}
