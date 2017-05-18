#ifndef RGB_H
#define RGB_H

#include <Arduino.h>

struct RGB {
  RGB() : RGB(0) {}
  RGB(long colour) {
    (*this) = colour;
  }
  RGB(int r, int g, int b) : r(r), g(g), b(b) {}
  
  byte r;
  byte g;
  byte b;

  void Set(long col) {
    r = (col >> 16) & 0x0FF;
    g = (col >> 8) & 0x0FF;
    b = col & 0x0FF;
  }

  RGB operator=(long col) {
    Set(col);
    return *this;
  }

    RGB operator*=(float scalar) {
      r *= scalar;
      g *= scalar;
      b *= scalar;
      return  *this;
    }

  long ToLong() {
    long rgb = r;
    rgb = (rgb << 8) + g;
    rgb = (rgb << 8) + b;
    return rgb;
  }
};

#endif // RGB_H
