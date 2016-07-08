cmake_minimum_required(VERSION 3.3)
project(robotiq_ft)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

include_directories(lib/robotiq)
add_library(RQ_LIB STATIC
        lib/robotiq/mutex.c
        lib/robotiq/rq_sensor_com.c
        lib/robotiq/rq_sensor_state.c
        lib/robotiq/rq_thread.c)

set(SOURCE_FILES
        src/main.cpp)

add_executable(robotiq_ft
        ${SOURCE_FILES})

target_link_libraries(robotiq_ft
        RQ_LIB)