package com.notesApp.demo.service;

import com.notesApp.demo.model.Note;
import com.notesApp.demo.model.User;
import com.notesApp.demo.repository.NoteRepository;
import com.notesApp.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    // Get current authenticated user
    private User getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    // Create new note
    public Note createNote(Note note) {
        User currentUser = getAuthenticatedUser();
        note.setUser(currentUser);
        return noteRepository.save(note);
    }

    // Get all notes for current user
    public List<Note> getAllNotesForUser() {
        User currentUser = getAuthenticatedUser();
        return noteRepository.findByUserId(currentUser.getId());
    }

    // Get single note by ID (ensure ownership)
    public Note getNoteById(Long id) {
        User currentUser = getAuthenticatedUser();
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found with id: " + id));

        if (!note.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Access denied");
        }
        return note;
    }

    // Update note (title or content)
    public Note updateNote(Long id, Note updatedNote) {
        Note existingNote = getNoteById(id);

        if (updatedNote.getTitle() != null) existingNote.setTitle(updatedNote.getTitle());
        if (updatedNote.getContent() != null) existingNote.setContent(updatedNote.getContent());

        return noteRepository.save(existingNote);
    }

    // Delete note (only owner can delete)
    public String deleteNote(Long id) {
        Note note = getNoteById(id);
        noteRepository.delete(note);
        return "Note deleted successfully";
    }
}

