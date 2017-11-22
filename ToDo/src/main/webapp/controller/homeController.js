var toDo = angular.module('ToDo');
toDo.controller('homeController', function($scope, $uibModal, loginService,
		noteService, $location) {

	$scope.showSidebar = function() {

		if ($scope.width == '0px') {
			$scope.width = '230px';
			$scope.mleft = "200px";
		} else {
			$scope.width = '0px';
			$scope.mleft = "0px";
		}

	}

	var getNotes = function() {
		var token = localStorage.getItem('token');
		console.log(token);
		var url = 'getAllNotes';
		var notes = noteService.service(url,'GET',token,note);
		console.log(notes);
		notes.then(function(response) {
			$scope.notes = response.data;
		}, function(response) {
			$scope.error = response.data.message;
		});
		$scope.notes = notes;
	}
	
	$scope.deleteNote = function(note) {

		var token = localStorage.getItem('token');
		console.log(token);
		var url='deletenote/'+note.noteId;
		var notes = noteService.service(url,'GET',token,note);
		notes.then(function(response) {

			getNotes();

		}, function(response) {

			getNotes();

			$scope.error = response.data.message;

		});
	}
	
	$scope.createNote = function() {

		var token = localStorage.getItem('token');
		var noteBody = angular.element(document.querySelector('#note-title'));
		noteBody.empty();
		var noteTitle = angular.element(document.querySelector('#note-body'));
		noteTitle.empty();

		console.log(token);
		var url='addNote';
		var notes = noteService.service(url,'POST',token,$scope.newNote);
		
		noteTitle.empty();
		noteBody.empty();
		notes.then(function(response) {

			getNotes();

		}, function(response) {

			getNotes();

			$scope.error = response.data.message;

		});
	}

	$scope.showModal = function(note) {
		$scope.note = note;
		modalInstance = $uibModal.open({
			templateUrl : 'template/EditNote.html',
			scope : $scope,
			size : 'md'
		});
	};

	$scope.updateNote = function(note) {
		var token = localStorage.getItem('token');
		var url="updateNote";
		var notes = noteService.service(url,'POST',token,note);
		

		notes.then(function(response) {

			getNotes();

		}, function(response) {

			getNotes();

			$scope.error = response.data.message;

		});
	}
	$scope.newnote = false;
	$scope.show = function() {
		$scope.newnote = true;
	}

	$scope.hide = function() {
		$scope.newnote = false;
	}
	getNotes();
});
