#include <time.h>
#include <cstdlib>
#include <cstdio>
#include <cstring>

void getData(char *buffer) {
    // init
    const float MIN_RAND_VALUE = (const float) -100.0;
    const float MAX_RAND_VALUE = (const float) 100.0;
    srand((unsigned int) time(NULL));

    // create output
    for (int i = 0; i < 6; i++) {
        char floatData[50];
        float randomFloat = MIN_RAND_VALUE +
                            static_cast <float> (rand()) /
                            (RAND_MAX / (MAX_RAND_VALUE - MIN_RAND_VALUE));

        sprintf(floatData, "%f", randomFloat);
        if (i == 0) {
            strcpy(buffer, "( ");
            strcat(buffer, floatData);
        }
        else {
            strcat(buffer, " , ");
            strcat(buffer, floatData);
        }
        if (i == 5) {
            strcat(buffer, " )");
        }
    }
}

int main() {
    float startTime = clock() / (static_cast <float> (CLOCKS_PER_SEC));
    float currentTime = clock() / (static_cast <float> (CLOCKS_PER_SEC));

    char buffer[512];
    while (currentTime - startTime < 20) {
        // updated clock
        currentTime = clock() / (static_cast <float> (CLOCKS_PER_SEC));

        // slow down simulator a bit
        while ((clock() / (static_cast <float> (CLOCKS_PER_SEC))) - currentTime < 0.1) { }

        // clear data
        strcpy(buffer, "");

        // get data
        getData(buffer);

        // print
        printf("%s\n", buffer);
    }
}