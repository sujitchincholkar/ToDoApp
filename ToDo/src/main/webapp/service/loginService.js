var toDo = angular.module('ToDo');

toDo.factory('loginService', function($http, $location) {

	var details = {};
	
	details.service = function(method,url,user) {
		return $http({
			method : method,
			url : url,
			data : user
		})
	}
	/*details.loginUser = function(user) {
		return $http({
			method : "POST",
			url : 'Login',
			data : user
		})
	}

	details.forgetPassword = function(user) {
		return $http({
			method : "POST",
			url : 'forgetpassword',
			data : user
		})
	}
	details.resetpassword= function(user,path) {
		return $http({
			method : "POST",
			url : path,
			data : user
		})
	}*/
	return details;
	

});