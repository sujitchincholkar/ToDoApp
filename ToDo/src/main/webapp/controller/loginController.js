var toDo = angular.module('ToDo');
toDo.controller('loginController', function($scope, loginService,$location){
	$scope.loginUser = function(){
		var message=loginService.loginUser($scope.user,$scope.error);
		message.then(function(response) {
				console.log(response.data);
				localStorage.setItem('token',response.headers('Authorization'));
				$location.path('/home');
			},function(response){
				$scope.error=response.data.message;
				console.log(response.data);
			});
	}
});