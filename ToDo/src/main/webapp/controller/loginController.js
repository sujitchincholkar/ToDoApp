var toDo = angular.module('ToDo');
toDo.controller('loginController', function($scope, loginService,$location){
	$scope.loginUser = function(){
		var message=loginService.service('POST','Login',$scope.user);
		message.then(function(response) {
				console.log(response.data);
				localStorage.setItem('token',response.headers('Authorization'));
				localStorage.setItem('view','images/list.png');
				$location.path('/home');
			},function(response){
				$scope.error=response.data.message;
				console.log(response.data);
			});
	};
	
	$scope.forgetPassword = function(){
		var message=loginService.service('POST','forgetpassword',$scope.user);
		message.then(function(response) {
				//localStorage.setItem('token',response.headers('Authorization'));
			$scope.error=response.data.message;
		},function(response){
				$scope.error=response.data.message;
		
			});
	};
	
	$scope.resetpassword = function(){
		var path=$location.path();
		path=path.replace(path.charAt(0),'');
		if($scope.password==$scope.user.password){
		var message=loginService.service('POST',path,$scope.user);
		message.then(function(response) {
				//localStorage.setItem('token',response.headers('Authorization'));
			$scope.error=response.data.message;
			$location.path('/login');
		},function(response){
				$scope.error=response.data.message;
			});
		}else{
			$scope.error='Password does not match';
		}
	}
	
});