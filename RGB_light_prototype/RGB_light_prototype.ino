#include "config.h"

#include "neopixel.h"
#include "instructions.h"

NeoPixel neopixel(NEOPIXEL_PIN, NEOPIXEL_PIXELS);

#include CONTROL_TYPE_SRC
CONTROL_TYPE controller(CONTROL_TYPE_ARGS);

void setup() {
  Serial.begin(9600);
  Serial.setTimeout(SERIAL_TIMEOUT);
  neopixel.Init();
  controller.Init();
}

void loop() {
  if(controller.IsAvailable()) 
    controller.ExecuteNeoPixel(neopixel);
  InstructionManager::GetInstance().Update(neopixel);
}


