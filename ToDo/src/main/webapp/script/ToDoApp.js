var ToDo = angular.module('ToDo', ['ui.router','ui.bootstrap','ngFileUpload', 'ngSanitize','toastr','ui.bootstrap.datepicker','base64']);

ToDo.config([ '$stateProvider', '$urlRouterProvider',
		function($stateProvider, $urlRouterProvider) {

			$stateProvider.state('login', {
				url : '/login',
				templateUrl : 'template/Login.html',
				controller : 'loginController'
			});
			
			$stateProvider.state('register', {
				url : '/register',
				templateUrl : 'template/Register.html',
				controller : 'registerController'
			});
			$stateProvider.state('home', {
				url : '/home',
				templateUrl : 'template/home.html',
				controller : 'homeController'
			});
			$stateProvider.state('forgetpassword', {
				url : '/forgetpassword',
				templateUrl : 'template/Forgetpassword.html',
				controller : 'loginController'
			});
			$stateProvider.state('resetpassword', {
				url : '/resetpassword/:token',
				templateUrl : 'template/ResetPassword.html',
				controller : 'loginController'
			});
			$stateProvider.state('trash', {
				url : '/trash',
				templateUrl : 'template/Trash.html',
				controller : 'homeController'
			});
			$stateProvider.state('archive', {
				url : '/archive',
				templateUrl : 'template/Archive.html',
				controller : 'homeController'
			});
			
			$stateProvider.state('dummy', {
				url : '/dummy',
				templateUrl : 'template/Dummypage.html',
				controller : 'dummycontroller'
			});
			
			$stateProvider.state('reminder', {
				url : '/reminder',
				templateUrl : 'template/Reminder.html',
				controller : 'homeController'
			});
			
			$urlRouterProvider.otherwise('login');
			
}]);