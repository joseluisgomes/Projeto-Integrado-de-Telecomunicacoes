function customAlert() {
    var modal = document.getElementById("customModal");
    var btn = document.getElementById("btn-change");
    var span = document.getElementById("btn-close");

    btn.onclick = function() {
        modal.style.display = "block";
    }

    span.onclick = function() {
        modal.style.display = "none";
    }

    window.onclick = function(event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }
}

function actionFunction() {
    var div = document.getElementById("invDiv");
    div.style.visibility = "visible";
}

function gateFunction() {
    var btn = document.getElementById("btn-confirm");
    btn.style.visibility = "visible";
}

function startFunc() {
    var change = document.getElementById("myList1").value = myList1.options[myList1.selectedIndex].text;
    var id = document.getElementById("myList2").value = myList2.options[myList2.selectedIndex].text;
    console.table(change,id);

    let xhr = new XMLHttpRequest();
    xhr.open("POST", "/api/group2/samples/protocol");
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");

    xhr.onreadystatechange = function () {
    if (xhr.readyState === 4) {
      console.log(xhr.status);
      console.log(xhr.responseText);
    }};

    var obj = new Object();
    obj.gatewayID = id;
    obj.message = change;

    var data = JSON.stringify(obj);

    console.table(data);

    xhr.send(data);
}