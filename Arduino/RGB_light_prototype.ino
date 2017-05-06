#include "config.h"

#if USE_ETHERNET
#include <SPI.h>
#include <Ethernet.h>
byte mac[] = { MAC_ADDRESS };
IPAddress ip(IP_ADDRESS);
#endif

#include "neopixel.h"
#include "instructions.h"

NeoPixel neopixel(NEOPIXEL_PIN, NEOPIXEL_PIXELS);

#include CONTROL_TYPE_SRC
CONTROL_TYPE controller;

void setup() {
  Serial.begin(9600);
  Serial.setTimeout(SERIAL_TIMEOUT);
  neopixel.Init();
}

void loop() {
  if(controller.IsAvailable()) 
    controller.ExecuteNeoPixel(neopixel);
  InstructionManager::GetInstance().Update(neopixel);
}


