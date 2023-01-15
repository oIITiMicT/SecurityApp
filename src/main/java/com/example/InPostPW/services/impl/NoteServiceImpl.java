package com.example.InPostPW.services.impl;

import com.example.InPostPW.dto.GetNoteForm;
import com.example.InPostPW.dto.NewNoteForm;
import com.example.InPostPW.model.Note;
import com.example.InPostPW.model.User;
import com.example.InPostPW.repository.NoteRepository;
import com.example.InPostPW.services.NoteService;
import com.example.InPostPW.services.TokenService;
import com.example.InPostPW.services.UserService;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final TokenService tokenService;

    private final NoteRepository noteRepository;

    private final UserService userService;

    @Override
    public void createNewNote(NewNoteForm newNoteForm, String token) {
        try {
            String subject = tokenService.getSubjectFromToken(token);
            if (userService.findUserByEmail(subject).isEmpty()) return;
            Note note = new Note();
            note.setAuthor(userService.findUserByEmail(subject).get());
            note.setText(newNoteForm.note);
            noteRepository.save(note);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Note> getNotes(GetNoteForm getNoteForm) {
        String subject = null;
        try {
            subject = tokenService.getSubjectFromToken(getNoteForm.token);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        if (userService.findUserByEmail(subject).isEmpty()) return new ArrayList<>();
        User user = userService.findUserByEmail(subject).get();
        return noteRepository.getAllByAuthor(user);
    }
}
