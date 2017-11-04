package com.bridgelabz.controller;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.model.Note;
import com.bridgelabz.model.User;
import com.bridgelabz.service.NoteService;

@RestController
public class NoteController {
	@Autowired
	NoteService noteService;
	@RequestMapping(value="/addNote",method=RequestMethod.POST)
	public ResponseEntity addNote(@RequestBody Note note,HttpSession session){
		User user=(User) session.getAttribute(session.getId());
		if(user!=null){
			Date date=new Date();
			note.setUser(user);
			note.setCreateDate(date);
			note.setLastUpdated(date);
			int id=noteService.saveNote(note);
			if(id>0){
				 	return ResponseEntity.ok("Note added");
			}else{
				return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Failed");
			}
		}else{
			return ResponseEntity.status(HttpStatus.CONFLICT).body("User is not logged in");
		}
	}
	@RequestMapping(value="/deletenote/{id}",method=RequestMethod.GET)
	public ResponseEntity deleteNote(@PathVariable("id") int id,HttpSession session){
		User user=(User) session.getAttribute(session.getId());
		if(user!=null){
			Set<Note> notes=user.getNotes();
			Iterator itrate=notes.iterator();
			while(itrate.hasNext()){
				
				Note note =(Note) itrate.next();
				if(note.getNoteId()==id){
					if(noteService.deleteNote(note)){
					 	return ResponseEntity.ok("Note deleted");
				}else{
					return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Failed");
				}
			}
		}
			return ResponseEntity.ok("Note is not Present");
		}else{
			return ResponseEntity.status(HttpStatus.CONFLICT).body("User is not logged in");
		}
	}
	@RequestMapping(value="/updateNote",method=RequestMethod.POST)
	public ResponseEntity updateNote(@RequestBody Note note,HttpSession session ){
			User user=(User) session.getAttribute(session.getId());
			if(user!=null){
				Date date=new Date();
				note.setLastUpdated(date);
				note.setUser(user);
				if(noteService.updateNote(note)){
					return ResponseEntity.ok("Note Updated");
				}else{
					return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Failed");
				}
			}else{
				return ResponseEntity.status(HttpStatus.CONFLICT).body("User is not logged in");
			}
			
	}
	@RequestMapping(value="/getAllNotes",method=RequestMethod.GET)
	public ResponseEntity<Set<Note>> getNotes(HttpSession  session){
		User user=(User) session.getAttribute(session.getId());
		if(user!=null){
		
		Set<Note> notes=user.getNotes();
		return ResponseEntity.ok(notes);
		}else{
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
		}
	}
}
