var toDo = angular.module('ToDo');
toDo.directive('topNavBar', function() {
		return{
			templateUrl : 'template/TopNavBar.html'
		}

	
});
toDo.directive('sideNavBar', function() {
	return{
		templateUrl : 'template/SideNavBar.html'
	}
});