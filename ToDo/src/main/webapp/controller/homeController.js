var toDo = angular.module('ToDo');
toDo.controller('homeController', function($scope, loginService,$location){
	$scope.addNote = function(){
		var message=loginService.loginUser($scope.note,$scope.error);
		message.then(function(response) {
				console.log(response.data);
			},function(response){
				$scope.error=response.data.message;
			});
	}
	$scope.showSidebar=function(){
		
		$scope.width="200px";
	}
});
