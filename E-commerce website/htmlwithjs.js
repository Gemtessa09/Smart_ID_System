
function arrayFunction() {
	var grade = new Array("70", "60", "80", "90", "20");
	document.write("<br> Popped: " + grade.pop());

	document.write("<br> After poping:");
	for(var i=0;i<grade.length; i++)
	        document.write(" " + grade[i]);

	grade.reverse();
	document.write("<br>Reversed: ");
	for(var i=0;i<grade.length; i++)
	        document.write(" " + grade[i]);
 
	document.write("<br>Search 80: " + grade.indexOf("80"));
	document.write("<br>Converted to string: " + grade.toString());
    
    	var slicedValue = grade.slice(2);
	document.write("<br>Sliced: " + slicedValue);
	document.write("<br>Spliced: " + grade.splice(1));
	grade.unshift("500","6000");
	document.write("<br>Final: " + grade);
}

