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

#define _GLIBCXX_USE_CXX11_ABI 0
//Default Temperature is in Celsius
//Comment the next line for Temperature in Fahrenheit
#define temperatureCelsius

//BLE server name
#define bleServerName "grupo_2_ESP"


#define DHTPIN 17
#define DHTTYPE DHT11
Adafruit_BME280 bme; // I2C
DHT_Unified dht(DHTPIN, DHTTYPE);
clock_t begin = clock();
char *timeArr;
bool first=true;
float temp;
float tempF;
float hum;
float pressure;



// Timer variables
unsigned long lastTime = 0;
unsigned long timerDelay = 30000;

boolean newTime = false;

bool deviceConnected = false;

// See the following for generating UUIDs:
// https://www.uuidgenerator.net/
#define SERVICE_UUID "1aa3d607-8465-4476-a592-40a6b0f14efb"
  
// Temperature Characteristic and Descriptor
#define temperatureCelsius
  BLECharacteristic bmeCharacteristics("b673b689-9772-47a8-825d-51f07e9a3098", BLECharacteristic::PROPERTY_NOTIFY);
  BLEDescriptor bmeDescriptor(BLEUUID((uint16_t)0x2902)); //check why this
  
#define timeC 
  BLECharacteristic timeCharacteristics("3a3e6d34-6a5d-4205-9b91-94ed7c92f6e1", BLECharacteristic::PROPERTY_NOTIFY);
  BLEDescriptor timeDescriptor(BLEUUID((uint16_t)0x2902)); //check why this

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

    #define timeC
    bmeService->addCharacteristic(&timeCharacteristics);
    bmeDescriptor.setValue("Time Values");
    bmeCharacteristics.addDescriptor(&timeDescriptor);
  
  
  // Start the service
  bmeService->start();

  // Start advertising
  BLEAdvertising *pAdvertising = BLEDevice::getAdvertising();
  pAdvertising->addServiceUUID(SERVICE_UUID);
  pServer->getAdvertising()->start();
  Serial.println("Waiting a client connection to notify...");
  //Assign callback functions for the Characteristics
   
}

void loop() {
  static char tempStringC[4];
  if (deviceConnected) {
    pressure = bme.readPressure()/100;
    //if ((millis() - lastTime) > timerDelay) {
      // Read temperature as Celsius (the default)
      //temp = bme.readTemperature();
      // Fahrenheit
      //tempF = 1.8*temp +32;
      // Read humidity
      //hum = bme.readHumidity();
  
      //Notify temperature reading from BME sensor

        // Delay between measurements.
        delay(delayMS);
        // Get temperature event and print its value.
        sensors_event_t event;
        dht.temperature().getEvent(&event);
        
      
          
    Serial.print("\n");
   Serial.print("\nnew Time");
   Serial.print((char*)timeCharacteristics.getValue().c_str());
    Serial.print("\n");
   Serial.print("\ntimeArr");
   
    timeArr=(char*)timeCharacteristics.getValue().c_str();
    Serial.print(timeArr);
    
    
   Serial.print("\n");
  
   
   
        Serial.print(timeConverter());
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

 
    Serial.print(": Pressure: ");
    Serial.print(pressure);
    Serial.print(" hPa");
  
    static char humStringC[7];
    static char pressureStringC[3];
    static char temporC[3];
      dtostrf(event.relative_humidity,2,0,humStringC);
    dtostrf(event.relative_humidity,3,0,temporC);
    dtostrf(pressure,3,0,pressureStringC);
  
  //charArray to send to the client
    static char toSendC[10];
    memset(toSendC,0,10);
    //this loop is redundant, currently for some weird reason it needs is to work; this compiler is the devil: try to fix later
    for(int i=0;i<2;i++){
      humStringC[i] = temporC[i] ;
    }
    for(int i=0;i<4;i++){
      humStringC[i+2] = tempStringC[i] ;
    }
   
    for(int i=0;i<6;i++){
      toSendC[i]=humStringC[i];
    }
    for(int i=0;i<3;i++){
      toSendC[i+6]=pressureStringC[i];
    }

    //we are aware this burns one's eyes
    toSendC[0]=temporC[1];
    toSendC[1]=temporC[2];
    toSendC[9]='\0'; 

    
   delay(1000);
  bmeCharacteristics.setValue(toSendC);
  bmeCharacteristics.notify();
  
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
  }
}

String timeConverter(){
  clock_t end = clock();
  float time_spent = round((float)(end - begin)) / CLOCKS_PER_SEC;
  float toAdd=atof(timeArr);
  float timePassed = millis();
  int timePassedDiv = (int)roundNo(timePassed/1000);
  double currentTime = (double)timePassedDiv+(double)toAdd;
  Serial.print("\ntoAdd");
  Serial.print(toAdd);
  Serial.print("\n");
  Serial.print("\ntimeSpents");
  Serial.print(timePassedDiv);
  Serial.print(time_spent);
  Serial.print("\n");
  time_t currentTimeT = currentTime;
    Serial.print("\ncurrentTime");
  Serial.print(currentTime);
  Serial.print("\n");
  

    struct tm  ts;
    char       buf[80];

    // Get current time
 

    // Format time, "ddd yyyy-mm-dd hh:mm:ss zzz"
    ts = *localtime(&currentTimeT);
    strftime(buf, sizeof(buf), "%a %Y-%m-%d %H:%M:%S %Z", &ts);

  return buf;
  }

  int roundNo(float num)
{
    return num < 0 ? num - 0.5 : num + 0.5;
}
  
