#include <DHT.h>
#include <DHT_U.h>
#include <BLEDevice.h>
#include <BLEServer.h>
#include <BLEUtils.h>
#include <BLE2902.h>
#include <Wire.h>
#include <Adafruit_Sensor.h>
#include <Adafruit_BME280.h>
#define temperatureCelsius
#define bleServerName "grupo_2_ESP"
#define DHTPIN 17
#define DHTTYPE DHT11
#define SERVICE_UUID "1aa3d607-8465-4476-a592-40a6b0f14efb"

BLECharacteristic bmeCharacteristics("b673b689-9772-47a8-825d-51f07e9a3098", BLECharacteristic::PROPERTY_NOTIFY);
BLEDescriptor bmeDescriptor(BLEUUID((uint16_t)0x2902)); 
Adafruit_BME280 bme; // I2C
DHT_Unified dht(DHTPIN, DHTTYPE);
float temp;
float tempF;
float hum;
unsigned long lastTime = 0; // Timer variables: lastTime, timerDelay
unsigned long timerDelay = 30000;
uint32_t delayMS;
bool deviceConnected = false;

//Setup callbacks onConnect and onDisconnect
class MyServerCallbacks : public BLEServerCallbacks {
  void onConnect(BLEServer* pServer) {
    deviceConnected = true;
  }
  
  void onDisconnect(BLEServer* pServer) {
    deviceConnected = false;
  }
};

void initBME(){
  if (!bme.begin(0x76)){
    Serial.println("Could not find a valid BME280 sensor, check wiring!");
    while(1);
  }
}

void setup() {
  // Start serial communication 
  Serial.begin(115200);
  dht.begin();
  sensor_t sensor;
  dht.temperature().getSensor(&sensor);
  dht.humidity().getSensor(&sensor);
  delayMS = sensor.min_delay / 1000;

  // Create the BLE Device
  BLEDevice::init(bleServerName);

  // Create the BLE Server
  BLEServer *pServer = BLEDevice::createServer();
  pServer->setCallbacks(new MyServerCallbacks());

  // Create the BLE Service
  BLEService *bmeService = pServer->createService(SERVICE_UUID);

  // Create BLE Characteristics and Create a BLE Descriptor
  bmeService->addCharacteristic(&bmeCharacteristics);
  bmeDescriptor.setValue("BME values");
  bmeCharacteristics.addDescriptor(&bmeDescriptor);
  
  bmeService->start();

  // Start advertising
  BLEAdvertising *pAdvertising = BLEDevice::getAdvertising();
  pAdvertising->addServiceUUID(SERVICE_UUID);
  pServer->getAdvertising()->start();
  Serial.println("Waiting a client connection to notify...");
}

void loop() {
  static char tempStringC[4];
  static char humStringC[7];
  
  if (deviceConnected) {
    delay(delayMS);
    sensors_event_t event;
    
    dht.temperature().getEvent(&event);
    if (isnan(event.temperature)) 
      Serial.println(F("Error reading temperature!"));
    else {
      Serial.print(F("Temperature: "));
      Serial.print(event.temperature);
    
      dtostrf(event.temperature,4,1,tempStringC);
      Serial.println(F("°C"));
    }
    
    dht.humidity().getEvent(&event);
    if (isnan(event.relative_humidity)) 
      Serial.println(F("Error reading humidity!"));
    else {
      Serial.print(F("Humidity: "));
      Serial.print(event.relative_humidity);
      Serial.println(F("%"));
    }
    dtostrf(event.relative_humidity,2,0,humStringC);
  
    for(int i=0;i<4;i++)
      humStringC[i+2] = tempStringC[i];
    humStringC[6]='\0'; 
    
    Serial.print("\n");
    Serial.print(tempStringC[0]);
    Serial.print(tempStringC[1]);
    Serial.print(tempStringC[2]);
    Serial.print(tempStringC[3]);
    Serial.print("\n");
    
    bmeCharacteristics.setValue(humStringC);
    bmeCharacteristics.notify();
  }
}
