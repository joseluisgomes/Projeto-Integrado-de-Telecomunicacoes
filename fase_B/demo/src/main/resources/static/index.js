setInterval(main,1000);

function main(){
  fetch('/api/group2/samples')
    .then(function (response) {
      return response.json();
  }).then(function (apiJsonData) {
      samples = parseData(apiJsonData);
      lastSample = getLastSample(samples);
      displaySample(lastSample);
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

function displaySample(lastSample){
  document.getElementById('num_samples').innerHTML = lastSample.sampleID;
  document.getElementById('cur_temp').innerHTML = lastSample.temperature + ' ºC';
  document.getElementById('cur_hum').innerHTML = lastSample.humidity + ' %';
  document.getElementById('cur_pres').innerHTML = lastSample.pressure + ' hPa';
  return;
}

function drawGraph(samples,lastSample){
  let idValues = [];
  let tempValues = [];
  let humValues = [];
  let presValues = [];

  for(i = 0; i < lastSample.sampleID; i++){
    let value_id = samples[i].sampleID;
    let value_temp = samples[i].temperature;
    let value_hum = samples[i].humidity;
    let value_pres = samples[i].pressure;

    idValues.push(value_id);
    tempValues.push(value_temp);
    humValues.push(value_hum);
    presValues.push(value_pres);
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
        backgroundColor: "rgb(33,150,243)",
        borderColor:"rgb(255,255,255)",
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
        backgroundColor: "rgb(0,150,136)",
        borderColor:"rgb(255,255,255)",
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
        backgroundColor: "rgb(139,195,74)",
        borderColor:"rgb(255,255,255)",
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