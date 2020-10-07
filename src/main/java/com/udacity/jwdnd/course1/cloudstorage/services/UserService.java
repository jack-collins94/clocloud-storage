package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.model.SignupUserRequest;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final HashService hashService;

    public UserService(UserMapper userMapper, HashService hashService) {
        this.userMapper = userMapper;
        this.hashService = hashService;
    }

    private boolean isUsernameAvailable(String username){
        return userMapper.getUser(username) == null;
    }

    public static class UserAlreadyExistsException extends UserServiceException {
        public UserAlreadyExistsException() {
            super("The username already exists.");
        }
    }

    public static class UserServiceException extends RuntimeException {
        public UserServiceException(String message) {
            super(message);
        }

        public UserServiceException(String message, Throwable error) {
            super(message, error);
        }
    }

    public void createUser(SignupUserRequest signupUserRequest) {
        if(!isUsernameAvailable(signupUserRequest.getUsername())) {
            throw new UserAlreadyExistsException();
        }

        String encodedSalt = salt();
        String hashedPassword = hashService.getHashedValue(signupUserRequest.getPassword(), encodedSalt);

        int rowsAdded = userMapper.insertUser(new User(signupUserRequest.getUsername(), encodedSalt, hashedPassword, signupUserRequest.getFirstName(), signupUserRequest.getLastName()));

        if(rowsAdded < 1 ){
            throw new UserServiceException("There was an error signing you up. Please try again.");
        }
    }

    private String salt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public User getUser(String username) {
        return userMapper.getUser(username);
    }
}
