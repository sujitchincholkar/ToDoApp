<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width,initial-scale=1">
<script
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.6/angular.js"></script>

<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/angular-ui-router/1.0.3/angular-ui-router.min.js"></script>

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
 
<script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.3.8/angular-sanitize.js"></script>
 
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

<link rel="stylesheet" type="text/css" href="css/form.css">
<link rel="stylesheet" type="text/css" href="css/sidenav.css">
<script type="text/javascript" src="script/ToDoApp.js"></script>
<script type="text/javascript" src="script/validation.js"></script>
<script type="text/javascript" src="service/loginService.js"></script>
<script type="text/javascript" src="service/registerService.js"></script>
<script type="text/javascript" src="directives/CustomDirectives.js"></script>
<script type="text/javascript" src="controller/homeController.js"></script>
<script type="text/javascript" src="controller/loginController.js"></script>
<script type="text/javascript" src="controller/registerController.js"></script>
</head>
<body ng-app="ToDo">
<div ui-view></div>

</body>
</html>