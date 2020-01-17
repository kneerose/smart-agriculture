#include <FirebaseArduino.h>
#include <DHT.h>
#include <ESP8266WiFi.h>
#define dhtpin D5                          
#define FIREBASE_HOST "https://nodemcu-de986.firebaseio.com/"                          // database URL 
#define FIREBASE_AUTH "Im48JL2QGm03TJd1BpkhHotTyG14baS0Te134OW3"             // secret key
#define DHTTYPE DHT11 
DHT dht(dhtpin, DHTTYPE);
#define Soil_moisture A0
#define WIFI_SSID "Visit_nepal_2020"                                           
#define WIFI_PASSWORD "sUvY@12345"   
void setup() {

  Serial.begin(9600);
  dht.begin();
  Serial.println("humidity and temperature sensor");
  delay(1000);
  Serial.println("Serial communication started\n\n");  
   pinMode(Soil_moisture,INPUT);        
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);                                     //try to connect with wifi
  Serial.print("Connecting to ");
  Serial.print(WIFI_SSID);


  
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }

  
  Serial.println();
  Serial.print("Connected to ");
  Serial.println(WIFI_SSID);
  Serial.print("IP Address is : ");
  Serial.println(WiFi.localIP());                                            //print local IP address
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);   // connect to firebase
delay(1000);
}

void loop() { 

// Firebase Error Handling ************************************************
  float  tv = dht.readTemperature();
  float hv = dht.readHumidity();
  int sm= analogRead(Soil_moisture);
  if (Firebase.failed())
  { delay(500);
    Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
    Serial.println(Firebase.error());
  Serial.println("Connection to fiebase failed. Reconnecting...");
  delay(500);
  }
  
 else { 
    Serial.println("Everything is ready!");
    delay(300); Serial.println("Everything is ready!");
    delay(300); Serial.println("Everything is ready! \n \n \n");
    delay(300);


    Firebase.setInt("/temperature",tv);
   Serial.println(tv);
    delay(300); Serial.println("uploaded val to firebase \n \n \n");

      Firebase.setInt("/humidity",hv);
   Serial.println(hv);
    delay(300); Serial.println("uploaded val3 to firebase \n \n \n");
    Firebase.setInt("/Soil_moisture",sm);
    Serial.println("Value of soil_moisture :  " + sm);
    delay(300);
 } 
}
