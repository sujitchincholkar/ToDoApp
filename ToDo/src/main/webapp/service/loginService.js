var toDo = angular.module('ToDo');

toDo.factory('loginService', function($http, $location) {

	var details = {};
	
	details.loginUser = function(user) {
		return $http({
			method : "POST",
			url : 'Login',
			data : user
		})
	}
	return details;

});