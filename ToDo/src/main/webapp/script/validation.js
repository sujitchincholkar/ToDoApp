$(document).ready(function() {
	
	$("#email").blur(function() {
		emailValidate();
	});
	
	$("#no").blur(function() {
		mobilevalidate();
	});
	$("#name").blur(function() {
		nameValidate();
	});
	$("#pwd").blur(function() {
		passwordValidate();
	});

	function emailValidate(){
		var valid=true;
		$("#emailerr").text("");
		var emailValidation = /^\w+@[a-zA-Z_]+?\.[a-zA-Z]{2,3}$/;
		if (!emailValidation.test($("#email").val())) {
			$("#emailerr").text("Invalid email");
			valid=false;
		}
		return valid;
	}
	function mobilevalidate(){
		var valid=true;
		$("#noerr").text("");
		var mobileValidation = /^((\+)?(\d{2}[-]))?(\d{10})$/;
		if (!mobileValidation.test($("#no").val())) {
			$("#noerr").text("Invalid Mobile number");
			valid=false;
		}
		return valid;
	}
	function nameValidate(){
		var valid=true;
		$("#nameerr").text("");
		var username = /^[a-zA-Z]{3,}?(\d{0,})$/;
		if (!username.test($("#name").val())) {
			$("#nameerr").text("Invalid Name");
			valid=false;
		}
		return valid;
	}
	function passwordValidate(){
		var valid=true;
		$("#pwderr").text("");
		if($("#pwd").val().length<8){
			$("#pwderr").text("Password must contain 8 characters");
			valid=false;
		}
		return valid;
	}
	function formValidate(){
		var isValid=true;
		if(!emailValidate()){
			isValid=false;
		}
		
		if(!mobilevalidate()){
			isValid=false;
		}
		if(!nameValidate()){
			isValid=false;
		}
		if(!passwordValidate()){
			isValid=false;
		}
		//console.log(isValid);
		return isValid;
	}
	$("#submit").click(function(event){
		if(formValidate()){
			//console.log("submitted");
			return;
		}else{
			event.preventDefault();
		}
		
	});

});