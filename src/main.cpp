#include <iostream>
#include <synchapi.h>
#include "rq_int.h"
#include "rq_sensor_state.h"

static void wait_for_other_connection() {
    INT_8 ret;
    while (1) {
        Sleep(1000);// wait 1 seconde
        ret = rq_sensor_state();
        if (ret == 0) {
            break;
        }
    }
}

static void get_data(INT_8 *chr_return) {
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

int main() {
    //IF can't connect with the sensor wait for another connection
    INT_8 ret = rq_sensor_state();
    if (ret == -1) {
        wait_for_other_connection();
    }

    //Read high-level informations
    ret = rq_sensor_state();
    if (ret == -1) {
        wait_for_other_connection();
    }

    //Initialize connection with the client
    ret = rq_sensor_state();
    if (ret == -1) {
        wait_for_other_connection();
    }

    /*
     * Here comes the code to establish a connection with the application
    */

    //INT_8 buffer[512]; //Init of the variable receiving the message
    INT_8 bufStream[512];
    while (1) {
        /*strcpy(buffer,"");
        *  // Here we receive the message of the application to read
        *  // high level variable.
        *if(strcmp(buffer, "") != 0)
        *{
        *	decode_message_and_do(buffer);
        *}
        */

        ret = rq_sensor_state();
        if (ret == -1) {
            wait_for_other_connection();
        }
        if (rq_sensor_get_current_state() == RQ_STATE_RUN) {
            strcpy(bufStream, "");
            get_data(bufStream);
            printf("%s\n", bufStream);
            /*
             * Here comes the code to send the data to the application.
            */
        }
    }
    return 0;
}

