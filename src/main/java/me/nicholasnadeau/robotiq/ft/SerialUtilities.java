package me.nicholasnadeau.robotiq.ft;

import com.fazecast.jSerialComm.SerialPort;
import com.ghgande.j2mod.modbus.util.SerialParameters;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

/**
 * Created by nicholas on 2016-09-13.
 */
public class SerialUtilities {
    private static final String CONFIG_FILE = "config.properties";

    public static void main(String[] args) {
        System.out.println(Arrays.toString(getSerialPortNames()));
    }

    public static String[] getSerialPortNames() {
        SerialPort[] commPorts = SerialPort.getCommPorts();

        String[] names = new String[commPorts.length];
        for (int i = 0; i < commPorts.length; i++) {
            names[i] = commPorts[i].getSystemPortName();
        }
        return names;
    }

    public static Properties loadProperties() throws IOException {
        Properties properties = new Properties();

        // load a properties file
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = loader.getResourceAsStream(CONFIG_FILE);
        properties.load(inputStream);

        return properties;
    }

    public static SerialParameters generateSerialParameters(Properties properties) {
        // set serial parameter
        SerialParameters serialParameters = new SerialParameters();
        serialParameters.setPortName(properties.getProperty("comm_port"));
        serialParameters.setBaudRate(Integer.parseInt(properties.getProperty("baudrate")));
        serialParameters.setDatabits(Integer.parseInt(properties.getProperty("data_bits")));
        serialParameters.setStopbits(Integer.parseInt(properties.getProperty("stop_bit")));
        serialParameters.setParity(properties.getProperty("parity"));
        serialParameters.setEncoding(properties.getProperty("encoding"));
        serialParameters.setEcho(false);

        return serialParameters;
    }
}
