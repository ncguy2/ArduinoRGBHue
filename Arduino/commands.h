#ifndef COMMANDS_H
#define COMMANDS_H

#include <StandardCplusplus.h>
#include <map>
#include <memory>
#include "neopixel.h"

#define INPUT_SIZE 50

#ifndef CMD_PROTO
#define CMD_PROTO String args, NeoPixel& neopixel, CommandManager& mgr
#endif

#ifndef BUILD_COLOUR
#define BUILD_COLOUR(r, g, b) neopixel.BuildPixelColour(r, g, b)
#endif

template <typename T>
struct FuncWrapper {
  void* (*func)(String, NeoPixel&, T&);
};

class CommandManager {
public:
  CommandManager() {
    RegisterCommand("cmds", [](CMD_PROTO) {
      Serial.println("Registered Commands: ");
      for(auto cmd : mgr.cmds) {
        Serial.println(cmd.first);
      }
    });
    RegisterCommand("set", [](CMD_PROTO) {
      int id = atoi(mgr.GetValue(args, ' ', 1).c_str());
      int r = atoi(mgr.GetValue(args, ' ', 2).c_str());
      int g = atoi(mgr.GetValue(args, ' ', 3).c_str());
      int b = atoi(mgr.GetValue(args, ' ', 4).c_str());
      int wait = atoi(mgr.GetValue(args, ' ', 5, "50").c_str());
      Serial.print("Setting pixel at ");
      Serial.print(id);
      Serial.print(" to colour [");
      Serial.print(r);
      Serial.print(", ");
      Serial.print(g);
      Serial.print(", ");
      Serial.print(b);
      Serial.println("]");
      neopixel.SetPixelColour(id, r, g, b, wait);
    });
    RegisterCommand("wipe", [](CMD_PROTO) {
      int r = atoi(mgr.GetValue(args, ' ', 1).c_str());
      int g = atoi(mgr.GetValue(args, ' ', 2).c_str());
      int b = atoi(mgr.GetValue(args, ' ', 3).c_str());
      int wait = atoi(mgr.GetValue(args, ' ', 4, "50").c_str());
      neopixel.ColourWipe(BUILD_COLOUR(r, g, b), wait);
    });
    RegisterCommand("wipe-offset", [](CMD_PROTO) {
      int offset = atoi(mgr.GetValue(args, ' ', 1).c_str());
      int r = atoi(mgr.GetValue(args, ' ', 2).c_str());
      int g = atoi(mgr.GetValue(args, ' ', 3).c_str());
      int b = atoi(mgr.GetValue(args, ' ', 4).c_str());
      int wait = atoi(mgr.GetValue(args, ' ', 5, "50").c_str());
      neopixel.ColourWipeOffset(BUILD_COLOUR(r, g, b), offset, wait);
    });
    RegisterCommand("dual-wipe", [](CMD_PROTO) {
      int ar = atoi(mgr.GetValue(args, ' ', 1).c_str());
      int ag = atoi(mgr.GetValue(args, ' ', 2).c_str());
      int ab = atoi(mgr.GetValue(args, ' ', 3).c_str());
      int br = atoi(mgr.GetValue(args, ' ', 4).c_str());
      int bg = atoi(mgr.GetValue(args, ' ', 5).c_str());
      int bb = atoi(mgr.GetValue(args, ' ', 6).c_str());
      int wait = atoi(mgr.GetValue(args, ' ', 7, "50").c_str());
      neopixel.DualWipe(BUILD_COLOUR(ar, ag, ab), BUILD_COLOUR(br, bg, bb), wait);
    });
    RegisterCommand("tri-wipe", [](CMD_PROTO) {
      int ar = atoi(mgr.GetValue(args, ' ', 1).c_str());
      int ag = atoi(mgr.GetValue(args, ' ', 2).c_str());
      int ab = atoi(mgr.GetValue(args, ' ', 3).c_str());
      int br = atoi(mgr.GetValue(args, ' ', 4).c_str());
      int bg = atoi(mgr.GetValue(args, ' ', 5).c_str());
      int bb = atoi(mgr.GetValue(args, ' ', 6).c_str());
      int cr = atoi(mgr.GetValue(args, ' ', 7).c_str());
      int cg = atoi(mgr.GetValue(args, ' ', 8).c_str());
      int cb = atoi(mgr.GetValue(args, ' ', 9).c_str());
      int wait = atoi(mgr.GetValue(args, ' ', 10, "50").c_str());
      neopixel.TriWipe(BUILD_COLOUR(ar, ag, ab), BUILD_COLOUR(br, bg, bb), BUILD_COLOUR(cr, cg, cb), wait);
    });
    RegisterCommand("quad-wipe", [](CMD_PROTO) {
      int ar = atoi(mgr.GetValue(args, ' ', 1).c_str());
      int ag = atoi(mgr.GetValue(args, ' ', 2).c_str());
      int ab = atoi(mgr.GetValue(args, ' ', 3).c_str());
      int br = atoi(mgr.GetValue(args, ' ', 4).c_str());
      int bg = atoi(mgr.GetValue(args, ' ', 5).c_str());
      int bb = atoi(mgr.GetValue(args, ' ', 6).c_str());
      int cr = atoi(mgr.GetValue(args, ' ', 7).c_str());
      int cg = atoi(mgr.GetValue(args, ' ', 8).c_str());
      int cb = atoi(mgr.GetValue(args, ' ', 9).c_str());
      int dr = atoi(mgr.GetValue(args, ' ', 10).c_str());
      int dg = atoi(mgr.GetValue(args, ' ', 11).c_str());
      int db = atoi(mgr.GetValue(args, ' ', 12).c_str());
      int wait = atoi(mgr.GetValue(args, ' ', 13, "50").c_str());
      neopixel.QuadWipe(BUILD_COLOUR(ar, ag, ab), BUILD_COLOUR(br, bg, bb), BUILD_COLOUR(cr, cg, cb), BUILD_COLOUR(dr, dg, db), wait);
    });
  }

  void RegisterCommand(String key, void*(*cmd)(String, NeoPixel&, CommandManager&)) {
    cmds[key] = FuncWrapper<CommandManager>{cmd};
  }

  void DetermineAndExecute(String cmdString, NeoPixel& neopixel) {
    String cmd = GetValue(cmdString, ' ', 0);
    if(cmds.find(cmd) != cmds.end()) {
      cmds[cmd].func(cmdString, neopixel, *this);
    }else{
      Serial.print(cmd);
      Serial.println(" not registered");
    }
  }
  
  String GetValue(String data, char separator, int index, String defVal = ""){
    int found = 0;
    int strIndex[] = {0, -1};
    int maxIndex = data.length()-1;

    for(int i=0; i<=maxIndex && found<=index; i++){
      if(data.charAt(i)==separator || i==maxIndex){
        found++;
        strIndex[0] = strIndex[1]+1;
        strIndex[1] = (i == maxIndex) ? i+1 : i;
      }
    }

    return found>index ? data.substring(strIndex[0], strIndex[1]) : defVal;
  }

  std::map<String, FuncWrapper<CommandManager>> cmds;
};

#endif // COMMANDS_H
