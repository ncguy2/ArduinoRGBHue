#ifndef INTERP_H
#define INTERP_H

#include "rgb.h"
#include <Arduino.h>
#include <StandardCplusplus.h>
#include <vector>
#define ASFLOAT(var) ((float)var)

enum InterpType {
  HUE, SAT, VAL,
  InterpType_LENGTH
};

class BaseInterpTarget {
public:
  virtual void Update(unsigned int delta) = 0;
  virtual bool IsFinished() = 0;
};

class InterpTarget : public BaseInterpTarget {
public:
  InterpTarget(HSB* value, long target, unsigned int millisDuration, InterpType type) : value(value), target(target), millisProgress(0), millisDuration(millisDuration), prog(0.f), type(type) {
    switch(type) {
      case HUE:
        start = value->h;
        break;
      case SAT:
        start = value->s;
        break;
      case VAL:
        start = value->b;
        break;
    }
  }
private:
  HSB* value;
  long start;
  long target;
  InterpType type;
  unsigned int millisProgress;
  unsigned int millisDuration;
  float prog;
  
public:
  void Update(unsigned int delta) override {   
    this->millisProgress += delta;
    prog = ((float)this->millisProgress) / ((float)millisDuration);
    long value = ASFLOAT(start) + (ASFLOAT(target) - ASFLOAT(start)) * prog;
    switch(type) {
      case HUE:
        this->value->Set(value);
        break;
      case SAT:
        this->value->s = value;
        break;
      case VAL:
        this->value->b = value;
        break;
    }
  }

  bool IsFinished() override {
    return prog >= 1.f;
  }
};

class Interpolation {
public:
  void Linear(HSB* value, long target, unsigned int durationMillis, InterpType type) {
    targets.push_back(new InterpTarget(value, target, durationMillis, type));
  }
  
  void Update() {
    unsigned int delta = GetDeltaMillis();
    for(BaseInterpTarget* target : targets)
      target->Update(delta);

    targets.erase(std::remove_if(targets.begin(), targets.end(),
      [](const InterpTarget* obj) { return obj->IsFinished(); }), targets.end());
  }
  unsigned int GetDeltaMillis() {
    currentTime = millis();
    deltaTime = abs(currentTime - prevTime);
    prevTime = currentTime;
    return deltaTime;
  }
protected:
  unsigned long currentTime;
  unsigned long prevTime;
  unsigned int deltaTime;
  std::vector<BaseInterpTarget*> targets;
};

#endif // INTERP_H
