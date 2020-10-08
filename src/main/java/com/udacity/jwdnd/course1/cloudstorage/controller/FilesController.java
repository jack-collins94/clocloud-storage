package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Files;
import com.udacity.jwdnd.course1.cloudstorage.services.FilesService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/files")
public class FilesController {

    private FilesService filesService;
    private UserService userService;

    public FilesController(FilesService filesService, UserService userService) {
        this.filesService = filesService;
        this.userService = userService;
    }

    @PostMapping("/add")
    public String addFile(Authentication authentication, @RequestParam("fileUpload") MultipartFile multipartFile, Model model){
        String errorMessage = null;
        Files files = null;

        Integer userId = userService.getUser(authentication.getName()).getUserId();
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        if(!filesService.checkForDuplicates(userId,fileName).isEmpty()){
            errorMessage = "File name already exists.";
        }



        if(errorMessage == null){
            files = new Files();
            files.setFileName(fileName);
            files.setContentType(multipartFile.getContentType());
            files.setFileSize(multipartFile.getSize());

            try {
                files.setFileData(multipartFile.getBytes());
            } catch (IOException e) {
                errorMessage = "Error uploading file.";
            }
            files.setUserId(userId);
        }

        if (errorMessage == null) {
            filesService.uploadFile(files);
            return "redirect:/result?success";
        } else {
            model.addAttribute("fileUploadError", errorMessage);
            return "result";
        }

    }

    @GetMapping("/delete/{fileId}")
    public String deleteFile(@PathVariable(name = "fileId") Integer fileId){
        filesService.deleteFile(fileId);

        return "redirect:/result?success";
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity downloadFile(@PathVariable(name = "fileId") String fileId) {
        Files file = filesService.downloadFile(Integer.parseInt(fileId));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(file.getFileData());
    }

    }

