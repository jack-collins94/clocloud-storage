package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    private UserService userService;
    private NoteService noteService;
    private CredentialService credentialService;
    private FilesService filesService;
    private EncryptionService encryptionService;

    public HomeController(UserService userService, NoteService noteService, CredentialService credentialService, FilesService filesService, EncryptionService encryptionService) {
        this.userService = userService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.filesService = filesService;
        this.encryptionService = encryptionService;
    }

    @GetMapping
    public String homeView(Authentication authentication, Model model){
        Integer userId = userService.getUser(authentication.getName()).getUserId();
        model.addAttribute("notes", noteService.getAllNotes(userId));
        model.addAttribute("credentials",credentialService.getAllCredentials(userId));
        model.addAttribute("files", filesService.getAllUserFiles(userId));
        model.addAttribute("encryptionService", encryptionService);
        return "home";
    }
}
