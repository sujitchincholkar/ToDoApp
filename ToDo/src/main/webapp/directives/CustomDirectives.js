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
toDo.directive('contenteditable', ['$sce', function($sce) {

	  return {

	  restrict: 'A', 

	  require: '?ngModel', 

	  link: function(scope, element, attrs, ngModel) {

	      if (!ngModel) return; // do nothing if no ng-model


	      ngModel.$render = function() {

	        element.html($sce.getTrustedHtml(ngModel.$viewValue || ''));

	        read(); // initialize

	      };

	      element.on('blur keyup change', function() {

	        scope.$evalAsync(read);

	      });

	      function read() {

	        var html = element.html();

	        if ( attrs.stripBr && html == '<br>' ) {

	          html = '';

	        }
	        if ( attrs.stripDiv && html == '<div>' &&html=='</div>') {

		          html = '';

		        }

	        ngModel.$setViewValue(html);

	      }

	    }

	  };

	}]);