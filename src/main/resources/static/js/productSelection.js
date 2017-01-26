function updateBasket() {
	removeBasketChildren();
	createBasketChildren();
}

function removeBasketChildren() {
	$("#basketDiv").empty();
}

function createBasketChildren() {
	var inputElements = document.getElementsByTagName("input");
	
	for (var i=0; i<inputElements.length; i++) {
		var element = inputElements[i];
		
		if (element.type == "checkbox" && element.checked) {
		    var value = $("<p></p>").text(" - " + element.name);
		    $("#basketDiv").append(value);
		}
	}
}