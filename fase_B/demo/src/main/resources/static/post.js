function alert() {
    let text;
    let id = prompt("ID of gateway to update", "GatewayID");
      if (id == null || id == "") {
        text = "User cancelled the prompt.";
      } else {
        //console.log(id)
        let change = prompt('Change to be made to Gateway ' + id, "Change");

        if(change == null || change == "") {
            text = "User cancelled the prompt.";
        } else {
            text = "Gateway " + id + " status is being updated to " + change;

            if(change == 'start') {
                startFunc(id,change);
            }
        }
      }
      console.log(text);
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

    let data = id + change;
    console.table(data);

    xhr.send(data);
}