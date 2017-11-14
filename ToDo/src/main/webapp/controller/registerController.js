var toDo = angular.module('ToDo');
toDo.controller('registerController', function($scope, registerService,$location){
	$scope.registerUser = function(){
		var a=registerService.registerUser($scope.user,$scope.error);
			a.then(function(response) {
				console.log(response.data);
				$location.path('/login')
			},function(response){
				$scope.error=response.data;
			});
	}
});