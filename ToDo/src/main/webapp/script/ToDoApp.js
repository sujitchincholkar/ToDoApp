var ToDo = angular.module('ToDo', ['ui.router', 'ngSanitize']);

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
			$urlRouterProvider.otherwise('login');
}]);