package com.bridgelabz.dao;

import java.util.Set;

import com.bridgelabz.model.Note;

public interface NoteDao {
	public int saveNote(Note note);
	public boolean updateNote(Note note);
	public boolean deleteNote(Note note);
	public Set<Note> getNotes(int id);
	public Note getNoteById(int noteId);
}
