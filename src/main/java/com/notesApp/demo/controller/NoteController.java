package com.notesApp.demo.controller;

import com.notesApp.demo.model.Note;
import com.notesApp.demo.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    // ----------------------------------
    // GET all notes of authenticated user
    // ----------------------------------
    @GetMapping
    public ResponseEntity<List<Note>> getAllNotes() {
        List<Note> notes = noteService.getAllNotesForUser();
        return ResponseEntity.ok(notes);
    }

    // ----------------------------------
    // GET single note by ID (must belong to user)
    // ----------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<?> getNoteById(@PathVariable Long id) {
        try {
            Note note = noteService.getNoteById(id);
            return ResponseEntity.ok(note);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    // ----------------------------------
    // CREATE new note
    // ----------------------------------
    @PostMapping
    public ResponseEntity<?> createNote(@RequestBody Note note) {
        try {
            Note createdNote = noteService.createNote(note);
            return ResponseEntity.ok(Map.of(
                    "message", "Note created successfully",
                    "note", createdNote
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ----------------------------------
    // UPDATE existing note
    // ----------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<?> updateNote(@PathVariable Long id, @RequestBody Note note) {
        try {
            Note updatedNote = noteService.updateNote(id, note);
            return ResponseEntity.ok(Map.of(
                    "message", "Note updated successfully",
                    "note", updatedNote
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    // ----------------------------------
    // DELETE note by ID
    // ----------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable Long id) {
        try {
            String message = noteService.deleteNote(id);
            return ResponseEntity.ok(Map.of("message", message));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }
}

