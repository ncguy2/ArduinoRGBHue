#ifndef INSTRUCTIONS_H
#define INSTRUCTIONS_H

#include "neopixel.h"

class Instruction {
public:
  virtual void Update(NeoPixel& neopixel, float delta) = 0;
  virtual void Cleanup() {}
};

class InstructionManager {
public:

  static InstructionManager& GetInstance() {
    static InstructionManager instance;
    return instance;
  }

  void Update(NeoPixel& neopixel) {
    if(instruction != nullptr)
      instruction->Update(neopixel, GetDelta());
  }

  void ClearInstruction() {
    SetInstruction(nullptr);
  }

  void SetInstruction(Instruction* newInst) {
    if(instruction != nullptr) {
      instruction->Cleanup();
      delete instruction;
    }
    instruction = newInst;
  }
  
protected:
  unsigned long currentTime;
  unsigned long prevTime;
  int deltaTime;

  unsigned int GetDeltaMillis() {
    currentTime = millis();
    deltaTime = abs(currentTime - prevTime);
    prevTime = currentTime;
    return deltaTime;
  }

  float GetDelta() {
    return ((float)GetDeltaMillis()) / 1000.f;
  }

  Instruction* instruction;
};

class WormSpinInstruction : public Instruction {
public:
  WormSpinInstruction(RGB fg, RGB bg, byte len, int wait) : fg(fg), bg(bg), len(len), wait(wait) {}
  void Update(NeoPixel& neopixel, float delta) override {
    neopixel.ColourWormBG_Stepped(fg, bg, len, wait, iteration++);
  }
  void Cleanup() override {
  }
protected:
  RGB fg;
  RGB bg;
  byte len;
  int wait;
  int iteration = 0;
};

class WipeInstruction : public Instruction {
public:
  WipeInstruction(uint32_t colA, uint32_t colB, int wait) : colA(colA), colB(colB), wait(wait) {}
  virtual void Update(NeoPixel& neopixel, float delta) override {
    neopixel.DualWipe_Stepped(colA, colB, wait, iteration++);
  }
  void Cleanup() override {
  }
protected:
  uint32_t colA;
  uint32_t colB;
  int wait;
  int iteration = 0;
};

class TriWipeInstruction : public WipeInstruction {
public:
  TriWipeInstruction(uint32_t colA, uint32_t colB, uint32_t colC, int wait) : WipeInstruction(colA, colB, wait), colC(colC) {}
  virtual void Update(NeoPixel& neopixel, float delta) override {
    neopixel.TriWipe_Stepped(colA, colB, colC, wait, iteration++);
  }
protected:
  uint32_t colC;
};

class QuadWipeInstruction : public TriWipeInstruction {
public:
  QuadWipeInstruction(uint32_t colA, uint32_t colB, uint32_t colC, uint32_t colD, int wait) : TriWipeInstruction(colA, colB, colC, wait), colD(colD) {}
  void Update(NeoPixel& neopixel, float delta) override {
    neopixel.QuadWipe_Stepped(colA, colB, colC, colD, wait, iteration++);
  }
protected:
  uint32_t colD;
};

class BreathingInstruction : public Instruction {
public:
    BreathingInstruction(RGB colour, float speed = 1.f) : colour(colour), speedScale(speed * 0.1f) {}
    void Update(NeoPixel& neoPixel, float delta) override {
      delta /= speedScale;
      if(!ascend) delta = -delta;
      value += delta;

      if(value >= 1.f) {
        value = 1.f;
        ascend = false;
      }else if(value <= 0.f) {
        value = 0.f;
        ascend = true;
      }

      RGB col(colour);
      col *= value;
      neoPixel.SetRingColour(col.ToLong(), 0);
    }

protected:
    float value = 1.f;
    float speedScale = 1.f;
    bool ascend = false;
    RGB colour;
};

#endif // INSTRUCTIONS_H

