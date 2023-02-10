package com.example.InPostPW.controller;

import com.example.InPostPW.dto.GetNoteForm;
import com.example.InPostPW.dto.NewNoteForm;
import com.example.InPostPW.services.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class NoteController {

    private final NoteService noteService;


    @PostMapping("/note/new")
    public ResponseEntity<?> createNewNote(@RequestBody NewNoteForm newNoteForm) throws InterruptedException {
        if (newNoteForm.note != null && newNoteForm.token != null) {
            noteService.createNewNote(newNoteForm, newNoteForm.token);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/note/get")
    public ResponseEntity<?> getNotes(@RequestBody GetNoteForm getNoteForm) throws InterruptedException {
        if (getNoteForm.token != null) {
            return new ResponseEntity<>(noteService.getNotes(getNoteForm), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }
}
