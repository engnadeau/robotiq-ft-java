cmake_minimum_required(VERSION 3.3)
project(robotiq_ft)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

#set(Boost_USE_STATIC_LIBS ON)
find_package(Boost 1.60.0 REQUIRED)
include_directories(${Boost_INCLUDE_DIR})

include_directories(lib/robotiq)
add_library(RQ_LIB STATIC
        lib/robotiq/mutex.c
        lib/robotiq/rq_sensor_com.c
        lib/robotiq/rq_sensor_state.c
        lib/robotiq/rq_thread.c)

set(SOURCE_FILES
        src/main.cpp src/SensorUdpServer.cpp src/SensorUdpServer.h)

add_executable(robotiq_ft
        ${SOURCE_FILES})

target_link_libraries(robotiq_ft
        RQ_LIB
        ${Boost_LIBRARIES})