CMAKE_MINIMUM_REQUIRED(VERSION 3.0.0)
PROJECT(RGB_light_prototype)
set(CMAKE_TOOLCHAIN_FILE ${CMAKE_SOURCE_DIR}/cmake/ArduinoToolchain.cmake)

set(${CMAKE_PROJECT_NAME}_BOARD uno)
set(${CMAKE_PROJECT_NAME}_PORT COM3)

set(${CMAKE_PROJECT_NAME}_SKETCH RGB_light_prototype.ino)

INCLUDE_DIRECTORIES("C:\\Program Files (x86)\\Arduino\\hardware\\arduino\\avr\\cores\\arduino")