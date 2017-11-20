<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width,initial-scale=1">
<script
	src="bower_components/angular/angular.js"></script>

<script type="text/javascript"
	src="bower_components/angular-ui-router/release/angular-ui-router.min.js"></script>

<script
	src="bower_components/jquery/dist/jquery.min.js"></script>

<script
	src="bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
 
<script src="bower_components/angular-sanitize/angular-sanitize.js"></script>
 
<link rel="stylesheet"
	href="bower_components/bootstrap/dist/css/bootstrap.min.css">

<link rel="stylesheet" type="text/css" href="css/loginform.css">
<link rel="stylesheet" type="text/css" href="css/sidenav.css">
<link rel="stylesheet" type="text/css" href="css/homepage.css">

<script type="text/javascript" src="script/ToDoApp.js"></script>
<script type="text/javascript" src="script/validation.js"></script>

<script type="text/javascript" src="service/loginService.js"></script>
<script type="text/javascript" src="service/registerService.js"></script>
<script type="text/javascript" src="service/noteService.js"></script>

<script type="text/javascript" src="directives/CustomDirectives.js"></script>

<script type="text/javascript" src="controller/homeController.js"></script>
<script type="text/javascript" src="controller/loginController.js"></script>
<script type="text/javascript" src="controller/registerController.js"></script>
</head>
<body ng-app="ToDo">
<div ui-view></div>

</body>
</html>