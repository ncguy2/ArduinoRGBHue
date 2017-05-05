#ifndef SERIALCONTROL_H
#define SERIALCONTROL_H

#include "neopixel.h"
#include "commands.h"

#define INPUT_SIZE 50

class SerialControl {
public:
  
  bool IsAvailable() {
    return Serial.available() > 0;
  }  
  void ExecuteNeoPixel(NeoPixel& neopixel) {
    char input[INPUT_SIZE + 1];
    byte size = Serial.readBytes(input, INPUT_SIZE);
    input[size] = 0;

    int i = 0;
    char *pch=strchr(input,':');
    while (pch!=NULL) {
      i++;
      pch=strchr(pch+1,':');
    }

    String cmds(input);
    cmdMgr.DetermineAndExecute(cmds, neopixel);
  }
  void Execute(LED& led) {
    char input[INPUT_SIZE + 1];
    byte size = Serial.readBytes(input, INPUT_SIZE);
    input[size] = 0;

    int i = 0;
    char *pch=strchr(input,':');
    while (pch!=NULL) {
      i++;
      pch=strchr(pch+1,':');
    }

    String cmds(input);
  }

protected:
  CommandManager cmdMgr;
};

#endif // SERIALCONTROL_H
