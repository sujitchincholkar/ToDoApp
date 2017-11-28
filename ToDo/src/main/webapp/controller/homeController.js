var toDo = angular.module('ToDo');
toDo.controller('homeController',
		function($scope, $uibModal, $state, noteService,toastr,$interval, $location, $interval,$filter) {

			if ($state.current.name == "home") {
				$scope.navColor = "#ffbb33";
				$scope.navBrand = "Google Keep";
			} else if ($state.current.name == "trash") {
				$scope.navBrand = "Trash";
				$scope.navColor = "#636363"
			} else if ($state.current.name == "archive") {
				$scope.navColor = "#607D8B";
				$scope.navBrand = "Archive";
			}

			$scope.showSidebar = function() {
				if ($scope.width == '0px') {
					$scope.width = '230px';
					$scope.mleft = "200px";
				} else {
					$scope.width = '0px';
					$scope.mleft = "0px";
				}
			}

			var colors = [ {
				'color' : '#FFFFFF',
				'tooltip' : 'White'
			}, {
				'color' : '#F8BBD0',
				'tooltip' : 'Pink'
			}, {
				'color' : '#DC94F7',
				'tooltip' : 'purple'
			}, {
				'color' : '#82B1FF',
				'tooltip' : 'Dark blue'
			}, {
				'color' : '#80D8FF',
				'tooltip' : 'Blue'
			}, {
				'color' : '#CCFF90',
				'tooltip' : 'Green'
			}, {
				'color' : '#FF8A80',
				'tooltip' : 'Red'
			}, {
				'color' : '#D5DBDB',
				'tooltip' : 'Grey'
			}, {
				'color' : '#FFD180',
				'tooltip' : 'Orange'
			}, {
				'color' : '#F5F518',
				'tooltip' : 'Yellow'
			}, {
				'color' : '#D7C9C8',
				'tooltip' : 'Brown'
			}, {
				'color' : '#A7FFEB',
				'tooltip' : 'Teal'
			} ];
			$scope.colors = colors;

			var getNotes = function() {
				var token = localStorage.getItem('token');
				console.log(token);
				var url = 'getAllNotes';
				var notes = noteService.service(url, 'GET', token);
				console.log(notes);
				notes.then(function(response) {

					$scope.notes = response.data;
				}, function(response) {

					$scope.error = response.data.message;
				});
				console.log($scope.pinnedNotes);
			}

			$scope.deleteNotePermanently = function(note) {

				var token = localStorage.getItem('token');
				console.log(token);
				var url = 'deletenote/' + note.noteId;
				var notes = noteService.service(url, 'GET', token, note);
				notes.then(function(response) {

					getNotes();

				}, function(response) {

					getNotes();

					$scope.error = response.data.message;

				});
			}

			$scope.deleteNote = function(note) {

				var token = localStorage.getItem('token');
				console.log(note);
				note.trashed = true;
				var url = 'updateNote';
				var notes = noteService.service(url, 'POST', token, note);
				notes.then(function(response) {

					getNotes();

				}, function(response) {

					getNotes();

					$scope.error = response.data.message;

				});
			}

			$scope.createNote = function() {

				var token = localStorage.getItem('token');
				var noteBody = angular.element(document
						.querySelector('#note-title'));

				var noteTitle = angular.element(document
						.querySelector('#note-body'));

				if ($scope.newNote.title != '' || $scope.newNote.body != '') {
					var url = 'addNote';
					var notes = noteService.service(url, 'POST', token,
							$scope.newNote);

					noteTitle.empty();
					noteBody.empty();
					notes.then(function(response) {
						$scope.newNote.title = "";
						$scope.newNote.body = "";
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
				console.log(note);
				obj.note = note;
				obj.ownerId = user;
				obj.shareWithId = {};

				var url = 'collaborate';
				var token = localStorage.getItem('token');
				var users = noteService.service(url, 'POST', token, obj);
				users.then(function(response) {

					console.log("Inside collborator");
					console.log(response.data);
					$scope.users = response.data;
					$scope.notes[index].collabratorUsers = response.data;

				}, function(response) {
					$scope.users = {};
					collborators = response.data;

				});
				console.log("Returned");
				console.log(collborators);
				console.log(users);
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

			$scope.collborate = function(note, user, index) {
				var obj = {};
				console.log(note);
				obj.note = note;
				obj.ownerId = user;
				obj.shareWithId = $scope.shareWith;

				var url = 'collaborate';
				var token = localStorage.getItem('token');
				var users = noteService.service(url, 'POST', token, obj);
				users.then(function(response) {

					console.log("Inside collborator");
					console.log(response.data);
					$scope.users = response.data;
					$scope.notes[index].collabratorUsers = response.data;

				}, function(response) {
					$scope.users = {};

				});
				console.log("Returned");
				console.log(collborators);
				console.log(users);

			}

			$scope.getOwner = function(note) {
				var url = 'getOwner';
				var token = localStorage.getItem('token');
				var users = noteService.service(url, 'POST', token, note);
				users.then(function(response) {

					$scope.owner = response.data;

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
				var token = localStorage.getItem('token');
				var users = noteService.service(url, 'POST', token, obj);
				users.then(function(response) {
					$scope.collborate(note, $scope.owner);

					console.log(response.data);

				}, function(response) {
					console.log(response.data);

				});
			}
			/*-----------------Collaborator End------------------------------*/
			$scope.pinned = function(note, pinned) {
				note.pinned = pinned;
				update(note);

			};

			$scope.doArchived = function(note, archived, pinned) {
				note.archived = archived;
				note.pinned = pinned;
				update(note);
			}

			var update = function(note) {
				var token = localStorage.getItem('token');
				var url = "updateNote";
				var notes = noteService.service(url, 'POST', token, note);

			}

			$scope.updateNote = function(note) {
				var token = localStorage.getItem('token');
				var url = "updateNote";
				var notes = noteService.service(url, 'POST', token, note);

				modalInstance.close('resetModel');
				notes.then(function(response) {

					// getNotes();

				}, function(response) {

					// getNotes();

					$scope.error = response.data.message;

				});
			}

			$scope.restore = function(note) {
				var token = localStorage.getItem('token');
				console.log(note);
				note.trashed = false;
				var url = 'updateNote';
				var notes = noteService.service(url, 'POST', token, note);
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
				var token = localStorage.getItem('token');
				var url = 'getuser';
				var user = noteService.service(url, 'Get', token);

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
				var token = localStorage.getItem('token');
				var url = 'addNote';
				var notes = noteService.service(url, 'POST', token, note);
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
							'og:title' : note.title,
							'og:description' : note.description
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

			$scope.reminder = "";
			$scope.openReminder = function(note) {
				$('.reminder').datetimepicker();
				var id = '#datepicker' + note.noteId;
				$scope.reminder = $(id).val();
				// note.reminderStatus=$scope.reminder;
				if ($scope.reminder === "" || $scope.reminder === undefined) {
					console.log(note);
					console.log($scope.reminder);
				} else {
					console.log($scope.reminder);
					note.reminderStatus = $scope.reminder;
					update(note);
					$scope.reminder = "";
				}
			}
			
			function interVal() {
				
					$interval(function(){
						var i=0;
						for(i;i<$scope.notes.length;i++){
							if($scope.notes[i].reminderStatus!='false'){
								
								var currentDate=$filter('date')(new Date(),'MM/dd/yyyy h:mm a');
								
								if($scope.notes[i].reminderStatus === currentDate){
									
									toastr.success('You have a reminder for a note', 'check it out');
								}
							}
							
						}
					},22000);
			}

			getNotes();
			getUser();
			interVal();

		});
