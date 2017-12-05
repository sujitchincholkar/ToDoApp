package com.bridgelabz.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.TypedQuery;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import com.bridgelabz.model.Collaborater;
import com.bridgelabz.model.Note;
import com.bridgelabz.model.User;

public class NoteDaoImpl implements NoteDao{
	
	@Autowired
	private SessionFactory factory; 
	
	@Autowired
	private UserDao userDao;
	
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
		Session session=factory.openSession();
		User user =session.get(User.class,userId);
		Set<Note> notes=user.getNotes();
		notes.size();
		session.close();
		return notes;
	}

	public Note getNoteById(int noteId) {
		Session session =factory.openSession();
		Note note=session.get(Note.class, noteId);
		session.close();
		return note;
	}

	public int saveCollborator(Collaborater collborate) {
		int collboratorId=0;
		Session session=factory.openSession();
		Transaction transaction=session.beginTransaction();
		try{
		collboratorId=(Integer) session.save(collborate);
		transaction.commit();
		}catch(HibernateException e){
			e.printStackTrace();
			transaction.rollback();
		}finally{
			session.close();
		}
		return collboratorId;
	}

	public List<User> getListOfUser(int noteId) {/*
		List<User> users=null;
		Session session=null;
		int i=0;
		try{
		session=factory.openSession();
		Query<Collaborater> query=session.createQuery("From Collaborater where note=:noteId");
		query.setParameter("noteId", noteId);
		 List<Collaborater> collaborateList=query.list();
		while(collaborateList.size()>i) {
			collaborateList.get(0).getShareWithId().getEmail();
			users.add(collaborateList.get(0).getShareWithId());
		}	
		}catch(Exception e){
		e.printStackTrace();
		}finally{
			session.close();
		}
		return users;
	*/
		Session session = factory.openSession();
		Query querycollab = session.createQuery("select c.shareWithId from Collaborater c where c.note= " + noteId);
		List<User> listOfSharedCollaborators = querycollab.list();
		System.out.println("listOfSharedCollaborators "+listOfSharedCollaborators);
		session.close();
		return listOfSharedCollaborators;	
	}

	public Set<Note> getCollboratedNotes(int userId) {
		// TODO Auto-generated method stub
		Session session = factory.openSession();
		Query query = session.createQuery("select c.note from Collaborater c where c.shareWithId= " + userId);
		List<Note> colllboratedNotes = query.list();
		Set<Note> notes=new HashSet<Note>(colllboratedNotes);
		
		session.close();
		return notes;
	}

	public int removeCollborator(int shareWith,int noteId) {
		Session session = factory.openSession();
		Transaction transaction=session.beginTransaction();
		Query query = session.createQuery("delete  Collaborater c where c.shareWithId= "+shareWith+" and c.note="+noteId );
	/*	query.setParameter("noteId", noteId);
		query.setParameter("shareWith", shareWith);*/
		int status=query.executeUpdate();
		session.close();
		return status;
	}

	public List<Note> getNotesInTrash() {
		Session session = factory.openSession();
		Query query = session.createQuery("From Note where isTrashed=true");
		List<Note> notes = query.list();		
		session.close();
		return notes;
	}

}
