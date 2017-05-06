#ifndef NEOPIXEL_H
#define NEOPIXEL_H

#include <Adafruit_NeoPixel.h>
#include "rgb.h"

#ifndef INT8
#define INT8 uint8_t
#endif // INT8

#ifndef INT16
#define INT16 uint16_t
#endif // INT16

#ifndef INT32
#define INT32 uint32_t
#endif // INT32

#ifndef ASFLOAT
#define ASFLOAT(var) ((float)var)
#endif

class NeoPixel {
public:
  NeoPixel(const int pin, const int pixelCount) : pin(pin), pixelCount(pixelCount),
    strip(pixelCount, pin, NEO_GRB + NEO_KHZ800) {}

  void Init() {
    strip.begin();
    strip.show();
  }

  // Helper functions
  INT32 BuildPixelColour(int r, int g, int b, float intensity = 1.0) {
    return strip.Color(r * intensity, g * intensity, b * intensity);
  }
  INT32 BuildPixelColour(int ar, int ag, int ab, int br, int bg, int bb, float intensity = 1.0) {
    int r = ar + (br - ar) * intensity;
    int g = ag + (bg - ag) * intensity;
    int b = ab + (bb - ab) * intensity;
    return strip.Color(r, g, b);
  }

  // Pixel functions
  
  void SetPixelColour(INT16 pixel, INT32 colour, INT8 wait) {
    strip.setPixelColor(pixel, colour);
    strip.show();
    delay(wait);
  }
  
  void SetRingColour(INT32 colour, INT8 wait) {
    for(INT16 i = 0; i < pixelCount; i++) {
      strip.setPixelColor(i, colour);
    }
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

  void ColourWorm(RGB colour, INT8 len, INT8 wait) {
    if(len <= 0) len = 1;
    float diff = 0.f;
    for(INT16 i = 0; i < pixelCount; i++) {
      this->SetPixelColour(i, BuildPixelColour(colour.r, colour.g, colour.b), 0);
      for(INT16 j = 0; j <= len; j++) {
        int pixel = (i - j);
        if(pixel < 0) continue;
        diff = 1.f - ASFLOAT(ASFLOAT(j) / ASFLOAT(len));
        this->SetPixelColour(pixel, BuildPixelColour(colour.r, colour.g, colour.b, diff), 0);
      }
      delay(wait);
    }
  }

  void ColourWormBG(RGB colour, RGB bgColour, INT8 len, INT8 wait, void* (*callback)(NeoPixel&) = nullptr) {
    if(len <= 0) len = 1;
    float diff = 0.f;
    for(INT16 i = 0; i < pixelCount; i++) {
      this->SetPixelColour(i, BuildPixelColour(colour.r, colour.g, colour.b), 0);
      for(INT16 j = 0; j <= len; j++) {
        int pixel = (i - j);
        if(pixel < 0) pixel += pixelCount;
        diff = ASFLOAT(ASFLOAT(j) / ASFLOAT(len));
        this->SetPixelColour(pixel, BuildPixelColour(colour.r, colour.g, colour.b, bgColour.r, bgColour.g, bgColour.b, diff), 0);
      }
      delay(wait);
    }
    if(callback != nullptr) 
      callback(*this);
  }

  void ColourWormBG_Stepped(RGB colour, RGB bgColour, INT8 len, INT8 wait, INT8 i) {
    if(len <= 0) len = 1;
    i %= pixelCount;
    float diff = 0.f;
    this->SetPixelColour(i, BuildPixelColour(colour.r, colour.g, colour.b), 0);
    for(INT16 j = 0; j <= len; j++) {
      int pixel = (i - j);
      if(pixel < 0) pixel += pixelCount;
      diff = ASFLOAT(ASFLOAT(j) / ASFLOAT(len));
      this->SetPixelColour(pixel, BuildPixelColour(colour.r, colour.g, colour.b, bgColour.r, bgColour.g, bgColour.b, diff), 0);
    }
    delay(wait);
  }

  void DualWipe(INT32 colourA, INT32 colourB, INT8 wait) {
    int dualOffset = pixelCount * 0.5f;
    for(INT16 i = 0; i < pixelCount; i++) {
      this->SetPixelColour(i, colourA, 0);
      this->SetPixelColour((i + dualOffset) % pixelCount, colourB, 0);
      delay(wait);
    }
  }

  void DualWipe_Stepped(INT32 colourA, INT32 colourB, INT8 wait, INT8 i) {
    i %= pixelCount;
    int dualOffset = pixelCount * 0.5f;
    this->SetPixelColour(i, colourA, 0);
    this->SetPixelColour((i + dualOffset) % pixelCount, colourB, 0);
    delay(wait);
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

  void TriWipe_Stepped(INT32 colourA, INT32 colourB, INT32 colourC, INT8 wait, INT8 i) {
    i %= pixelCount;
    int triOffsetA = pixelCount / 3;
    int triOffsetB = triOffsetA * 2;
    this->SetPixelColour(i, colourA, 0);
    this->SetPixelColour((i + triOffsetA) % pixelCount, colourB, 0);
    this->SetPixelColour((i + triOffsetB) % pixelCount, colourC, 0);
    delay(wait);
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

  void QuadWipe_Stepped(INT32 colourA, INT32 colourB, INT32 colourC, INT32 colourD, INT8 wait, INT8 i) {
    i %= pixelCount;
    int quadOffsetA = pixelCount  * 0.25f;
    int quadOffsetB = quadOffsetA * 2;
    int quadOffsetC = quadOffsetA * 3;
    this->SetPixelColour(i, colourA, 0);
    this->SetPixelColour((i + quadOffsetA) % pixelCount, colourB, 0);
    this->SetPixelColour((i + quadOffsetB) % pixelCount, colourC, 0);
    this->SetPixelColour((i + quadOffsetC) % pixelCount, colourD, 0);
    delay(wait);
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
