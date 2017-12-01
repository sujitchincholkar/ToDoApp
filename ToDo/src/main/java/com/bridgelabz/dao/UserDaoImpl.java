package com.bridgelabz.dao;

import java.util.List;
import java.util.Set;

import javax.persistence.TypedQuery;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import com.bridgelabz.model.Label;
import com.bridgelabz.model.User;

public class UserDaoImpl implements UserDao{
	@Autowired
	private SessionFactory factory;

	public int saveUser(User user) {
		// TODO Auto-generated method stub
		int userId=0;
		Session session=factory.openSession();
		Transaction transaction=session.beginTransaction();
		try{
		userId=(Integer) session.save(user);
		transaction.commit();
		}catch(HibernateException e){
			e.printStackTrace();
			transaction.rollback();
		}finally{
			session.close();
		}
		return userId;
	}

	public boolean updateUser(User user) {
		// TODO Auto-generated method stub
		Session session=factory.openSession();
		Transaction transaction=session.beginTransaction();
		try{
		session.update(user);
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

	public boolean deleteUser(User user) {
		int userId=0;
		Session session=factory.openSession();
		Transaction transaction=session.beginTransaction();
		try{
		session.delete(user);
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

	public User getUserById(int id) {
		Session session =factory.openSession();
		User user=session.get(User.class,id);
		session.close();
		return user;
	}


	public User getUserByEmail(String email) {
		User user=null;
		Session session=null;
		try{
		session=factory.openSession();
		TypedQuery<User> query=session.createQuery("From User where email=:email");
		query.setParameter("email", email);
		List emails=query.getResultList();
		if(!emails.isEmpty())
		user=query.getResultList().get(0);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		return user;
	}

	public int addLabel(Label label) {
		
		int labelId=0;
		Session session=factory.openSession();
		Transaction transaction=session.beginTransaction();
		try{
		labelId=(Integer) session.save(label);
		transaction.commit();
		}catch(HibernateException e){
			e.printStackTrace();
			transaction.rollback();
		}finally{
			session.close();
		}
		return labelId;
	}
	
	public boolean updateLable(Label label) {
		
		boolean status=false;
		Session session=factory.openSession();
		Transaction transaction=session.beginTransaction();
		try{
		 session.update(label);
		 transaction.commit();
		 status=true;
		}catch(HibernateException e){
			e.printStackTrace();
			transaction.rollback();
		}finally{
			session.close();
		}
		return status;
	}
	
    public boolean deleteLable(Label label) {
		
		boolean status=false;
		Session session=factory.openSession();
		Transaction transaction=session.beginTransaction();
		try{
		 session.delete(label);
		 transaction.commit();
		 status=true;
		}catch(HibernateException e){
			e.printStackTrace();
			transaction.rollback();
		}finally{
			session.close();
		}
		return status;
	}

	public Set<Label> getAllLabels(int userId) {
		Session session=factory.openSession();
		User user=session.get(User.class,userId);
		Set<Label> label=user.getLabels();
		System.out.println(label);
		session.close();
		return label;
	}
    
    

}
