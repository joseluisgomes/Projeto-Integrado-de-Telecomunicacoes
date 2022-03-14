#include <DHT.h>
#include <DHT_U.h>

/*********
  Rui Santos
  Complete instructions at https://RandomNerdTutorials.com/esp32-ble-server-client/
  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files.
  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
*********/

#include <BLEDevice.h>
#include <BLEServer.h>
#include <BLEUtils.h>
#include <BLE2902.h>
#include <Wire.h>
#include <Adafruit_Sensor.h>
#include <Adafruit_BME280.h>

//Default Temperature is in Celsius
//Comment the next line for Temperature in Fahrenheit
#define temperatureCelsius

//BLE server name
#define bleServerName "grupo_2_ESP"


#define DHTPIN 17
#define DHTTYPE DHT11
Adafruit_BME280 bme; // I2C
DHT_Unified dht(DHTPIN, DHTTYPE);
float temp;
float tempF;
float hum;
float pressure;

// Timer variables
unsigned long lastTime = 0;
unsigned long timerDelay = 30000;
float startTime = millis();

bool deviceConnected = false;

// See the following for generating UUIDs:
// https://www.uuidgenerator.net/
#define SERVICE_UUID "1aa3d607-8465-4476-a592-40a6b0f14efb"
  
// Temperature Characteristic and Descriptor
#define temperatureCelsius
  BLECharacteristic bmeCharacteristics("b673b689-9772-47a8-825d-51f07e9a3098", BLECharacteristic::PROPERTY_NOTIFY);
  BLEDescriptor bmeDescriptor(BLEUUID((uint16_t)0x2902)); //check why this

//Setup callbacks onConnect and onDisconnect
class MyServerCallbacks: public BLEServerCallbacks {
  void onConnect(BLEServer* pServer) {
    deviceConnected = true;
  };
  void onDisconnect(BLEServer* pServer) {
    deviceConnected = false;
  }
};

void initBME(){
  if (!bme.begin(0x76)) {
    Serial.println("Could not find a valid BME280 sensor, check wiring!");
    while (1);
  }
}

uint32_t delayMS;

void setup() {
  // Start serial communication 
  Serial.begin(115200);
  dht.begin();
  sensor_t sensor;
  dht.temperature().getSensor(&sensor);
  dht.humidity().getSensor(&sensor);
  delayMS = sensor.min_delay / 1000;
  // Init BME Sensor
  initBME();
  //turn on when included

  // Create the BLE Device
  BLEDevice::init(bleServerName);

  // Create the BLE Server
  BLEServer *pServer = BLEDevice::createServer();
  pServer->setCallbacks(new MyServerCallbacks());

  // Create the BLE Service
  BLEService *bmeService = pServer->createService(SERVICE_UUID);

  // Create BLE Characteristics and Create a BLE Descriptor
  // Temperature
  #define temperatureCelsius
    bmeService->addCharacteristic(&bmeCharacteristics);
    bmeDescriptor.setValue("BME values");
    bmeCharacteristics.addDescriptor(&bmeDescriptor);
  
  
  // Start the service
  bmeService->start();

  // Start advertising
  BLEAdvertising *pAdvertising = BLEDevice::getAdvertising();
  pAdvertising->addServiceUUID(SERVICE_UUID);
  pServer->getAdvertising()->start();
  Serial.println("Waiting a client connection to notify...");
}

void loop() {
  static char tempStringC[4];
  if (deviceConnected) {
    //if ((millis() - lastTime) > timerDelay) {
      // Read temperature as Celsius (the default)
      //temp = bme.readTemperature();
      // Fahrenheit
      //tempF = 1.8*temp +32;
      // Read humidity
      //hum = bme.readHumidity();
      pressure = bme.readPressure()/100;
  
      //Notify temperature reading from BME sensor

        // Delay between measurements.
        
        delay(1000);
        // Get temperature event and print its value.
        sensors_event_t event;
        dht.temperature().getEvent(&event);
  if (isnan(event.temperature)) {
    Serial.println(F("Error reading temperature!"));
  }
  else {
    Serial.print(F("Temperature: "));
    Serial.print(event.temperature);
    
    dtostrf(event.temperature,4,1,tempStringC);
    Serial.println(F("°C"));
  }
  // Get humidity event and print its value.
  dht.humidity().getEvent(&event);
  if (isnan(event.relative_humidity)) {
    Serial.println(F("Error reading humidity!"));
  }
  else {
    Serial.print(F("Humidity: "));
    Serial.print(event.relative_humidity);
    Serial.println(F("%"));
  }
  
  static char humStringC[7];
  dtostrf(event.relative_humidity,2,0,humStringC);
  
    for(int i=0;i<4;i++){
    humStringC[i+2] = tempStringC[i] ;
    }
    humStringC[6]='\0'; 
    Serial.print("\n");
    Serial.print(humStringC[0]);
    Serial.print(humStringC[1]);
    Serial.print(humStringC[2]);
    Serial.print(humStringC[3]);
    Serial.print(humStringC[4]);
    Serial.print(humStringC[5]);
    Serial.print(humStringC[6]);
    Serial.print("\nPressure: ");
    Serial.print(pressure);
    Serial.print(" hPa");


    
    Serial.print("\n");
    if(millis()-startTime>=20000){
  bmeCharacteristics.setValue(humStringC);
  bmeCharacteristics.notify();
  startTime = millis();}
  
      /*#define temperatureCelsius
        static char tempC[6];
        static char humC[6];
        dtostrf(temp, 6, 2, tempC);
        dtostrf(hum, 6, 2, humC);
        static char toSendC[12];
        strcat(toSendC,tempC);
        strcat(toSendC,humC);
      
         
        //Set temperature Characteristic value and notify connected client
        //gotta send humidity
        //bmeCharacteristics.setValue(toSendC);
        //bmeCharacteristics.notify();
        
        Serial.print("Temperature Celsius: ");
        Serial.print(temp);
        Serial.print(" ºC");
        Serial.print(" - Humidity: ");
        Serial.print(hum);
        Serial.println(" %");
      
      lastTime = millis();
      */   
   // }
   delay(1000);
  }
  
}
