setInterval(main,1000);

function main(){
  fetch('/api/group2/samples')
    .then(function (response) {
        return response.json();
    }).then(function (apiJsonData) {
        renderDataInTheTable(apiJsonData);
    })
}

function renderDataInTheTable(data) {
    const arrayTitle = ['Gateway','ID','Temperature','Humidity','Pressure','Timestamp']
    const mytable = document.getElementById("data-table");
    mytable.innerHTML = '';

    let initRow  = document.createElement("tr");
    initRow.style = "width:100%";

    for(let i = 0; i < 6; i++){
      let cell = document.createElement("th");

      if(i == 0 || i == 1){
        cell.style = "width:12%";
      }
      else if(i == 5){
        cell.style = "width:25%";
      }
      else{
        cell.style = "width:17%";
      }

      cell.innerText = arrayTitle[i];
      initRow.appendChild(cell);
    }

    mytable.appendChild(initRow);

    data.forEach(sample => {
        let newRow = document.createElement("tr");
        Object.values(sample).forEach((value) => {
            let cell = document.createElement("td");
            cell.innerText = value;
            newRow.appendChild(cell);
        })
        mytable.appendChild(newRow);
    });
  }