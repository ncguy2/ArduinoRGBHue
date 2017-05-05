#ifndef INSTRUCTIONS_H
#define INSTRUCTIONS_H

class Instruction {
public:
  virtual void Update(LED& led, float delta) = 0;
  virtual void Cleanup() {}
};

class InstructionManager {
public:
  void Update(LED& led) {
    if(instruction != nullptr) {
      instruction->Update(led, GetDelta());
    }
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

class BreathingInstruction : public Instruction {
public:
  virtual void Update(LED& led, float delta) override {
    delta *= 0.1f;
    if(ascend) {
      bValue += delta;
    }else{
      bValue -= delta;
    }
    
    if(ascend) {
      if(bValue >= 1.f) {
        bValue = 1.f;
        ascend = false;
      }
    }else{
      if(bValue <= 0.f) {
        bValue = 0.f;
        ascend = true;
      }
    }
    
    led.SetVal((int)(bValue * 50));
  }
protected:
  bool ascend = true;
  float bValue = 0;
};

class TwoColourFade : public BreathingInstruction {
public:
  TwoColourFade(float hA, float hB) : hA(hA), hB(hB) {}
  void Update(LED& led, float delta) override {
    delta *= 0.1f;
    if(ascend) {
      bValue += delta;
    }else{
      bValue -= delta;
    }
    
    if(ascend) {
      if(bValue >= 1.f) {
        bValue = 1.f;
        ascend = false;
      }
    }else{
      if(bValue <= 0.f) {
        bValue = 0.f;
        ascend = true;
      }
    }

    led.SetHue(hA + (hB - hA) * bValue);
  }
protected:
  float hA, hB;
};

#endif // INSTRUCTIONS_H

