package com.bridgelabz.dao;

import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import com.bridgelabz.model.Note;
import com.bridgelabz.model.User;

public class NoteDaoImpl implements NoteDao{
	@Autowired
	SessionFactory factory; 
	@Autowired
	UserDao userDao;
	public int saveNote(Note note) {
		int noteId=0;
		Session session=factory.openSession();
		Transaction transaction=session.beginTransaction();
		try{
		noteId=(Integer) session.save(note);
		transaction.commit();
		}catch(HibernateException e){
			e.printStackTrace();
			transaction.rollback();
		}finally{
			session.close();
		}
		return noteId;
	}

	public boolean updateNote(Note note) {
		Session session=factory.openSession();
		Transaction transaction=session.beginTransaction();
		try{
		session.update(note);
		transaction.commit();
		}catch(HibernateException e){
			e.printStackTrace();
			transaction.rollback();
			return false;
		}finally{
			session.close();
		}
		return true;
	}

	public boolean deleteNote(Note note) {
		int noteId=0;
		Session session=factory.openSession();
		Transaction transaction=session.beginTransaction();
		try{
		session.delete(note);
		transaction.commit();
		}catch(HibernateException e){
			e.printStackTrace();
			transaction.rollback();
			return false;
		}finally{
			session.close();
		}
		return true;
		
	}

	public Set<Note> getNotes(int userId) {
		User user =userDao.getUserById(userId);
		Set<Note> notes=user.getNotes();
		return notes;
	}

	public Note getNoteById(int noteId) {
		Session session =factory.openSession();
		Note note=session.get(Note.class, noteId);
		return note;
	}

}
