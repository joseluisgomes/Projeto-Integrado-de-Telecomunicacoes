#include <ETH.h>
#include <WiFi.h>
#include <WiFiAP.h>
#include <WiFiClient.h>
#include <WiFiGeneric.h>
#include <WiFiMulti.h>
#include <WiFiScan.h>
#include <WiFiServer.h>
#include <WiFiSTA.h>
#include <WiFiType.h>
#include <WiFiUdp.h>



#include <NTPClient.h>
#include <DHT.h>
#include <DHT_U.h>
#include <BLEDevice.h>
#include <BLEServer.h>
#include <BLEUtils.h>
#include <BLE2902.h>
#include <Wire.h>
#include <Adafruit_Sensor.h>
#include <Adafruit_BME280.h>

//Sensors Defines 
#define temperatureCelsius

//Time_NTP
WiFiClient client;
WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP, "europe.pool.ntp.org");

char ssid[]= "LAP3";
char pass[]= "LAP3LAP3";



//DHT
    #define DHTPIN 17
    #define DHTTYPE DHT11
    DHT_Unified dht(DHTPIN, DHTTYPE); 

  //BME
    Adafruit_BME280 bme; // I2C

//BLE Defines
#define bleServerName "grupo_2_ESP"
#define SERVICE_UUID "1aa3d607-8465-4476-a592-40a6b0f14efb"
bool deviceConnected = false;

#define temperatureCelsius
  BLECharacteristic bmeCharacteristics("b673b689-9772-47a8-825d-51f07e9a3098", BLECharacteristic::PROPERTY_NOTIFY);
  BLEDescriptor bmeDescriptor(BLEUUID((uint16_t)0x2902)); //check why this

 
//Variables to keep sensors outputs
float temp;
float tempF;
float hum;
float pressure;
 

 
//Setup callbacks onConnect and onDisconnect
class MyServerCallbacks: public BLEServerCallbacks {
  void onConnect(BLEServer* pServer) {
    deviceConnected = true;
  };
  void onDisconnect(BLEServer* pServer) {
    deviceConnected = false;
  }
};

//Initiate de BME Sensor
void initBME(){
  if (!bme.begin(0x76)) {
    Serial.println("Could not find a valid BME280 sensor, check wiring!");
    while (1);
  }
}
 

 
void setup() {
  
  // Start serial communication 
    Serial.begin(115200);
    WiFi.mode(WIFI_STA);
  //Init DHT
  dht.begin();
  sensor_t sensor;
  dht.temperature().getSensor(&sensor);
  dht.humidity().getSensor(&sensor);
  
  //Init BME
  initBME();
  
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
  Serial.println("\nWaiting a client connection to notify...");

  timeClient.begin();
  timeClient.setTimeOffset(0);
}

int startTime = timeClient.getEpochTime();
int startTime2 = timeClient.getEpochTime()-20;

void loop() {

  
      
  static char tempStringC[4];
  //timeClient.update();
  
  if (deviceConnected) {
    if(WiFi.status() != WL_CONNECTED){
        Serial.print("Attempting to connect to SSID: ");
        Serial.println("LAP3");
        while(WiFi.status() != WL_CONNECTED){
          WiFi.begin(ssid, pass);  // Connect to WPA/WPA2 network. Change this line if using open or WEP network
          Serial.print(".");
          delay(5000);
        } 
        Serial.println("\nConnected WiFi.\n");
      }
      if(timeClient.getEpochTime()-startTime>=1){
    timeClient.update();
    String formattedTime = timeClient.getFormattedTime();
    startTime=timeClient.getEpochTime();
    pressure = bme.readPressure()/100;
    sensors_event_t event;
    // Get temperature event and print its value.
    Serial.print("\n----> New Measurements <----\n");
    dht.temperature().getEvent(&event);
    if (isnan(event.temperature)) {
      Serial.println(F("Error reading temperature!"));
    }else{
      Serial.print(formattedTime);
      Serial.print(F(": Temperature: "));
      Serial.print(event.temperature);
      dtostrf(event.temperature,4,1,tempStringC);
      Serial.println(F("°C"));
    }
    // Get humidity event and print its value.
    dht.humidity().getEvent(&event);
    if (isnan(event.relative_humidity)) {
      Serial.println(F("Error reading humidity!"));
    }else{
      Serial.print(formattedTime);
      Serial.print(F(": Humidity: "));
      Serial.print(event.relative_humidity);
      Serial.println(F("%"));
    }
    Serial.print(formattedTime);
    Serial.print(": Pressure: ");
    Serial.print(pressure);
    Serial.print(" hPa");
    Serial.print("\n---- / / ----\n");

    //Strings to pass as arguments to ThingSpeak
    static char humStringC[7];
    static char pressureStringC[3];
    static char temporC[3];
    
    //converts from float to charArray
    dtostrf(event.relative_humidity,2,0,humStringC);
    dtostrf(event.relative_humidity,3,0,temporC);
    dtostrf(pressure,3,0,pressureStringC);
    //for some unknown reason, when it dtostrfs the event.relative_humidity, it doesnt store on its first position event.relative_humidity[0] - only on the second event.relative_humidity[1].
    //this is some sort of solution 
    
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
    toSendC[9]='\0'; //need to have a terminator otherwise the compiler continues to print unallocated memory - its the devil
    
    if(timeClient.getEpochTime()-startTime2>=20){
      startTime2=timeClient.getEpochTime();
     bmeCharacteristics.setValue(toSendC);
     bmeCharacteristics.notify();
    }
  }
}
}
 
