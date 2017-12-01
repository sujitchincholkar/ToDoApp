package com.bridgelabz.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.model.Collaborater;
import com.bridgelabz.model.CustomResponse;
import com.bridgelabz.model.Label;
import com.bridgelabz.model.Note;
import com.bridgelabz.model.User;
import com.bridgelabz.service.NoteService;
import com.bridgelabz.service.TokenService;
import com.bridgelabz.service.UserService;

@RestController
public class NoteController {

	@Autowired
	private UserService userService;

	@Autowired
	private NoteService noteService;

	@Autowired
	private TokenService tokenService;

	static Logger logger = Logger.getLogger(NoteController.class);

	/**This method will add notes
	 * @param note
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/addNote", method = RequestMethod.POST)
	public ResponseEntity<String> addNote(@RequestBody Note note, HttpSession session, HttpServletRequest request) {
		// User user=(User) session.getAttribute(session.getId());

		String token = request.getHeader("Authorization");
		System.out.println(token);
		User user = userService.getUserById(tokenService.verifyToken(token));

		if (user != null) {

			Date date = new Date();
			note.setUser(user);
			note.setCreateDate(date);
			note.setLastUpdated(date);

			int id = noteService.saveNote(note);

			if (id > 0) {
				return ResponseEntity.ok("Note added");
			} else {
				return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Failed");
			}

		} else {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("User is not logged in");
		}
	}

	/**This method will delete the note of given id
	 * @param id
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/deletenote/{id}", method = RequestMethod.GET)
	public ResponseEntity<CustomResponse> deleteNote(@PathVariable("id") int id, HttpSession session, HttpServletRequest request) {
		// User user=(User) session.getAttribute(session.getId());
		String token = request.getHeader("Authorization");
		User user = userService.getUserById(tokenService.verifyToken(token));
		CustomResponse response=new CustomResponse();
		
		if (user != null) {
			Note note = noteService.getNoteById(id);

			if (note!=null && note.getUser().getUserId() == user.getUserId()) {

				if (noteService.deleteNote(note)) {
					response.setMessage("Note Deleted");
					return ResponseEntity.ok(response);
				} else {
					response.setMessage("Database problem");
					return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

				}
			}
			response.setMessage("Note is not Present");
			return ResponseEntity.ok(response);

		} else {
			response.setMessage("User is not logged in");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		}
	}

	/**This method  update the note and save it to database
	 * @param note
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/updateNote", method = RequestMethod.POST)
	public ResponseEntity<CustomResponse> updateNote(@RequestBody Note note, HttpSession session, HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		User user = userService.getUserById(tokenService.verifyToken(token));
		
		Note oldNote = noteService.getNoteById(note.getNoteId());
		CustomResponse response=new CustomResponse();
		
		if (user != null) {

			Date date = new Date();
			note.setLastUpdated(date);

			if (oldNote.getUser().getUserId() == user.getUserId()) {
				note.setUser(user);

				if (noteService.updateNote(note)) {
					response.setMessage("Note Updated");
					return ResponseEntity.ok(response);
				} else {
					response.setMessage("Database problem");
					return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
				}
			} else {
				List<User> users=	noteService.getListOfUser(note.getNoteId());
				int i=0;
				int flag=0;
				while(users.size()>i) {
					if(users.get(i).getUserId()==user.getUserId()) {
						flag=1;
					}
					i++;
				}if(flag==1){
					
					note.setUser(oldNote.getUser());
					noteService.updateNote(note);
				}
				response.setMessage("Note updated");
				return ResponseEntity.status(HttpStatus.OK).body(response);
			}

		} else {
			response.setMessage("User is not logged in");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}

	}

	/**This method will return all the notes of user
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getAllNotes", method = RequestMethod.GET)
	public ResponseEntity<Set<Note>> getNotes(HttpSession session, HttpServletRequest request) {

		String token = request.getHeader("Authorization");

		User user = userService.getUserById(tokenService.verifyToken(token));

		if (user != null) {
			Set<Note> notes = noteService.getNotes(user.getUserId());
			Set<Note> collborated =noteService.getCollboratedNotes(user.getUserId());
			notes.addAll(collborated);
			return ResponseEntity.ok(notes);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
		}
	}
	

	
	@RequestMapping(value = "/collaborate", method = RequestMethod.POST)
	public ResponseEntity<List<User>> getNotes(@RequestBody Collaborater collborator, HttpServletRequest request){
		
		List<User> users=new ArrayList<User>();
		Collaborater collaborate =new Collaborater();
		
		Note note= (Note) collborator.getNote();
		User shareWith= (User) collborator.getShareWithId();
		shareWith=userService.getUserByEmail(shareWith.getEmail());
		
		User owner= (User) collborator.getOwnerId();
		
		String token=request.getHeader("Authorization");
		User user=userService.getUserById(tokenService.verifyToken(token));
		
		users=	noteService.getListOfUser(note.getNoteId());
		
		if(user!=null) {
				if(shareWith!=null && shareWith.getUserId()!=owner.getUserId()) {
					int i=0;
					int flag=0;
					while(users.size()>i) {
						if(users.get(i).getUserId()==shareWith.getUserId()) {
							flag=1;
						}
						i++;
					}
					if(flag==0) {
						collaborate.setNote(note);
						collaborate.setOwnerId(owner);
						collaborate.setShareWithId(shareWith);
						if(noteService.saveCollborator(collaborate)>0) {
						  	users.add(shareWith);
						}else {
							 ResponseEntity.ok(users);
						}
					}
		}
		}
		return ResponseEntity.ok(users);
	}
	
	@RequestMapping(value = "/getOwner", method = RequestMethod.POST)
	public ResponseEntity<User> getOwner(@RequestBody Note note, HttpServletRequest request){
		String token=request.getHeader("Authorization");
		User user=userService.getUserById(tokenService.verifyToken(token));
		if(user!=null) {
		Note noteComplete=noteService.getNoteById(note.getNoteId());
		User owner=noteComplete.getUser();
		return ResponseEntity.ok(owner);
		}
		else{
			return ResponseEntity.ok(null);
		}
	}
	
	@RequestMapping(value = "/removeCollborator", method = RequestMethod.POST)
	public ResponseEntity<CustomResponse> removeCollborator(@RequestBody Collaborater collborator, HttpServletRequest request){
		CustomResponse response=new CustomResponse();
		int shareWith=collborator.getShareWithId().getUserId();
		int noteId=collborator.getNote().getNoteId();
		
		Note note=noteService.getNoteById(noteId);
		User owner=note.getUser();
	
		String token=request.getHeader("Authorization");
		User user=userService.getUserById(tokenService.verifyToken(token));
		
		if(user!=null) {
				if(owner.getUserId()!=shareWith){
					if(noteService.removeCollborator(shareWith, noteId)>0){
						response.setMessage("Collborator removed");
						return ResponseEntity.ok(response);
					}else{
						response.setMessage("database problem");
						return ResponseEntity.ok(response);
					}
				}else{
					response.setMessage("Can't remove owner");
					return ResponseEntity.ok(response);
				}
		
	    }else{
	    	response.setMessage("Token expired");
			return ResponseEntity.ok(response);
	    }
	}
	
/*	@RequestMapping(value = "/addNoteLabel", method = RequestMethod.POST)
	public ResponseEntity<CustomResponse> addLabel(@RequestBody Note note, HttpServletRequest request){
		String token =request.getHeader("Authorization");
		User user=userService.getUserById(tokenService.verifyToken(token));
		if(user!=null){
			Note oldNote=noteService.getNoteById(note.getNoteId());
			
			
		}
		return null;
	}
	*/
}
