var toDo = angular.module('ToDo');
toDo.controller('registerController', function($scope, loginService,$location){
	$scope.loginUser = function(){
		var a=registerService.registerUser($scope.user,$scope.error);
			a.then(function(response) {
				console.log(response.data);
				//localStorage.setItem('token',response.data.responseMessage)
				$location.path('/login')
			},function(response){
				$scope.error=response.data;
			});
	}
});