cmake_minimum_required(VERSION 3.3)
project(robotiq_ft_simulator)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES
        src/main.cpp)

add_executable(robotiq_ft_simulator
        ${SOURCE_FILES})