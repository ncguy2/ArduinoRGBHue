#ifndef CONFIG_H
#define CONFIG_H

#define CONTROL_TYPE EthernetControl
#define CONTROL_TYPE_SRC "ethernetcontrol.h"
#define CONTROL_TYPE_ARGS "RGBHue"
#define USE_ETHERNET false
#define MAC_ADDRESS 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED
#define IP_ADDRESS 169, 254, 177, 139
#define DNS_ADDRESS 169, 254, 1, 1
#define GATEWAY_ADDRESS 169, 254, 1, 1
#define SUBNET_ADDRESS 255, 255, 0, 0
#define NEOPIXEL_PIN 3
#define NEOPIXEL_PIXELS 12
#define SERIAL_UPDATE_INTERVAL 50
#define INSTRUCTION_UPDATE_INTERVAL 33
#define SERIAL_TIMEOUT 25
#define ETHERNET_PORT 3300
#define MAX_BUFFER_SIZE 256
#define VERBOSE_LOG true

#endif // CONFIG_H
