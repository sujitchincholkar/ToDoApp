package com.bridgelabz.dao;

import java.util.List;
import java.util.Set;

import com.bridgelabz.model.Collaborater;
import com.bridgelabz.model.Note;
import com.bridgelabz.model.User;

public interface NoteDao {
	public int saveNote(Note note);
	public boolean updateNote(Note note);
	public boolean deleteNote(Note note);
	public Set<Note> getNotes(int id);
	public Note getNoteById(int noteId);
	public int saveCollborator(Collaborater collborate);
	public List<User> getListOfUser(int noteId);
	public Set<Note> getCollboratedNotes(int userId);
}
