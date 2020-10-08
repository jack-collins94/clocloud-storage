package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notes")
public class NotesController {

    private NoteService noteService;
    private UserService userService;

    public NotesController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping("/add")
    public String addOrUpdateNote(Authentication authentication, NoteForm noteForm){
        Note newNote = null;
        if(noteForm.getNoteId() == null){
        newNote = new Note();
        newNote.setNoteTitle(noteForm.getNoteTitle());
        newNote.setNoteDescription(noteForm.getNoteDescription());
        newNote.setUserId(userService.getUser(authentication.getName()).getUserId());
        noteService.createNote(newNote);
        }else{
            newNote = noteService.getById(noteForm.getNoteId());
            newNote.setNoteTitle(noteForm.getNoteTitle());
            newNote.setNoteDescription(noteForm.getNoteDescription());
            noteService.updateNote(newNote);
        }
        return "redirect:/home";

    }

    @GetMapping("/delete/{noteId}")
    public String deleteNote(@PathVariable Integer noteId){

        noteService.deleteNote(noteId);

        return "redirect:/home";
    }


    // get - retrive info - html/data - safe operation - should not change the state of the system
    // post - change the state on the serve (create data)
    // put - update the data (api)
    // delete - delete the data (api)
}
