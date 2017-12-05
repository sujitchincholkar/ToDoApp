var ToDo = angular.module('ToDo', ['ui.router','ui.bootstrap','ngFileUpload', 'ngSanitize','toastr','ui.bootstrap.datepicker','base64','angular-img-cropper']);

ToDo.config([ '$stateProvider', '$urlRouterProvider',
		function($stateProvider, $urlRouterProvider) {

			$stateProvider.state('login', {
				url : '/login',
				templateUrl : 'template/Login.html',
				controller : 'loginController'
			})
			
			.state('register', {
				url : '/register',
				templateUrl : 'template/Register.html',
				controller : 'registerController'
			})
			.state('home', {
				url : '/home',
				templateUrl : 'template/home.html',
				controller : 'homeController'
			})
			.state('forgetpassword', {
				url : '/forgetpassword',
				templateUrl : 'template/Forgetpassword.html',
				controller : 'loginController'
			})
			.state('resetpassword', {
				url : '/resetpassword/:token',
				templateUrl : 'template/ResetPassword.html',
				controller : 'loginController'
			})
			.state('trash', {
				url : '/trash',
				templateUrl : 'template/Trash.html',
				controller : 'homeController'
			})
			.state('archive', {
				url : '/archive',
				templateUrl : 'template/Archive.html',
				controller : 'homeController'
			})
			
			.state('dummy', {
				url : '/dummy',
				templateUrl : 'template/Dummypage.html',
				controller : 'dummycontroller'
			})
			
			.state('reminder', {
				url : '/reminder',
				templateUrl : 'template/Reminder.html',
				controller : 'homeController'
			})
			.state('search', {
				url : '/search',
				templateUrl : 'template/search.html',
				controller : 'homeController'
			})
			.state('labels', {
				url : '/{labelName}',
				templateUrl : 'template/Labels.html',
				controller : 'homeController'
			});
		
			
			$urlRouterProvider.otherwise('login');
			
}]);