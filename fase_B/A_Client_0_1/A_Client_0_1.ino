#include <NTPClient.h>

  #include <ThingSpeak.h>
  
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
  #include <iostream>
  
  
  #include <WiFi.h>
  #include "secrets.h"
  #include "ThingSpeak.h" 
  char ssid[] = SECRET_SSID; 
  char pass[] = SECRET_PASS;
  int keyIndex = 0;
  WiFiClient  client;
  
  WiFiUDP ntpUDP;
  NTPClient timeClient(ntpUDP, "europe.pool.ntp.org");

  
  unsigned long myChannelNumber = SECRET_CH_ID;
  const char * myWriteAPIKey = SECRET_WRITE_APIKEY;
  #include <BLEDevice.h>
  #include <Wire.h>
  

  
  #define bleServerName "grupo_2_ESP"
  
  static BLEUUID bmeServiceUUID("1aa3d607-8465-4476-a592-40a6b0f14efb");
  
  
  
    //Temperature Celsius Characteristic
    static BLEUUID temperatureCharacteristicUUID("b673b689-9772-47a8-825d-51f07e9a3098");
    
    //Time Characteristic
    static BLEUUID timeCharacteristicUUID("3a3e6d34-6a5d-4205-9b91-94ed7c92f6e1");
    
  //Flags stating if should begin connecting and if the connection is up
  static boolean doConnect = false;
  static boolean connected = false;
  
  //Address of the peripheral device. Address will be found during scanning...
  static BLEAddress *pServerAddress;
   
  //Characteristicd that we want to read
  static BLERemoteCharacteristic* temperatureCharacteristic;
    //Characteristicd that we want to read
  static BLERemoteCharacteristic* timeCharacteristic;
  
  //Activate notify
  const uint8_t notificationOn[] = {0x1, 0x0};
  const uint8_t notificationOff[] = {0x0, 0x0};
  
  //Variable to store temperature and humidity
  char  temperatureChar[9];
  
  //Flags to check whether new temperature and humidity readings are available
  boolean newTemperature = false;
  
  
  //Connect to the BLE Server that has the name, Service, and Characteristics
  bool connectToServer(BLEAddress pAddress) { 
     BLEClient* pClient = BLEDevice::createClient();
   
    // Connect to the remove BLE Server.
    pClient->connect(pAddress);
    Serial.println(" - Connected to server");
   
    // Obtain a reference to the service we are after in the remote BLE server.
    BLERemoteService* pRemoteService = pClient->getService(bmeServiceUUID);
    if (pRemoteService == nullptr) {
      Serial.print("Failed to find our service UUID: ");
      Serial.println(bmeServiceUUID.toString().c_str());
      return (false);
    }
   
    // Obtain a reference to the characteristics in the service of the remote BLE server.
    temperatureCharacteristic = pRemoteService->getCharacteristic(temperatureCharacteristicUUID);
    timeCharacteristic = pRemoteService->getCharacteristic(timeCharacteristicUUID);
  
    if (temperatureCharacteristic == nullptr || timeCharacteristic == nullptr) {
      Serial.print("Failed to find our characteristic UUID");
      return false;
    }
    Serial.println(" - Found our characteristics");
   
    //Assign callback functions for the Characteristics
    temperatureCharacteristic->registerForNotify(temperatureNotifyCallback);
    
    
    return true;
  }
  
  //Callback function that gets called, when another device's advertisement has been received
  class MyAdvertisedDeviceCallbacks: public BLEAdvertisedDeviceCallbacks {
    void onResult(BLEAdvertisedDevice advertisedDevice) {
      if (advertisedDevice.getName() == bleServerName) { //Check if the name of the advertiser matches
        advertisedDevice.getScan()->stop(); //Scan can be stopped, we found what we are looking for
        pServerAddress = new BLEAddress(advertisedDevice.getAddress()); //Address of advertiser is the one we need
        doConnect = true; //Set indicator, stating that we are ready to connect
        Serial.println("Device found. Connecting!");
      }
    }
  };
   
  //When the BLE Server sends a new temperature reading with the notify property
  static void temperatureNotifyCallback(BLERemoteCharacteristic*  pBLERemoteCharacteristic, 
                                          uint8_t* pData, size_t length, bool isNotify) {
    //casting to char: had to do it this way, classic normal way isnt working on this compiler
    for(int i=0;i<9;i++){
      if(pData=='\0')break;
      char stor = pData[i];
      temperatureChar[i]=stor;
      }
    newTemperature = true;
  }
  
  

  
  void setup() {
    
    //Start serial communication
    Serial.begin(115200);
    WiFi.mode(WIFI_STA);   
    ThingSpeak.begin(client);  // Initialize ThingSpeak
    timeClient.begin();
    timeClient.setTimeOffset(0);
    Serial.println("Starting Arduino BLE Client application...");
  
    //Init BLE device
    BLEDevice::init("");
   
    // Retrieve a Scanner and set the callback we want to use to be informed when we
    // have detected a new device.  Specify that we want active scanning and start the
    // scan to run for 30 seconds.
    BLEScan* pBLEScan = BLEDevice::getScan();
    pBLEScan->setAdvertisedDeviceCallbacks(new MyAdvertisedDeviceCallbacks());
    pBLEScan->setActiveScan(true);
    pBLEScan->start(30);
    while (!Serial) {
      ; // wait for serial port to connect. Needed for Leonardo native USB port only
    }
    
    
    
  }

  int startTime = timeClient.getEpochTime()-20;
  void loop() {
    // If the flag "doConnect" is true then we have scanned for and found the desired
    // BLE Server with which we wish to connect.  Now we connect to it.  Once we are
    // connected we set the connected flag to be true.
    if(WiFi.status() != WL_CONNECTED){
        Serial.print("Attempting to connect to SSID: ");
        Serial.println(SECRET_SSID);
        while(WiFi.status() != WL_CONNECTED){
          WiFi.begin(ssid, pass);  // Connect to WPA/WPA2 network. Change this line if using open or WEP network
          Serial.print(".");
          delay(5000);     
        } 
        Serial.println("\nConnected WiFi.\n");
      }
    timeClient.update();
    String formattedTime = timeClient.getFormattedTime();
    if(timeClient.getEpochTime()-startTime>=20){
      timeClient.update();
      startTime=timeClient.getEpochTime();
    if (doConnect == true) {
      if (connectToServer(*pServerAddress)) {
        Serial.println("We are now connected to the BLE Server.");
        //Activate the Notify property of each Characteristic
        temperatureCharacteristic->getDescriptor(BLEUUID((uint16_t)0x2902))->writeValue((uint8_t*)notificationOn, 2, true);
        //timeCharacteristic->getDescriptor(BLEUUID((uint16_t)0x2902))->writeValue((uint8_t*)notificationOn, 2, true);
        connected = true;
      } else {
        Serial.println("We have failed to connect to the server; Restart your device to scan for nearby BLE server again.");
      }
      doConnect = false;
    }
    if(newTemperature){
      newTemperature = false;
    
      //the variables to be passed to ThingSpeak
      char hum[3];
      memset(hum,0,3);
      char temp[5];
      memset(temp,0,5);
      char pressure[4];
      memset(pressure,0,4);
   
      for(int i=0;i<2;i++){
        hum[i]=temperatureChar[i];
      }
      hum[2]='\0';
      
      for(int i=0;i<4;i++){
        temp[i]=temperatureChar[i+2];
      }
      temp[4]='\0';
      
      for(int i=0;i<3;i++){
        pressure[i]=temperatureChar[i+6];
      }
      pressure[3]='\0';
    
      // Connect or reconnect to WiFi
      
  
      Serial.print("\n----> New Measurements <----\n");
      Serial.print("Time measured in hh:mm:ss.\n");
      ThingSpeak.setField(1,temp);
      //timeClient.update();
      Serial.print(formattedTime); 
      Serial.print(": Temperature: ");
      for(int i=0;i<4;i++){
        Serial.print(temp[i]);
      }
      Serial.print(" ºC\n");
      
      ThingSpeak.setField(2, hum);
      //timeClient.update();
      Serial.print(formattedTime); 
      Serial.print(": Humidade: ");
      for(int i=0;i<2;i++){
      Serial.print(hum[i]);
      }
      Serial.print(" %\n");
      
      ThingSpeak.setField(3, pressure);
      //timeClient.update();
      Serial.print(formattedTime); 
      Serial.print(": Pressure: ");
      for(int i=0;i<3;i++){
        Serial.print(pressure[i]);
      }
      Serial.print(" hPa\n");
      Serial.print("\n---- / / ----\n");
      ThingSpeak.setStatus("status");
    
      // write to the ThingSpeak channel
      int x = ThingSpeak.writeFields(myChannelNumber, myWriteAPIKey);
    
      
   }
   
  }
  byte pSend[1];
   pSend[0]='a';
   timeCharacteristic ->writeValue(pSend,sizeof(pSend));
  }
