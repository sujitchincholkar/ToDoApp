var toDo = angular.module('ToDo');
toDo.controller('homeController', function($scope, loginService,noteService,$location){
	

	
	$scope.showSidebar=function(){
		
		if($scope.width=='0px'){
			$scope.width='230px';
			$scope.mleft="200px";
		}else{
			$scope.width='0px';
			$scope.mleft="0px";
		}
		
	}

	 var getNotes=function(){
		var token=localStorage.getItem('token');
			console.log(token);
		var notes=noteService.getNotes(token);
		console.log(notes);
		notes.then(function(response) {
			$scope.notes=response.data;
		},function(response){
			$scope.error=response.data.message;
		});
		$scope.notes=notes;
	}
	 $scope.deleteNote = function(note) {

			var token = localStorage.getItem('token');
			
			
			console.log(token);

			var notes = noteService.deleteNote(token,note);

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

			var notes = noteService.addNote(token, $scope.newNote);

			notes.then(function(response) {
				
				getNotes();

			}, function(response) {

				getNotes();

				$scope.error = response.data.message;

			});
		}
	getNotes();
});
