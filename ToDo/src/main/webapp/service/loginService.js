var toDo = angular.module('ToDo');

toDo.factory('loginService', function($http, $location) {

	var details = {};
	
	details.loginUser = function(user) {
		console.log(user.password);
		return $http({
			method : "POST",
			url : 'Login',
			data : user
		})
	}

	details.forgetPassword = function(user) {
		console.log(user.password);
		return $http({
			method : "POST",
			url : 'forgetpassword',
			data : user
		})
	}
	details.resetpassword= function(user,path) {
		console.log(user.password);
		return $http({
			method : "POST",
			url : path,
			data : user
		})
	}
	return details;
	

});