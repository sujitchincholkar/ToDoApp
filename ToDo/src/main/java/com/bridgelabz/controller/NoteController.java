package com.bridgelabz.controller;

import java.util.Date;
import java.util.Iterator;
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
	public ResponseEntity deleteNote(@PathVariable("id") int id, HttpSession session, HttpServletRequest request) {
		// User user=(User) session.getAttribute(session.getId());
		String token = request.getHeader("Authorization");
		User user = userService.getUserById(tokenService.verifyToken(token));

		if (user != null) {
			Note note = noteService.getNoteById(id);

			if (note.getUser().getUserId() == user.getUserId()) {

				if (noteService.deleteNote(note)) {
					return ResponseEntity.ok("Note deleted");
				} else {
					return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Failed");

				}
			}
			return ResponseEntity.ok("Note is not Present");

		} else {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("User is not logged in");
		}
	}

	/**This method  update the note and save it to database
	 * @param note
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/updateNote", method = RequestMethod.POST)
	public ResponseEntity updateNote(@RequestBody Note note, HttpSession session, HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		User user = userService.getUserById(tokenService.verifyToken(token));
		Note oldNote = noteService.getNoteById(note.getNoteId());
		if (user != null) {

			Date date = new Date();
			note.setLastUpdated(date);

			if (oldNote.getUser().getUserId() == user.getUserId()) {
				note.setUser(user);

				if (noteService.updateNote(note)) {
					return ResponseEntity.ok("Note Updated");
				} else {
					return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Failed");
				}
			} else {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Note doesnt exist");
			}

		} else {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("User is not logged in");
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
			return ResponseEntity.ok(notes);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
		}
	}
}
