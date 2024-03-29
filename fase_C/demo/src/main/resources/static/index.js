setInterval(main,1000);

function main(){
  fetch('/api/group2/samples')
    .then(function (response) {
      return response.json();
  }).then(function (apiJsonData) {
      samples = parseData(apiJsonData);
      lastSample = getLastSample(samples);
      displaySample(lastSample,samples);
      drawGraph(samples,lastSample);
  })
}

function parseData(samples){
  samples = JSON.stringify(samples);
  samples = JSON.parse(samples);
  return samples;
}

function getLastSample(samples){
  lastSample = samples[samples.length - 1];
  return lastSample;
}

function displaySample(lastSample,samples){
  document.getElementById('num_samples').innerHTML = lastSample.sampleID;

  for(i = 0; i < lastSample.sampleID;i++) {
    if(samples[i].gatewayID == 1) {
        gateTemp = samples[i].temperature;
        gateHum = samples[i].humidity;
        gatePres = samples[i].pressure;
    }
  }
  document.getElementById('cur_temp').innerHTML = gateTemp + ' ºC';
  document.getElementById('cur_hum').innerHTML = gateHum + ' %';
  document.getElementById('cur_pres').innerHTML = gatePres + ' hPa';
  return;
}

function drawGraph(samples,lastSample){
  let idValues = [];
  let tempValues = [];
  let humValues = [];
  let presValues = [];

  for(i = 0; i < lastSample.sampleID; i++){
    if(samples[i].gatewayID == 1) {
        let value_id = samples[i].sampleID;
        let value_temp = samples[i].temperature;
        let value_hum = samples[i].humidity;
        let value_pres = samples[i].pressure;

        idValues.push(value_id);
        tempValues.push(value_temp);
        humValues.push(value_hum);
        presValues.push(value_pres);
    }
  }

  const collection = document.getElementsByTagName("canvas");
  var tag = collection[0].id;

  if(tag == 'temp_graph'){
    drawTempGraph(idValues,tempValues);
  }

  else if(tag == 'hum_graph'){
    drawHumGraph(idValues,humValues);
  }

  else if(tag == 'pres_graph'){
    drawPresGraph(idValues,presValues);
  }
}

function drawTempGraph(idValues,tempValues) {
  new Chart("temp_graph", {
    type: "line",
    data: {
      labels: idValues,
      datasets: [{
        fill:true,
        lineTension:0,
        label:"Temperature in ºC",
        backgroundColor: "hsl(207,90%,54%)",
        borderColor:"hsl(0,0%,100%)",
        data: tempValues
      }]
    },
    options: {
      legend: { display: true },
      scales: { yAxes: [{ticks: {min: 0, max:40}}] }
    }
  });
}

function drawHumGraph(idValues,humValues) {
  new Chart("hum_graph", {
    type: "line",
    data: {
      labels: idValues,
      datasets: [{
        fill:true,
        lineTension:0,
        label:"Humidity in %",
        backgroundColor: "hsl(174,100%,29%)",
        borderColor:"hsl(0,0%,100%)",
        data: humValues
      }]
    },
    options: {
      legend: { display: true },
      scales: { yAxes: [{ticks: {min: 0, max:100}}] }
    }
  });
}

function drawPresGraph(idValues,presValues) {
  new Chart("pres_graph", {
    type: "line",
    data: {
      labels: idValues,
      datasets: [{
        fill:true,
        lineTension:0,
        label:"Atmosferic Pressure in hPa",
        backgroundColor: "hsl(88,50%,53%)",
        borderColor:"hsl(0,0%,100%)",
        data: presValues
      }]
    },
    options: {
      legend: { display:true },
      scales: { yAxes: [{ticks: {min: 0, max:1000}}] }
    }
  });
}

function nextGraph(){
  const collection = document.getElementsByTagName("canvas");
  var tag = collection[0].id;

  if(tag == 'temp_graph'){
    document.getElementById(tag).id = 'hum_graph';
  }
  else if(tag == 'hum_graph'){
    document.getElementById(tag).id = 'pres_graph';
  }
  else if(tag == 'pres_graph'){
    document.getElementById(tag).id = 'temp_graph';
  }
}

function prevGraph(){
  const collection = document.getElementsByTagName("canvas");
  var tag = collection[0].id;

  if(tag == 'temp_graph'){
    document.getElementById(tag).id = 'pres_graph';
  }
  else if(tag == 'hum_graph'){
    document.getElementById(tag).id = 'temp_graph';
  }
  else if(tag == 'pres_graph'){
    document.getElementById(tag).id = 'hum_graph';
  }
}