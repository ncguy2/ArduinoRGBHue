#ifndef ETHERNETCONTROL_H
#define ETHERNETCONTROL_H

#include <Arduino.h>
#include "neopixel.h"
#include "rgb.h"

#include <SPI.h>
#include <Ethernet.h>
byte mac[] = { MAC_ADDRESS };
//IPAddress ip(IP_ADDRESS);

#ifndef COMMAND_IDS
#define COMMAND_IDS

#define CMD_SET 1
#define CMD_PULSE 2
#define CMD_PULSEMASK 3
#define CMD_SWEEP 4
#define CMD_WIPE 5
#define CMD_DUALWIPE 6
#define CMD_TRIWIPE 7
#define CMD_QUADWIPE 8

#endif

struct CommandPayload {
  byte commandId;
  RGB colours[4];
  unsigned int wait;
  bool pinMask[NEOPIXEL_PIXELS];
  int pins[NEOPIXEL_PIXELS];
  int maskedPins = 0;
};

template<typename T, typename U>
struct pair {
    T first;
    U second;
};

class EthernetControl {
public:
  EthernetControl(String cellName) : cellName(cellName), svr(ETHERNET_PORT) {}
  void Init() {
    // DHCP
    Serial.println("Attempting to connect through DHCP");
    if(Ethernet.begin(mac) == 0) {
      Serial.println("Failed to configure Ethernet using DHCP");
      while(true);
    }
    svr.begin();
    Serial.print("Server address: ");
    Serial.println(Ethernet.localIP());
  }

  bool IsAvailable() {
    this->request = "";
    EthernetClient client = svr.available();
    if(!client) return false;
    this->request = ReadRequest(&client);
    Serial.println("REQUEST_RECEIVED");
    Serial.println(this->request);
    this->payload = AssembleCommandPayload(this->request);
    return !ExecuteRequest(&client);
  }  
  /**
   * @returns Whether the request was consumed
   */
  bool ExecuteRequest(EthernetClient* client) {
    
    return false;
  }

  int& MaskToValueSize(CommandPayload& payload) {
    int size = 0;
    for(int i = 0; i < NEOPIXEL_PIXELS; i++) {
      if(payload.pinMask[i])
        size++;
    }
    return size;
  }

  INT8* MaskToValues(CommandPayload& payload, int size) {
    uint8_t values[size];
    int index = 0;
    for(int i = 0; i < NEOPIXEL_PIXELS; i++) {
      if(payload.pinMask[i])
        values[index++] = i;
    }
    return values;
  }

  void ExecuteNeoPixel(NeoPixel& neopixel) {
    switch(payload.commandId) {
      case CMD_SET:
        for(int i = 0; i < payload.maskedPins; i++)
          neopixel.SetPixelColour_fast(payload.pins[i], payload.colours[0].ToLong());
        neopixel.ShowRing(payload.wait);
        break;
      case CMD_PULSE:
        neopixel.SetRingColour(payload.colours[0].ToLong(), payload.wait);
        neopixel.SetRingColour(payload.colours[1].ToLong(), 0);
        break;
      case CMD_PULSEMASK:
        for(int i = 0; i < payload.maskedPins; i++)
          neopixel.SetPixelColour_fast(payload.pins[i], payload.colours[0].ToLong());
        neopixel.ShowRing(payload.wait);
        for(int i = 0; i < payload.maskedPins; i++)
          neopixel.SetPixelColour_fast(payload.pins[i], payload.colours[1].ToLong());
        neopixel.ShowRing(0);
        break;
      case CMD_SWEEP:
        for(int i = 0; i < payload.maskedPins; i++)
          neopixel.SetPixelColour(payload.pins[i], payload.colours[0].ToLong(), payload.wait);
        break;
      case CMD_WIPE:
        neopixel.ColourWipe(payload.colours[0].ToLong(), payload.wait);
        break;
      case CMD_DUALWIPE:
        neopixel.DualWipe(payload.colours[0].ToLong(), payload.colours[1].ToLong(), payload.wait);
        break;
      case CMD_TRIWIPE:
        neopixel.TriWipe(payload.colours[0].ToLong(), payload.colours[1].ToLong(), payload.colours[2].ToLong(), payload.wait);
        break;
      case CMD_QUADWIPE:
        neopixel.QuadWipe(payload.colours[0].ToLong(), payload.colours[1].ToLong(), payload.colours[2].ToLong(), payload.colours[3].ToLong(), payload.wait);
        break;
      default:
        Serial.print("Unrecognised command, ID: ");
        Serial.println(payload.commandId);
      break;
    }
  }

  CommandPayload AssembleCommandPayload(String& request) {
    unsigned int bufSize = request.length();
    int buf[bufSize+1];
    char byteBuf[bufSize+1];
    request.toCharArray(byteBuf, bufSize+1);

    for(int i = 0; i < bufSize; i++)
      buf[i] = byteBuf[i];

    CommandPayload payload;
    payload.commandId = (byte)buf[0];
    payload.colours[0] = RGB(buf[1] - 1,  buf[2] - 1,  buf[3] - 1);
    payload.colours[1] = RGB(buf[4] - 1,  buf[5] - 1,  buf[6] - 1);
    payload.colours[2] = RGB(buf[7] - 1,  buf[8] - 1,  buf[9] - 1);
    payload.colours[3] = RGB(buf[10] - 1, buf[11] - 1, buf[12] - 1);

    payload.wait = 50;

    for(int i = 0; i < NEOPIXEL_PIXELS; i++) {
      payload.pinMask[i] = ((char) buf[13 + i]) == '1';
      if(payload.pinMask[i]) {
        payload.pins[payload.maskedPins++] = i;
      }
    }

  #if VERBOSE_LOG
    Serial.println("Buffer:");
    for(int i = 0; i < bufSize; i++) {
      Serial.print("  ");
      Serial.print((byte)buf[i]);
      Serial.print(" / ");
      Serial.println((char)buf[i]);
    }

    Serial.println("Command assembled: ");
    Serial.print("    ID: ");
    Serial.print(buf[0]);
    Serial.print(" / ");
    Serial.println(payload.commandId);
    Serial.print("    Wait: ");
    Serial.println(payload.wait);
    Serial.println("    Colours: ");
    Serial.print("        1: ");
    Serial.print(buf[1]);
    Serial.print(",");
    Serial.print(buf[2]);
    Serial.print(",");
    Serial.print(buf[3]);
    Serial.print(" / ");
    Serial.println(payload.colours[0].ToLong());
    Serial.print("        2: ");
    Serial.print(buf[4]);
    Serial.print(",");
    Serial.print(buf[5]);
    Serial.print(",");
    Serial.print(buf[6]);
    Serial.print(" / ");
    Serial.println(payload.colours[1].ToLong());
    Serial.print("        3: ");
    Serial.print(buf[7]);
    Serial.print(",");
    Serial.print(buf[8]);
    Serial.print(",");
    Serial.print(buf[9]);
    Serial.print(" / ");
    Serial.println(payload.colours[2].ToLong());
    Serial.print("        4: ");
    Serial.print(buf[10]);
    Serial.print(",");
    Serial.print(buf[11]);
    Serial.print(",");
    Serial.print(buf[12]);
    Serial.print(" / ");
    Serial.println(payload.colours[3].ToLong());
    Serial.println("    PinMask: ");
    for(int i = 0; i < NEOPIXEL_PIXELS; i++) {
      Serial.print("        ");
      Serial.print(i);
      Serial.print(": ");
      Serial.print(buf[13 + i]);
      Serial.print(" / ");
      Serial.println(payload.pinMask[i]);
    }
  #endif
    return payload;
  }

  String ReadRequest(EthernetClient* client) {
    String request = "";
    while(client->connected()) {
      while(client->available()) {
        char c = client->read();
        if(c == '\n') return request;
        request += c;
      }
    }
    return request;
  }
  
protected:
  long lastSent;
  String cellName;
  String request;
  EthernetServer svr;
  CommandPayload payload;
};

#endif // ETHERNETCONTROL_H
