package me.nicholasnadeau.robotiq.ft;

import com.fazecast.jSerialComm.SerialPort;

import java.util.Arrays;

/**
 * Created by nicholas on 2016-09-13.
 */
public class SerialUtilities {
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
}
