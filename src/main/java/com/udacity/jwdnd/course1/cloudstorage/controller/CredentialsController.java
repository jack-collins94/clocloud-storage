package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialsForm;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.SecureRandom;
import java.util.Base64;

@Controller
@RequestMapping("/credentials")
public class CredentialsController {

    private CredentialService credentialService;
    private UserService userService;

    public CredentialsController(CredentialService credentialService, UserService userService) {
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @PostMapping("/add")
    public String addNewCredentials(Authentication authentication, CredentialsForm credentialsForm, Model model){

        Credentials newCredential = null;
        if(credentialsForm.getCredentialId() == null) {
            newCredential = new Credentials();
            newCredential.setUrl(credentialsForm.getUrl());
            newCredential.setUsername(credentialsForm.getUsername());
            newCredential.setPassword(credentialsForm.getPassword());
            newCredential.setUserId(userService.getUser(authentication.getName()).getUserId());
            credentialService.createCredentials(newCredential);
        }else{
            newCredential = credentialService.getById(credentialsForm.getCredentialId());
            newCredential.setUrl(credentialsForm.getUrl());
            newCredential.setUsername(credentialsForm.getUsername());
            newCredential.setPassword(credentialsForm.getPassword());
            credentialService.updateCredential(newCredential);
        }
        return "redirect:/home";

    }

    @GetMapping("/delete/{credentialId}")
    public String deleteCredential(@PathVariable Integer credentialId){

        credentialService.deleteCredential(credentialId);

        return ("redirect:/home");
    }
}
