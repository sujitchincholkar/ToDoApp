var toDo = angular.module('ToDo');

toDo.factory('noteService', function($http,$location) {
	var notes={};
	notes.getNotes=function(token){
	return $http({
	    method: 'GET',
	    url: 'getAllNotes',
	    headers: {
	        'Authorization': token
	    }
	
	});
	
	}
	return notes;
});