package me.nicholasnadeau.robotiq.ft;

import com.fazecast.jSerialComm.SerialPort;
import org.pmw.tinylog.Logger;

/**
 * Created by nicholas on 2016-09-13.
 */
public class SerialUtilities {
    static final String[] ROBOTIQ_FT_PORT_NAME = {"cu", "FTX"};

    public static String findSerialPort() {
        String portName = null;

        Logger.info("Finding serial ports");
        SerialPort[] serialPorts = SerialPort.getCommPorts();
        for (SerialPort serialPort : serialPorts) {
            String tempPortName = serialPort.getSystemPortName();

            boolean isPortFound = tempPortName.toLowerCase().contains(ROBOTIQ_FT_PORT_NAME[0].toLowerCase())
                    && tempPortName.toLowerCase().contains(ROBOTIQ_FT_PORT_NAME[1].toLowerCase());

            if (isPortFound) {
                portName = tempPortName;
                Logger.info("Found Robotiq sensor:\t" + portName);
            }
        }

        return portName;
    }
}
