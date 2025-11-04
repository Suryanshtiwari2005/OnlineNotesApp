package com.notesApp.demo.repository;

import com.notesApp.demo.model.Note;
import com.notesApp.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUser(User user);
}
