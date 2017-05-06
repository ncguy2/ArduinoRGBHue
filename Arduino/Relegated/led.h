#ifndef LED_H
#define LED_H

#include "rgb.h"
#include "interp.h"

class LED {
public:
  const int redPin;
  const int greenPin;
  const int bluePin;
  LED(const int redPin, const int greenPin, const int bluePin) : redPin(redPin), greenPin(greenPin), bluePin(bluePin) {
    currentColour = new HSB();
    currentColour->s = 100;
    currentColour->b = 50;
  }
  void Init() {
    pinMode(redPin, OUTPUT);
    pinMode(greenPin, OUTPUT);
    pinMode(bluePin, OUTPUT);
  }
  void SetHue(int hue) {
    this->currentColour->h = hue;
//    interp.Linear(this->currentColour, targetHue, interpDuration, HUE);
  }
  void SetSat(int sat) {
    this->currentColour->s = sat;
//    interp.Linear(this->currentColour, targetSat, interpDuration, SAT);
  }
  void SetVal(int val) {
    this->currentColour->b = val;
//    interp.Linear(this->currentColour, targetVal, interpDuration, VAL);
  }
  void Update() {
//    interp.Update();
    currentRGB.FromHSB(*currentColour);
    analogWrite(this->redPin,   currentRGB.r);
    analogWrite(this->greenPin, currentRGB.g);
    analogWrite(this->bluePin,  currentRGB.b);
  }
  HSB* currentColour;
  RGB currentRGB;
  unsigned int interpDuration = 1000;
protected:
  int targetHue;
  int targetSat;
  int targetVal;
//  Interpolation interp;
};

#endif // LED_H
