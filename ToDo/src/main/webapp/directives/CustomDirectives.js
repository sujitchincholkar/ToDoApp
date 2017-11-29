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


toDo.directive("ngFileSelect", function(fileReader, $timeout) {
	return {
		scope : {
			ngModel : '='
		},
		link : function($scope, el) {
			function getFile(file) {
				fileReader.readAsDataUrl(file, $scope).then(function(result) {
					$timeout(function() {
						$scope.ngModel = result;
					});
				});
			}

			el.bind("change", function(e) {
				var file = (e.srcElement || e.target).files[0];
				getFile(file);
			});
		}
	};
});

toDo.factory("fileReader", function($q, $log) {
	var onLoad = function(reader, deferred, scope) {
		return function() {
			scope.$apply(function() {
				deferred.resolve(reader.result);
			});
		};
	};

	var onError = function(reader, deferred, scope) {
		return function() {
			scope.$apply(function() {
				deferred.reject(reader.result);
			});
		};
	};

	var onProgress = function(reader, scope) {
		return function(event) {
			scope.$broadcast("fileProgress", {
				total : event.total,
				loaded : event.loaded
			});
		};
	};

	var getReader = function(deferred, scope) {
		var reader = new FileReader();
		reader.onload = onLoad(reader, deferred, scope);
		reader.onerror = onError(reader, deferred, scope);
		reader.onprogress = onProgress(reader, scope);
		return reader;
	};

	var readAsDataURL = function(file, scope) {
		var deferred = $q.defer();

		var reader = getReader(deferred, scope);
		reader.readAsDataURL(file);

		return deferred.promise;
	};

	return {
		readAsDataUrl : readAsDataURL
	};
});