var toDo = angular.module('ToDo');
toDo.controller('homeController', function($scope, loginService,noteService,$location){
	$scope.addNote = function(){
		var message=loginService.loginUser($scope.note,$scope.error);
		message.then(function(response) {
				console.log(response.data);
			},function(response){
				$scope.error=response.data.message;
			});
	}
	$scope.showSidebar=function(){
		
		if($scope.width=='0px'){
			$scope.width='200px';
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
	getNotes();
});
