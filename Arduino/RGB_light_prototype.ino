#include "config.h"

#if USE_ETHERNET
#include <SPI.h>
#include <Ethernet.h>
byte mac[] = { MAC_ADDRESS };
IPAddress ip(IP_ADDRESS);
#endif

#include "rgb.h"
#include "led.h"
#include "instructions.h"
#include "neopixel.h"

//LED led(RED_PIN, GREEN_PIN, BLUE_PIN);
NeoPixel neopixel(3, 12);

#include CONTROL_TYPE_SRC
CONTROL_TYPE controller;
//InstructionManager instMgr;

void setup() {
  Serial.begin(9600);
  Serial.setTimeout(50);
//  led.Init();
  neopixel.Init();
//  instMgr.SetInstruction(new TwoColourFade(0, 360));
}

void loop() {
  if(controller.IsAvailable()) 
    controller.ExecuteNeoPixel(neopixel);
//  instMgr.Update(led);
//  led.Update();
}


