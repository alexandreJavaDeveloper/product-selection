function updateBasket() {
	removeBasketChildren();
	var isOneCheckBoxChecked = createBasketChildren();
	
	$('#checkout').prop('disabled', true);
	if (isOneCheckBoxChecked) {
		$('#checkout').prop('disabled', false);
	}
}

function removeBasketChildren() {
	$("#basketDiv").empty();
}

function createBasketChildren() {
	var inputElements = document.getElementsByTagName("input");
	var isOneCheckBoxChecked = false;
	
	for (var i=0; i<inputElements.length; i++) {
		var element = inputElements[i];
		
		if (element.type == "checkbox" && element.checked) {
		    var value = $("<p></p>").text(" - " + element.name);
		    $("#basketDiv").append(value);
		    
		    // input hidden to send via HTTP GET
		    var inputHidden = $("<input name='basketHidden' id='" + element.value + "' type='hidden'></input>");
		    $("#basketDiv").append(inputHidden);
		    
		    isOneCheckBoxChecked = true;
		}
	}
	
	return isOneCheckBoxChecked;
}