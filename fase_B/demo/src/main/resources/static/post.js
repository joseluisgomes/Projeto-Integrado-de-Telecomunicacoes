function alert() {
    var modal = document.getElementById("customModal");
    var btn = document.getElementById("btn-change");
    var span = document.getElementsByClassName("close")[0];

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
    var list = document.getElementById("myList");
    document.getElementById("actionText").value = list.options[list.selectedIndex].text;
}

function startFunc(id,change) {
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