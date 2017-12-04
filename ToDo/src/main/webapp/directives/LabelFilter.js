var toDo = angular.module('ToDo');

toDo.filter('labelFilter', function() {
	return function(notes, labelName) {
		var filteredNotes = [];
		
		if (labelName == '') {
			return notes;
		}
		console.log(notes);
		for (var i = 0; i < notes.length; i++) {
			var note = notes[i];
			var lbl = note.labels;
			for (var j = 0; j < lbl.length; j++) {
				if (labelName === lbl[j].labelName) {
					filteredNotes.push(note);
				}
			}
		}

		return filteredNotes;
	}
});