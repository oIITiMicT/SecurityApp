package com.example.InPostPW.repository;

import com.example.InPostPW.model.Note;
import com.example.InPostPW.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

    ArrayList<Note> getAllByAuthor(User user);
}
