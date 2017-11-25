var toDo = angular.module('ToDo');
toDo.controller('homeController', function($scope, $uibModal, $state,
		noteService, $location) {
	
	if($state.current.name=="home"){
		$scope.navColor= "#ffbb33";
		$scope.navBrand="Google Keep";
	}
	else if($state.current.name=="trash"){
		$scope.navBrand="Trash";
		$scope.navColor="#636363"
	}
	else if($state.current.name=="archive"){
		$scope.navColor= "#607D8B";
		$scope.navBrand="Archive";
	}
	
	$scope.showSidebar = function() {
		if ($scope.width == '0px') {
			$scope.width = '230px';
			$scope.mleft = "200px";
		} else {
			$scope.width = '0px';
			$scope.mleft = "0px";
		}
	}
	
	var colors=[{'color':'#FFFFFF','tooltip':'White'},{'color':'#F8BBD0','tooltip':'Pink'},
				{'color':'#DC94F7','tooltip':'purple'},{'color':'#82B1FF','tooltip':'Dark blue'},
				{'color':'#80D8FF','tooltip':'Blue'},{'color':'#CCFF90','tooltip':'Green'},
				{'color':'#FF8A80','tooltip':'Red'},{'color':'#D5DBDB','tooltip':'Grey'},
				{'color':'#FFD180','tooltip':'Orange'},{'color':'#F5F518','tooltip':'Yellow'},
				{'color':'#D7C9C8','tooltip':'Brown'},{'color':'#A7FFEB','tooltip':'Teal'}];
	$scope.colors=colors;
	
	
	var getNotes = function() {
		var token = localStorage.getItem('token');
		console.log(token);
		var url = 'getAllNotes';
		var notes = noteService.service(url,'GET',token);
		console.log(notes);
		notes.then(function(response) {
		
			$scope.notes = response.data;
		}, function(response) {
			
			$scope.error = response.data.message;
		});
		console.log($scope.pinnedNotes);

	}
	
	$scope.deleteNotePermanently = function(note) {

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
	
	$scope.deleteNote = function(note) {

		var token = localStorage.getItem('token');
		console.log(note);
		note.trashed=true;
		var url='updateNote';
		var notes = noteService.service(url,'POST',token,note);
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
		
		var noteTitle = angular.element(document.querySelector('#note-body'));
		
		if($scope.newNote.title!='' || $scope.newNote.body!=''){
		var url='addNote';
		var notes = noteService.service(url,'POST',token,$scope.newNote);
		
		noteTitle.empty();
		noteBody.empty();
		notes.then(function(response) {
			$scope.newNote.title="";
			$scope.newNote.body="";
			getNotes();

		}, function(response) {
			$scope.newNote.title="";
			$scope.newNote.body="";
			getNotes();

			$scope.error = response.data.message;

		});
		}
	}

	$scope.showModal = function(note) {
		$scope.note = note;
		modalInstance = $uibModal.open({
			templateUrl : 'template/EditNote.html',
			scope : $scope,
			size : 'md'
		});
	};
	
	$scope.pinned = function(note,pinned) {
		note.pinned=pinned;
		update(note);
		
	};
	
	$scope.doArchived=function(note,archived,pinned){
		note.archived=archived;
		note.pinned=pinned;
		update(note);
	}
	
	var  update=function(note){
		var token = localStorage.getItem('token');
		var url="updateNote";
		var notes = noteService.service(url,'POST',token,note);
		
	}
	
	
	
	$scope.updateNote = function(note) {
		var token = localStorage.getItem('token');
		var url="updateNote";
		var notes = noteService.service(url,'POST',token,note);
		
		modalInstance.close('resetModel');
		notes.then(function(response) {
		
		//	getNotes();
			

		}, function(response) {
		
		//	getNotes();

			$scope.error = response.data.message;

		});
	}
	
	$scope.restore=function(note){
		var token = localStorage.getItem('token');
		console.log(note);
		note.trashed=false;
		var url='updateNote';
		var notes = noteService.service(url,'POST',token,note);
		notes.then(function(response) {

			getNotes();

		}, function(response) {

			getNotes();

			$scope.error = response.data.message;

		});
	}
	
	$scope.changeColor=function(note,color){
		note.color=color;
		$scope.color=color;
		update(note);
		
	}
	
	$scope.newnote = false;
	
	$scope.show = function() {
		$scope.newnote = true;
	}

	$scope.hide = function() {
		$scope.newnote = false;
	}
	var getUser=function(){
		var token = localStorage.getItem('token');
		var url='getuser';
		var user = noteService.service(url,'Get',token);
	
		user.then(function(response) {
			var User=response.data;
			if(User.profileUrl==null){
				User.profileUrl="images/default-Profile.png";
				$scope.user=User
			}
			$scope.user=User;
			
		}, function(response) {

		});
		
	}
	$scope.signout=function(){
		
		localStorage.removeItem('token');
		$location.path("/login");
	}
	
	$scope.makeCopy=function(note){
		note.noteId=null;
		var token = localStorage.getItem('token');
		var url='addNote';
		var notes = noteService.service(url,'POST',token,note);
		notes.then(function(response) {
			
			getNotes();

		}, function(response) {
	
			getNotes();

			$scope.error = response.data.message;

		});
	}
	
	getNotes();
	getUser();
	
});
