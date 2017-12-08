var toDo = angular.module('ToDo');

toDo.factory('noteService', function($http,$location) {
	var notes={};
	notes.service=function(url,method,note){
		return $http({
		    method: method,
		    url: url,
		    data:note,
		    headers: {
		        'Authorization': localStorage.getItem('token')
		    }
		
		});
	}
	
	notes.getUrl=function(urlToSend){
		return $http({
		    method: 'POST',
		    url: 'geturl',
		    headers: {
		        'url': urlToSend
		    }
		
		});
	}
	
/*	notes.getNotes=function(token){
	return $http({
	    method: 'GET',
	    url: 'getAllNotes',
	    headers: {
	        'Authorization': token
	    }
	
	});
	
	}
	notes.addNote=function(token,note){

		console.log(note);

		return $http({

		    method: 'POST',

		    url: 'addNote',

		    data:note,

		    headers: {

		        'Authorization': token

		    }
		});
	}
	notes.deleteNote=function(token,note){

		console.log(note);

		return $http({

		    method: 'GET',

		    url: 'deletenote/'+note.noteId,

		    data:note,

		    headers: {

		        'Authorization': token

		    }
		});
	}
	notes.updateNote=function(token,note){

		console.log(note);

		return $http({

		    method: 'POST',

		    url: 'updateNote',

		    data:note,

		    headers: {

		        'Authorization': token

		    }
		});
	}*/
	return notes;
});