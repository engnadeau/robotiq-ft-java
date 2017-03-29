package me.nicholasnadeau.robotiq.ft.demo;

import com.ghgande.j2mod.modbus.ModbusException;
import me.nicholasnadeau.robotiq.ft.RobotiqFtMaster;
import me.nicholasnadeau.robotiq.ft.RobotiqFtSerial;

import java.util.Arrays;
import java.util.logging.Logger;

import static me.nicholasnadeau.robotiq.ft.RobotiqFtSerial.getSerialPortNames;

/**
 * Created by nicholas on 2017-03-29.
 */
public class RunDemo {
    private final static Logger logger = Logger.getLogger(RunDemo.class.getSimpleName());

    public static void main(String[] args) {
        logger.info("Please set properties file to correct serial port");
        logger.info("Available serial ports:\t" + Arrays.toString(getSerialPortNames()));

        logger.info("Running simple RobotiqFT connection test");
        try {
            RobotiqFtMaster robotiqFtMaster = new RobotiqFtMaster();
            robotiqFtMaster.connect();

            int maxTime = 20;
            logger.info(String.format("Reading from sensor for %d seconds", maxTime));
            long startTime = System.currentTimeMillis();
            while ((System.currentTimeMillis() - startTime) < (maxTime * 1000)) {
                try {
                    System.out.println(String.format("%d\t%s", System.currentTimeMillis() - startTime, Arrays.toString(robotiqFtMaster.getCompleteMeasure())));
                } catch (ModbusException e) {
                    logger.severe(String.valueOf(e));
                }
            }
        } catch (Exception e) {
            logger.severe(String.valueOf(e));
        }
    }
}
