package com.bridgelabz.service;

import java.util.Set;

import com.bridgelabz.model.Note;

public interface NoteService {
	public int saveNote(Note note);
	public boolean updateNote(Note note);
	public boolean deleteNote(Note note);
	public Note getNoteById(int noteId);
	public Set<Note> getNotes(int id);

}
