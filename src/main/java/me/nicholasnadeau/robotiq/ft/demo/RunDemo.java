package me.nicholasnadeau.robotiq.ft.demo;

import com.ghgande.j2mod.modbus.ModbusException;
import me.nicholasnadeau.robotiq.ft.RobotiqFtMaster;

import java.util.Arrays;
import java.util.logging.Logger;

import static me.nicholasnadeau.robotiq.ft.RobotiqFtSerial.getSerialPortNames;

/**
 * Created by nicholas on 2017-03-29.
 */
public class RunDemo {
    private final static Logger LOGGER = Logger.getLogger(RunDemo.class.getSimpleName());
    private final static int MAX_TIME = 1;
    private static long startTime;

    public static void main(String[] args) throws Exception {
        LOGGER.info("Please set properties file to correct serial port");
        LOGGER.info("Available serial ports:\t" + Arrays.toString(getSerialPortNames()));

        LOGGER.info("Running simple RobotiqFT connection test");
        RobotiqFtMaster robotiqFtMaster = new RobotiqFtMaster();
        robotiqFtMaster.connect();

        LOGGER.info(String.format("Reading from sensor for %d seconds using Modbus RTU", MAX_TIME));
        startTime = System.currentTimeMillis();
        while ((System.currentTimeMillis() - startTime) < (MAX_TIME * 1000)) {
            try {
                System.out.println(
                        String.format(
                                "%d\t%s", System.currentTimeMillis() - startTime,
                                Arrays.toString(robotiqFtMaster.getCompleteMeasure())));
            } catch (ModbusException e) {
                LOGGER.severe(String.valueOf(e));
            }
        }

        LOGGER.info(String.format("Reading from sensor for %d seconds using data stream", MAX_TIME));
        startTime = System.currentTimeMillis();
        robotiqFtMaster.startDataStream();
        while ((System.currentTimeMillis() - startTime) < (MAX_TIME * 1000)) {
            try {
                System.out.println(
                        String.format(
                                "%d\t%s", System.currentTimeMillis() - startTime,
                                Arrays.toString(robotiqFtMaster.getCompleteMeasure())));
            } catch (ModbusException e) {
                LOGGER.severe(String.valueOf(e));
            }
        }
    }
}
