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

toDo.filter('parseUrlFilter', function () {

    var urlPattern = /(http|ftp|https):\/\/[\w-]+(\.[\w-]+)+([\w.,@?^=%&amp;:\/~+#-]*[\w@?^=%&amp;\/~+#-])?/gi;

    return function (text, target) {

        return text.replace(urlPattern, '<a target="' + target + '" href="$&">$&</a>');
        
    };

});