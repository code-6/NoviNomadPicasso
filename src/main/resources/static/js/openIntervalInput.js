function addElement() {
    var element = document.createElement("input");
    element.setAttribute("value", "");
    element.setAttribute("name", "newInput");
    var foo = document.getElementById("attachInterval");
    foo.appendChild(element);
}
function addInput(){
    document.getElementById("attachInterval").innerHTML="";
    var noInp=parseInt(document.getElementById("attachDrivers").value);
    for(var i = 0; i <= noInp; i++){
        addElement();
    }
}

