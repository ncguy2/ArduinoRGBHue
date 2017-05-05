#ifndef NEOPIXEL_H
#define NEOPIXEL_H

#include <Adafruit_NeoPixel.h>
#include "rgb.h"
#include "hsb.h"
#include "interp.h"
#include <StandardCplusplus.h>
#include <vector>

#ifndef INT8
#define INT8 uint8_t
#endif // INT8

#ifndef INT16
#define INT16 uint16_t
#endif // INT16

#ifndef INT32
#define INT32 uint32_t
#endif // INT32

class NeoPixel {
public:
  NeoPixel(const int pin, const int pixelCount) : pin(pin), pixelCount(pixelCount),
    strip(pixelCount, pin, NEO_GRB + NEO_KHZ800) {}

  void Init() {
    strip.begin();
    strip.show();
  }

  // Helper functions
  INT32 BuildPixelColour(int r, int g, int b) {
    return strip.Color(r, g, b);
  }

  // Pixel functions
  
  void SetPixelColour(INT16 pixel, INT32 colour, INT8 wait) {
    strip.setPixelColor(pixel, colour);
    strip.show();
    delay(wait);
  }

  void SetPixelColour(INT16 pixel, int r, int g, int b, INT8 wait) {
    SetPixelColour(pixel, BuildPixelColour(r, g, b), wait);
  }

  // Wheel functions
  void ColourWipe(INT32 colour, INT8 wait) {
    for(INT16 i = 0; i < pixelCount; i++) {
      this->SetPixelColour(i, colour, wait);
    }
  }

  void ColourWipeOffset(INT32 colour, INT8 offset, INT8 wait) {
    for(INT16 i = 0; i < pixelCount; i++) {
      this->SetPixelColour((i + offset) % pixelCount, colour, wait);
    }
  }

  void DualWipe(INT32 colourA, INT32 colourB, INT8 wait) {
    int dualOffset = pixelCount * 0.5f;
    for(INT16 i = 0; i < pixelCount; i++) {
      this->SetPixelColour(i, colourA, 0);
      this->SetPixelColour((i + dualOffset) % pixelCount, colourB, 0);
      delay(wait);
    }
  }

  void TriWipe(INT32 colourA, INT32 colourB, INT32 colourC, INT8 wait) {
    int triOffsetA = pixelCount / 3;
    int triOffsetB = triOffsetA * 2;
    
    for(INT16 i = 0; i < pixelCount; i++) {
      this->SetPixelColour(i, colourA, 0);
      this->SetPixelColour((i + triOffsetA) % pixelCount, colourB, 0);
      this->SetPixelColour((i + triOffsetB) % pixelCount, colourC, 0);
      delay(wait);
    }
  }

  void QuadWipe(INT32 colourA, INT32 colourB, INT32 colourC, INT32 colourD, INT8 wait) {
    int quadOffsetA = pixelCount  * 0.25f;
    int quadOffsetB = quadOffsetA * 2;
    int quadOffsetC = quadOffsetA * 3;
    
    for(INT16 i = 0; i < pixelCount; i++) {
      this->SetPixelColour(i, colourA, 0);
      this->SetPixelColour((i + quadOffsetA) % pixelCount, colourB, 0);
      this->SetPixelColour((i + quadOffsetB) % pixelCount, colourC, 0);
      this->SetPixelColour((i + quadOffsetC) % pixelCount, colourD, 0);
      delay(wait);
    }
  }

  void Rainbow(INT8 wait) {
    INT16 i, j;

    for(j = 0; j < 256; j++) {
      for(i = 0; i < pixelCount; i++) {
        strip.setPixelColor(i, Wheel((i+j) & 255));
      }
      strip.show();
      delay(wait);
    }
  }

  void RainbowCycle(INT8 wait) {
    INT16 i, j;
    for(j = 0; j < 256 * 5; j++) {
      for(i = 0; i < pixelCount; i++) {
        strip.setPixelColor(i, Wheel(((i * 256 / pixelCount) + j) & 255));
      }
      strip.show();
      delay(wait);
    }
  }

  void TheaterChase(INT32 colour, INT8 wait) {
    for (int j = 0; j<10; j++) {  //do 10 cycles of chasing
      for (int q = 0; q < 3; q++) {
        for (INT16 i = 0; i < pixelCount; i+=3) {
          strip.setPixelColor(i + q, colour);    //turn every third pixel on
        }
        strip.show();

        delay(wait);

        for (INT16 i = 0; i < pixelCount; i+=3) {
          strip.setPixelColor(i + q, 0);        //turn every third pixel off
        }
      }
    }
  }

  void theaterChaseRainbow(INT8 wait) {
    for (int j = 0; j < 256; j++) {     // cycle all 256 colors in the wheel
      for (int q = 0; q < 3; q++) {
        for (INT16 i=0; i < pixelCount; i+=3) {
          strip.setPixelColor(i + q, Wheel((i + j) % 255));    //turn every third pixel on
        }
        strip.show();

        delay(wait);

        for (INT16 i = 0; i < pixelCount; i+=3) {
          strip.setPixelColor(i + q, 0);        //turn every third pixel off
        }
      }
    }
  }

  INT32 Wheel(byte wheelPos) {
    wheelPos = 255 - wheelPos;
    if(wheelPos < 85) {
      return strip.Color(255 - wheelPos * 3, 0, wheelPos * 3);
    }
    if(wheelPos < 170) {
      wheelPos -= 85;
      return strip.Color(0, wheelPos * 3, 255 - wheelPos * 3);
    }
    wheelPos -= 170;
    return strip.Color(wheelPos * 3, 255 - wheelPos * 3, 0);
  }
    
protected:
  const int pin;
  const int pixelCount;
  const Adafruit_NeoPixel strip;
};

#endif // NEOPIXEL_H
