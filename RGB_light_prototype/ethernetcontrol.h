#ifndef ETHERNETCONTROL_H
#define ETHERNETCONTROL_H

#include <Arduino.h>
#include "neopixel.h"
#include "rgb.h"

#include <SPI.h>
#include <Ethernet.h>
#include <EthernetUdp.h>
byte mac[] = { MAC_ADDRESS };
IPAddress ip(IP_ADDRESS);
IPAddress myDns(DNS_ADDRESS);
IPAddress gateway(GATEWAY_ADDRESS);
IPAddress subnet(SUBNET_ADDRESS);

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
#define CMD_TWIN_WIPE_SINGLE 9
#define CMD_TWIN_WIPE 10

#define CMD_INSTRUCTION_START 30
#define CMD_INSTRUCTION_WORM 31
#define CMD_INSTRUCTION_DUALWIPE 32
#define CMD_INSTRUCTION_TRIWIPE 33
#define CMD_INSTRUCTION_QUADWIPE 34
#define CMD_INSTRUCTION_BREATHING 35
#define CMD_INSTRUCTION_END 36

#define CMD_DISCOVERY 127

#endif

struct CommandPayload {
  byte commandId;
  RGB colours[4];
  unsigned int wait;
};

template<typename T, typename U>
struct pair {
    T first;
    U second;
};

class EthernetControl {
public:
    #if USE_UDP
  EthernetControl(String cellName) : cellName(cellName), svr() {}
    #else
    EthernetControl(String cellName) : cellName(cellName), svr(ETHERNET_PORT) {}
    #endif
  void Init() {
    pinMode(DEBUG_DHCP_SUCCESS, OUTPUT);
    pinMode(DEBUG_DHCP_WORKING, OUTPUT);
    pinMode(DEBUG_DHCP_FAIL, OUTPUT);



    digitalWrite(DEBUG_DHCP_SUCCESS, LOW);
    digitalWrite(DEBUG_DHCP_WORKING, HIGH);
    digitalWrite(DEBUG_DHCP_FAIL, LOW);
    // DHCP
    Serial.println("Attempting to connect through DHCP");
    if(Ethernet.begin(mac) == 0) {
      OnDHCPConnectionFail();
    }else OnDHCPConnectionSuccess();
    #if USE_UDP
    svr.begin(ETHERNET_PORT);
    #else
    svr.begin();
    #endif
    Serial.print("Server address: ");
    Serial.println(Ethernet.localIP());
  }

  void OnDHCPConnectionSuccess() {
    digitalWrite(DEBUG_DHCP_SUCCESS, HIGH);
    digitalWrite(DEBUG_DHCP_WORKING, LOW);
    digitalWrite(DEBUG_DHCP_FAIL, LOW);
  }
  void OnDHCPConnectionFail() {
    Serial.println("Failed to configure Ethernet using DHCP");
    Ethernet.begin(mac, ip, myDns, gateway, subnet);
    Serial.println("Using default network settings");

    digitalWrite(DEBUG_DHCP_SUCCESS, LOW);
    digitalWrite(DEBUG_DHCP_WORKING, LOW);
    digitalWrite(DEBUG_DHCP_FAIL, HIGH);
  }

    bool IsAvailable() {
    this->request = "";

    #if USE_UDP
    int packetSize = svr.parsePacket();
    if(packetSize <= 0) return false;
    this->request = svr.readStringUntil('\0');
    #else
    EthernetClient client = svr.available();
    if(!client) return false;
    this->request = ReadRequest(&client);
    #endif
    this->payload = AssembleCommandPayload(this->request);
    bool consumed = ExecuteRequest();
    if(consumed)
      Serial.println("Request consumed by system, command ID: " + this->payload.commandId);
    return !consumed;

  }
  /**
   * @returns Whether the request was consumed
   */
  bool ExecuteRequest() {
    bool isDiscovery = this->payload.commandId == CMD_DISCOVERY;
    Serial.print("Debug: ");
    Serial.print(this->payload.commandId);
    Serial.print(" == ");
    Serial.print(CMD_DISCOVERY);
    Serial.print(" = ");
    Serial.println(isDiscovery);
    if(isDiscovery) {
      Serial.print("Discovery packet received ");
      IPAddress remote = svr.remoteIP();
      int remotePort = svr.remotePort();
      Serial.print("from ");
      Serial.print(remote);
      Serial.print(":");
      Serial.println(remotePort);
      Serial.println("DISCOVERY_RESPONSE");
      svr.beginPacket(remote, remotePort);
      svr.write("DISCOVERY_RESPONSE");
      int result = svr.endPacket();
      Serial.print("Response status: ");
      Serial.print(result == 1 ? "Success" : "Failed");
      Serial.print(" [");
      Serial.print(result);
      Serial.println("]");
      return true;
    }
    return false;
  }

  void ExecuteNeoPixel(NeoPixel& neopixel) {
    Serial.print("Command ID: ");
    Serial.println(payload.commandId);
    if(payload.commandId < CMD_INSTRUCTION_START || payload.commandId > CMD_INSTRUCTION_END)
      InstructionManager::GetInstance().ClearInstruction();
    switch(payload.commandId) {
      case CMD_SET:
        neopixel.SetRingColour(payload.colours[0].ToLong(), payload.wait);
        break;
      case CMD_PULSE:
        neopixel.SetRingColour(payload.colours[0].ToLong(), payload.wait);
        neopixel.SetRingColour(payload.colours[1].ToLong(), 0);
        break;
      case CMD_SWEEP:
        for(int i = 0; i < NEOPIXEL_PIXELS; i++)
          neopixel.SetPixelColour(i, payload.colours[0].ToLong(), payload.wait);
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
      case CMD_TWIN_WIPE_SINGLE:
        neopixel.TwinFill_Same(payload.colours[0].ToLong(), payload.wait);
        break;
      case CMD_TWIN_WIPE:
        neopixel.TwinFill(payload.colours[0].ToLong(), payload.colours[1].ToLong(), payload.wait);
        break;
      case CMD_INSTRUCTION_WORM:
        InstructionManager::GetInstance().SetInstruction(new WormSpinInstruction(payload.colours[0].ToLong(), payload.colours[1].ToLong(), payload.colours[2].r, payload.wait));
        break;
      case CMD_INSTRUCTION_DUALWIPE:
        InstructionManager::GetInstance().SetInstruction(new WipeInstruction(payload.colours[0].ToLong(), payload.colours[1].ToLong(), payload.wait));
        break;
      case CMD_INSTRUCTION_TRIWIPE:
        InstructionManager::GetInstance().SetInstruction(new TriWipeInstruction(payload.colours[0].ToLong(), payload.colours[1].ToLong(), payload.colours[2].ToLong(), payload.wait));
        break;
      case CMD_INSTRUCTION_QUADWIPE:
        InstructionManager::GetInstance().SetInstruction(new QuadWipeInstruction(payload.colours[0].ToLong(), payload.colours[1].ToLong(), payload.colours[2].ToLong(), payload.colours[3].ToLong(), payload.wait));
        break;
      case CMD_INSTRUCTION_BREATHING:
        InstructionManager::GetInstance().SetInstruction(new BreathingInstruction(payload.colours[0], payload.colours[1].r));
        break;
      default:
        Serial.print("Unrecognised command, ID: ");
        Serial.println(payload.commandId);
      break;
    }
  }

  CommandPayload AssembleCommandPayload(String& request) {
    unsigned int bufSize = request.length();
    int buf[bufSize];
    byte byteBuf[bufSize];
    request.getBytes(byteBuf, bufSize);

    for(int i = 0; i < bufSize; i++)
      buf[i] = byteBuf[i];

    CommandPayload payload;
    payload.commandId = (byte)buf[0];
    payload.colours[0] = RGB(buf[1] ,  buf[2] ,  buf[3] );
    payload.colours[1] = RGB(buf[4] ,  buf[5] ,  buf[6] );
    payload.colours[2] = RGB(buf[7] ,  buf[8] ,  buf[9] );
    payload.colours[3] = RGB(buf[10] , buf[11] , buf[12] );

    for(int i = 0; i < 4; i++) {
      payload.colours[i].r -= 1;
      payload.colours[i].g -= 1;
      payload.colours[i].b -= 1;
    }

    payload.wait = 50;

  #if VERBOSE_LOG
    Serial.print("Request: ");
    Serial.println(request);
    Serial.println("Buffer:");
    for(int i = 0; i < bufSize; i++) {
      Serial.print(i);
      Serial.print(": ");
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
  #endif
    return payload;
  }

    #if !USE_UDP
  String ReadRequest(EthernetClient* client) {
    String request = "";
    while(client->connected()) {
      while(client->available()) {
        char c = client->read();
        if(c == '\0') return request;
        request += c;
      }
    }
    return request;
  }
    #endif
  
protected:
  long lastSent;
  String cellName;
  String request;
  #if USE_UDP
  EthernetUDP svr;
  #else
  EthernetServer svr;
  #endif

  CommandPayload payload;
};

#endif // ETHERNETCONTROL_H
