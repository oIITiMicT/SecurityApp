package com.example.InPostPW.services;

import com.example.InPostPW.dto.GetNoteForm;
import com.example.InPostPW.dto.NewNoteForm;
import com.example.InPostPW.model.Note;

import java.util.ArrayList;

public interface NoteService {

    void createNewNote(NewNoteForm newNoteForm, String token);

    ArrayList<Note> getNotes(GetNoteForm getNoteForm);
}
