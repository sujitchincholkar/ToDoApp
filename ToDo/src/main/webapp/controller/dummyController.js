var toDo = angular.module('ToDo');
toDo.controller('dummycontroller',function($location,dummyservice){
	var dummyservice=dummyservice.service();
	dummyservice.then(function(response){
		localStorage.setItem('token',response.data.message);
		localStorage.setItem('view','images/list.png');
		$location.path('/home');
	},function(response){
		
	});
	dummyservice();
});