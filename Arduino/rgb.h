#ifndef RGB_H
#define RGB_H

#include <Arduino.h>
#include <RGBConverter.h>
#include "hsb.h"

static RGBConverter converter;

struct RGB {
  RGB() : RGB(0) {}
  RGB(long colour) {
    (*this) = colour;
  }
  int r;
  int g;
  int b;

  void Set(long col) {
    r = (col >> 16) & 0x0FF;
    g = (col >> 8) & 0x0FF;
    b = col & 0x0FF;
    Serial.print("Setting colour to: ");
    Serial.println(col);
  }

  RGB operator=(long col) {
    Set(col);
    return *this;
  }

  long ToLong() {
    long rgb = r;
    rgb = (rgb << 8) + g;
    rgb = (rgb << 8) + b;
    return rgb;
//    return ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
  }

  void FromHSB(HSB hsb) {
    byte rgb[3];
    converter.hslToRgb(hsb.h / 360.f, hsb.s / 100.f, hsb.b / 100.f, rgb);
    this->r = rgb[0];
    this->g = rgb[1];
    this->b = rgb[2];
  }
};

#endif // RGB_H
