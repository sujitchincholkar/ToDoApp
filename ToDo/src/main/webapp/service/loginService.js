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
	return details;
	

});