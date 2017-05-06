#ifndef SERIALCONTROL_H
#define SERIALCONTROL_H

#include "neopixel.h"
#include "commands.h"

#define INPUT_SIZE 255

class SerialControl {
public:
  bool IsAvailable() {
    return Serial.available() > 0;
  }  
  void ExecuteNeoPixel(NeoPixel& neopixel) {
//    char input[INPUT_SIZE + 1];
//    byte size = Serial.readBytes(input, INPUT_SIZE);
//    input[size] = 0;

    String cmds(Serial.readString());

    int i = 0;
    String val = cmdMgr.GetValue(cmds, ';', i++, "");
    while(val.length() > 0){
      cmdMgr.DetermineAndExecute(val, neopixel);
      val = cmdMgr.GetValue(cmds, ';', i++, "");
    }
  }

protected:
  CommandManager cmdMgr;
};

#endif // SERIALCONTROL_H
