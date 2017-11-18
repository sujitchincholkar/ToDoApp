package com.bridgelabz.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.bridgelabz.dao.NoteDao;
import com.bridgelabz.dao.UserDao;
import com.bridgelabz.model.Note;

public class NoteServiceImpl implements NoteService{
	
	@Autowired
	private NoteDao noteDao;
	
	public int saveNote(Note note) {
		int id=noteDao.saveNote(note);
		return id;
	}

	public boolean updateNote(Note note) {
		
		return noteDao.updateNote(note);
	}

	public boolean deleteNote(Note note) {
		// TODO Auto-generated method stub
		return noteDao.deleteNote(note);
	}

	public Set<Note> getNotes(int id) {
		// TODO Auto-generated method stub
		return noteDao.getNotes(id);
	}

	public Note getNoteById(int noteId) {
		return noteDao.getNoteById(noteId);
		
	}
	
}
