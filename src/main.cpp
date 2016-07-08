#include <string.h>
#include <stdio.h>
#include <windows.h>
#include "SensorUdpServer.h"
#include <boost/asio.hpp>

extern "C"
{
#include "rq_sensor_com.h"
#include "rq_sensor_state.h"
#include "rq_thread.h"
#include "rq_int.h"
}

static void waitForOtherConnection() {
    INT_8 ret;
    while (true) {
        Sleep(1000);// wait 1 second
        ret = rq_sensor_state();
        if (ret == 0) {
            break;
        }
    }
}

static void getData(INT_8 *chr_return) {
    INT_8 i;
    INT_8 floatData[50];
    for (i = 0; i < 6; i++) {
        sprintf(floatData, "%f", rq_state_get_received_data(i));
        if (i == 0) {
            strcpy(chr_return, "( ");
            strcat(chr_return, floatData);
        }
        else {
            strcat(chr_return, " , ");
            strcat(chr_return, floatData);
        }
        if (i == 5) {
            strcat(chr_return, " )");
        }
    }
}

void serveData() {
    //IF can't connect with the sensor wait for another connection
    INT_8 ret = rq_sensor_state();
    if (ret == -1) {
        waitForOtherConnection();
    }

    //Read high-level informations
    ret = rq_sensor_state();
    if (ret == -1) {
        waitForOtherConnection();
    }

    //Initialize connection with the client
    ret = rq_sensor_state();
    if (ret == -1) {
        waitForOtherConnection();
    }

    // set up UDP async server
    boost::asio::io_service io_service;
    SensorUdpServer server(io_service);
    io_service.run();

    //INT_8 buffer[512]; //Init of the variable receiving the message
    INT_8 bufStream[512];
    while (true) {
        ret = rq_sensor_state();

        if (ret == -1) {
            waitForOtherConnection();
        }

        if (rq_sensor_get_current_state() == RQ_STATE_RUN) {
            strcpy(bufStream, "");
            getData(bufStream);
            printf("%s\n", bufStream);
        }
    }
}

int main() {
    serveData();
    return 0;
}