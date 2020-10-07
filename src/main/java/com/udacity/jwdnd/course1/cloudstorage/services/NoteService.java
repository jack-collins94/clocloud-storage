package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NotesMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NoteService {


    private final NotesMapper notesMapper;

    public NoteService(NotesMapper notesMapper) {
        this.notesMapper = notesMapper;
    }

    public List<Note> getAllNotes(int userid){
        List<Note> notes = notesMapper.getAllNotes(userid);
        return notes;
    }

    public Note getById(Integer noteId){
        return notesMapper.getNoteById(noteId);
    }



    public int createNote(Note note){

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        UserService userService = new UserService(null, null);
//        User user = userService.getUser(authentication.getName());
//        if (user != null) {
//            // can not create a not for an unknown user
//        }
//
//        Note note = null;

        return notesMapper.insertNote(note);
    }

    public int updateNote(Note note){ return notesMapper.updateNote(note);}

    public int deleteNote(Integer noteId){ return notesMapper.deleteNote(noteId);}
}
