package com.bridgelabz.util;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.bridgelabz.model.Note;
import com.bridgelabz.service.NoteService;

public class DeleteScheduler {
	
	@Autowired
	NoteService noteService;
	
	@Scheduled(fixedDelay=200000)
	public void deleteTrashNote() {
		List<Note> notes=noteService.getNotesInTrash();
		int size=notes.size();
		long sevenDayBefore=System.currentTimeMillis();
		sevenDayBefore=sevenDayBefore-(7*24*60*60*1000);
		Date current=new Date(sevenDayBefore);
		for(int i=0;i<size;i++) {
			if(notes.get(i).getLastUpdated().before(current)) {
				noteService.deleteNote(notes.get(i));
			}
		}
		
	}

}
