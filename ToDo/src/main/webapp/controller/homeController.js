var toDo = angular.module('ToDo');
toDo.controller('homeController',
		function($scope, $uibModal, $state, noteService,toastr,$interval, $location,$filter,$sanitize) {
				console.log($sanitize("<div>sanitise</div>"));
	
			if ($state.current.name == "home") {
				$scope.navColor = "#ffbb33";
				$scope.navBrand = "Google Keep";
			} else if ($state.current.name == "trash") {
				$scope.navBrand = "Trash";
				$scope.navColor = "#636363"
			} else if ($state.current.name == "archive") {
				$scope.navColor = "#607D8B";
				$scope.navBrand = "Archive";
			}else if($state.current.name =="search"){
				$("#searchbox").focus();
				
				$scope.navColor = "#3E50B4";
				$scope.navBrand = "Google Keep";
			}else{
				
				$scope.currentlabel=$location.path().substr(1) ;
				$scope.navColor = "#607D8B";
				$scope.navBrand =$scope.currentlabel ;
			}

			$scope.showSidebar = function(toggleMain) {
				if ($scope.width == '0px') {
					$scope.width = '230px';
					if(toggleMain==true){
					$scope.mleft = "200px";
					}
				} else {
					$scope.width = '0px';
					$scope.mleft = "0px";
				}
			}
			
			
			var colors = [ 
			{
				'color' : '#FFFFFF',
				'tooltip' : 'White'
			}, 
			{
				'color' : '#F8BBD0',
				'tooltip' : 'Pink'
			}, 
			{
				'color' : '#DC94F7',
				'tooltip' : 'purple'
			}, 
			{
				'color' : '#82B1FF',
				'tooltip' : 'Dark blue'
			}, 
			{
				'color' : '#80D8FF',
				'tooltip' : 'Blue'
			},
			{
				'color' : '#CCFF90',
				'tooltip' : 'Green'
			}, 
			{
				'color' : '#FF8A80',
				'tooltip' : 'Red'
			}, 
			{
				'color' : '#D5DBDB',
				'tooltip' : 'Grey'
			}, 
			{
				'color' : '#FFD180',
				'tooltip' : 'Orange'
			}, 
			{
				'color' : '#F5F518',
				'tooltip' : 'Yellow'
			},
			{
				'color' : '#D7C9C8',
				'tooltip' : 'Brown'
			}, 
			{
				'color' : '#A7FFEB',
				'tooltip' : 'Teal'
			} ];
			$scope.colors = colors;

			var getNotes = function() {
				var url = 'getAllNotes';
				var notes = noteService.service(url, 'GET');
				notes.then(function(response) {

					$scope.notes = response.data;
					
				}, function(response) {

					$scope.error = response.data.message;
				});
			}

			$scope.deleteNotePermanently = function(note) {

				var url = 'deletenote/' + note.noteId;
				var notes = noteService.service(url, 'GET', note);
				notes.then(function(response) {

					getNotes();

				}, function(response) {

					getNotes();

					$scope.error = response.data.message;

				});
			}

			$scope.deleteNote = function(note) {

				note.trashed = true;
				var url = 'updateNote';
				var notes = noteService.service(url, 'POST', note);
				notes.then(function(response) {

					getNotes();

				}, function(response) {

					getNotes();

					$scope.error = response.data.message;

				});
			}

			$scope.createNote = function() {

				
				var noteBody = angular.element(document
						.querySelector('#note-title'));

				var noteTitle = angular.element(document
						.querySelector('#note-body'));

				if ($scope.newNote.title != '' || $scope.newNote.body != '' || $scope.newNote.image!='') {
					var url = 'addNote';
					var notes = noteService.service(url, 'POST', 
							$scope.newNote);

					noteTitle.empty();
					noteBody.empty();
					notes.then(function(response) {
						$scope.newNote="";
						$scope.newNote.title = "";
						$scope.newNote.body = "";
						$scope.newNote.image = "";
						getNotes();

					}, function(response) {
						$scope.newNote.title = "";
						$scope.newNote.body = "";
						getNotes();

						$scope.error = response.data.message;

					});
				}
			}

			$scope.showModal = function(note) {
				$scope.note = note;
				modalInstance = $uibModal.open({
					templateUrl : 'template/EditNote.html',
					scope : $scope,
					size : 'md'
				});
			};
			/*-----------------Collaborator------------------------------*/

			$scope.openCollboarate = function(note, user, index) {
				$scope.note = note;
				$scope.user = user;
				$scope.indexOfNote = index;
				modalInstance = $uibModal.open({
					templateUrl : 'template/Collborate.html',
					scope : $scope,

				});
			}
			var collborators = [];
			$scope.getUserlist = function(note, user, index) {
				var obj = {};
				obj.note = note;
				obj.ownerId = user;
				obj.shareWithId = {};

				var url = 'collaborate';
				
				var users = noteService.service(url, 'POST', obj);
				users.then(function(response) {

					$scope.users = response.data;
					note.collabratorUsers = response.data;

				}, function(response) {
					$scope.users = {};
					collborators = response.data;

				});
				
				return collborators;
			}

			/*
			 * var collborate=function(obj){ var url='collaborate'; var token =
			 * localStorage.getItem('token'); var users =
			 * noteService.service(url,'POST',token,obj); var collborators={};
			 * users.then(function(response) {
			 * 
			 * console.log("Inside collborator"); console.log(response.data);
			 * collborators= response.data; }, function(response) {
			 * $scope.users={}; collborators= response.data;
			 * 
			 * }); return collborators; }
			 */

			$scope.collborate = function(note, user) {
				var obj = {};
				obj.note = note;
				obj.ownerId = user;
				obj.shareWithId = $scope.shareWith;

				var url = 'collaborate';
				var users = noteService.service(url, 'POST', obj);
				users.then(function(response) {

					$scope.users = response.data;
					$scope.note.collabratorUsers = response.data;

				}, function(response) {
					$scope.users = {};

				});
			

			}

			$scope.getOwner = function(note) {
				var url = 'getOwner';
				
				var users = noteService.service(url, 'POST', note);
				users.then(function(response) {
					$scope.owner = response.data;
					note.owner=response.data;
					getUsers();
				}, function(response) {

					$scope.users = {};
				});
			}

			$scope.removeCollborator = function(note, user, index) {
				var obj = {};
				var url = 'removeCollborator';
				obj.note = note;
				obj.ownerId = {
					'email' : ''
				};
				obj.shareWithId = user;
				
				var users = noteService.service(url, 'POST', obj);
				users.then(function(response) {
					getNotes();
					$scope.getUserList(note, $scope.owner);
					

				}, function(response) {
					console.log(response.data);

				});
			}
			
			var getUsers=function(){
				var url = "getUserList";
				var users = noteService.service(url, 'GET');
				users.then(function(response) {
					$scope.userList=response.data;
				}, function(response) {

				});
			}
			
			/*-----------------Collaborator End-----------------------*/
			$scope.pinned = function(note, pinned) {
				note.pinned = pinned;
				update(note);

			};
			$scope.togglePin = function(note) {
				if(note.pinned==false || note.pinned==null){
					note.pinned=true;
					update(note);
				} else{
					note.pinned=false;
					update(note);
				}

			};
			
			$scope.toggleArchive = function(note,pinned) {
				if(note.archived==false || note.archived==null){
					note.archived=true;
					note.pinned=pinned;
					update(note);
				} else{
					note.archived=false;
					note.pinned=pinned;
					update(note);
				}

			};
			$scope.doArchived = function(note, archived, pinned) {
				note.archived = archived;
				note.pinned = pinned;
				update(note);
			}

			var update = function(note) {
				
				var url = "updateNote";
				var notes = noteService.service(url, 'POST', note);
			

			}

			$scope.updateNote = function(note) {
				
				var url = "updateNote";
				var notes = noteService.service(url, 'POST', note);

				modalInstance.close('resetModel');
				notes.then(function(response) {

					// getNotes();

				}, function(response) {

					// getNotes();

					$scope.error = response.data.message;

				});
			}

			$scope.restore = function(note) {
				
				note.trashed = false;
				var url = 'updateNote';
				var notes = noteService.service(url, 'POST', note);
				notes.then(function(response) {

					getNotes();

				}, function(response) {

					getNotes();

					$scope.error = response.data.message;

				});
			}

			$scope.changeColor = function(note, color) {
				note.color = color;
				$scope.color = color;
				update(note);

			}

			$scope.newnote = false;

			$scope.show = function() {
				$scope.newnote = true;
			}

			$scope.hide = function() {
				$scope.newnote = false;
			}
			
			var getUser = function() {
			
				var url = 'getuser';
				var user = noteService.service(url, 'Get');

				user.then(function(response) {
					var User = response.data;
					if (User.profileUrl == null) {
						User.profileUrl = "images/default-Profile.png";
						$scope.user = User
					}
					$scope.user = User;

				}, function(response) {

				});

			}
			$scope.signout = function() {

				localStorage.removeItem('token');
				$location.path("/login");
			}

			$scope.makeCopy = function(note) {
				note.noteId = null;
				
				var url = 'addNote';
				var notes = noteService.service(url, 'POST', note);
				notes.then(function(response) {

					getNotes();

				}, function(response) {

					getNotes();

					$scope.error = response.data.message;

				});
			}

			$scope.AddReminder = '';
			$scope.openAddReminder = function() {
				$('#datepicker').datetimepicker();
				$scope.AddReminder = $('#datepicker').val();
			}

			$scope.socialShare = function(note) {
				FB.init({
					appId : '438526916544857',
					status : true,
					cookie : true,
					xfbml : true,
					version : 'v2.4'
				});

				FB.ui({
					method : 'share_open_graph',
					action_type : 'og.likes',
					action_properties : JSON.stringify({
						object : {
							'og:title' :$sanitize( note.title),
							'og:description' : $sanitize(note.body)
						}
					})
				}, function(response) {
					if (response && !response.error_message) {
						toastr.success('Note shared', 'successfully');
					} else {
						toastr.success('Note share', 'Error');
					}
				});
			};
			
			$scope.viewImage=localStorage.getItem('view');
			if($scope.viewImage=="images/grid.png"){
				$scope.viewcol="col-md-12 col-sm-12 col-xs-12 col-lg-12 list";
			}else{
				
				$scope.viewcol="col-md-3 col-sm-5 col-xs-12 col-lg-3  grid";
			}
			
		
			$scope.changeview=function(){
				if($scope.viewImage=="images/list.png"){
				$scope.viewImage="images/grid.png";
				localStorage.setItem('view',"images/grid.png");
				$scope.viewcol="col-md-10 col-sm-5 col-xs-12 col-lg-10 list";
			}else{
				$scope.viewImage="images/list.png";
				localStorage.setItem('view',"images/list.png");
				$scope.viewcol="col-md-3 col-sm-5 col-xs-12 col-lg-3  grid";
			}
			}
			/*////////////////////////------------Reminder---------//////////////////////////////////*/			
			$scope.reminder = "";
			$scope.openReminder = function(note) {
				$('.reminder').datetimepicker();
				var id = '#datepicker' + note.noteId;
				$scope.reminder = $(id).val();
				// note.reminderStatus=$scope.reminder;
				if ($scope.reminder === "" || $scope.reminder === undefined) {
					
				} else {
					
					note.reminderStatus = $scope.reminder;
					update(note);
					$scope.reminder = "";
				}
			}
			
			$scope.removeReminder=function(note){
			
				note.reminderStatus=null;
				update(note);
			}
			
			function interVal() {
				
					$interval(function(){
						var i=0;
						for(i;i<$scope.notes.length;i++){
							if($scope.notes[i].reminderStatus!='false'){
								
								var currentDate=$filter('date')(new Date(),'MM/dd/yyyy h:mm a');
								
								if($scope.notes[i].reminderStatus === currentDate){
									
									toastr.success($scope.notes[i].title, 'Reminder Deleted');
									$scope.notes[i].reminderStatus=null;
									update($scope.notes[i]);
									
								}
							}
							
						}
					},22000);
			}
			
			
			/*set tomorrows reminder*/
			$scope.tomorrowsReminder=function(notes){
				var currentTime=$filter('date')(new Date().getTime() + 24 * 60 * 60 * 1000,'MM/dd/yyyy');
				notes.reminderStatus=currentTime+" 8:00 AM";
		     	update(notes);
			}
			
			/*set next week reminder*/
			$scope.NextweekReminder=function(notes){
				$scope.currentTime=$filter('date')(new Date().getTime() + 7 * 24 * 60 * 60 * 1000,'MM/dd/yyyy');
				notes.reminderStatus=$scope.currentTime+" 8:00 AM";
				update(notes);
			}
			
			/*set later todays reminder*/
			$scope.todaysReminder=function(notes){
				$scope.currentTime=$filter('date')(new Date(), 'MM/dd/yyyy');
				var currentHour=new Date().getHours();
				if(currentHour >= 7){
					notes.reminderStatus=$scope.currentTime+" 8:00 PM";	
				}
				if(currentHour < 7){
					notes.reminderStatus=$scope.currentTime+" 8:00 AM";
				}
				
				update(notes);
			}
			

			$scope.TodaylaterReminder=true;
			
			/*check weather to display later todays reminder or not*/
			function checktime(){
				var currentDate=new Date().getHours();
				if(currentDate > 19){
					$scope.TodaylaterReminder=false;
				}
				if(currentDate > 1){
					$scope.TodaylaterReminder=true;
				}
			}
			
			/*////////////---Upload Image---/////////////*/
			
			
			$scope.openImageUploader = function(type,typeOfImage) {
				$scope.type = type;
				$scope.typeOfImage=typeOfImage;
				$('#image').trigger('click');
				return false;
			}
			var openCropper=function(user){
				
			modalInstance = $uibModal.open({
				templateUrl : 'template/ImageUpload.html',
				scope : $scope,
				size : 'md'
			});
			}
			
			$scope.stepsModel = [];

			$scope.imageUpload = function(element){
			    var reader = new FileReader();
			  
			    reader.onload = $scope.imageIsLoaded;
			    reader.readAsDataURL(element.files[0]);
			   
			}
		
			$scope.imageIsLoaded = function(e){
			    $scope.$apply(function() {
			        $scope.stepsModel.push(e.target.result);
			        
			        var imageSrc=e.target.result;
			        
			        if($scope.typeOfImage=='user'){
			        	
			        	$scope.imageSrc=imageSrc;
			        	openCropper($scope.user);	
			        	
			        }else if($scope.typeOfImage=='newNote'){
			        $scope.type.image=imageSrc;
			        
			       }
			        else{
			        	   $scope.type.image=imageSrc;
					        
					        update($scope.type);
			        }
			    });
			};
			
			$scope.updatePic=function(){
				$scope.user.profileUrl=$scope.profile;
				updateUser($scope.user);
				modalInstance.close();
			}
			
			var updateUser=function(user){
				var url = 'changeprofilePic';
				var notes = noteService.service(url, 'POST', user);
				notes.then(function(response) {

					getUser();

				}, function(response) {

					getUser();
					$scope.error = response.data.message;

				});
				
			}
			
			
/*			$scope.$on("fileProgress", function(e, progress) {
				$scope.progress = progress.loaded / progress.total;
			});*/
			
			$scope.type = {};
			$scope.type.image = ''; 
			
			$scope.removeImage=function(note){
				note.image=null;
				update(note);
				getNotes();
			}
			
			/*/////////////////----Label----//////////////////////*/
			$scope.addlabel=function(){
				var url = 'addLabelInUser';
				var addlabel = noteService.service(url, 'POST', $scope.newLabel);
				addlabel.then(function(response) {
					 $scope.newLabel.labelName="";
					 getUser();
				},function(response){
					
				});
			}
			$scope.addLabelmodal=function(){

				modalInstance = $uibModal.open({
					templateUrl : 'template/LabelModal.html',
					scope : $scope,
					size:"sm"

				});
			}
			
			$scope.deleteLabel=function(label){
				var url = 'deleteLabel';
				var addlabel = noteService.service(url, 'POST', label);
				addlabel.then(function(response) {
					getUser();
				},function(response){
					
				});
			}
			
			$scope.updatelabel=function(label){
				var url = 'updateLabel';
				var addlabel = noteService.service(url, 'POST', label);
				addlabel.then(function(response) {
					
				},function(response){
					
				});
			}
			
			$scope.toggleLabelOfNote = function(note, label) {
				var index = -1;
				var i = 0;
				for (i = 0, len = note.labels.length; i < len; i++) {
					if (note.labels[i].labelName === label.labelName) {
						index = i;
						break;
					}
				}

				if (index == -1) {
					note.labels.push(label);
					update(note);
				} else {
					note.labels.splice(index, 1);
					update(note);
				}
			}
			
			$scope.checked = function(note, label) {
				var checkedLabels = note.labels;
				for (var labelNo = 0; labelNo < checkedLabels.length; labelNo++) {
					if (checkedLabels[labelNo].labelName == label.labelName)
						return true;
				}
				return false;
			}
			
			
			
			/*//////////////////----Link preview--------/////////////////*/
			var urls=[];
			 $scope.checkUrl=function(note){
				
				var urlPattern=/(http|ftp|https):\/\/[\w-]+(\.[\w-]+)+([\w.,@?^=%&amp;:\/~+#-]*[\w@?^=%&amp;\/~+#-])?/gi;
				var url=note.body.match(urlPattern);
				var link=[];
				
				if(note.size==undefined){
					note.size=0;
					note.url=[];
					note.link=[];
				}
			
				if((url!=null || url!=undefined) && note.size<url.length){
					for(var i=0;i<url.length;i++){
						
						note.url[i]=url[i];
						var addlabel = noteService.getUrl(url[i]);
						addlabel.then(function(response) {
							
							
							var responseData=response.data;
							console.log(response.data);
							if(responseData.title.length>35){
								responseData.title=responseData.title.substr(0,35)+'...';
							}
							link[note.size]={
									title:responseData.title,
									url:note.url[note.size],
									imageUrl:responseData.imageUrl,
									domain:responseData.domain
									}
							
						
							note.link[note.size]=link[note.size];
							note.size=note.size+1;
					
					},function(response){
						
					});
					
				}
			}
			 }
			
			
			getNotes();
			getUser();
			interVal();
		

		});
