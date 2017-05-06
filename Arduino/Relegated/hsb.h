#ifndef HSB_H
#define HSB_H

#include "rgb.h"

struct HSB {
  int h;
  float s;
  float b;

  void Set(int hue) {
    this->h = hue % 360;
  }
};

#endif // HSB_H
