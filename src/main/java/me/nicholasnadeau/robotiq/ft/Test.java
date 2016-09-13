package me.nicholasnadeau.robotiq.ft;


import com.fazecast.jSerialComm.SerialPort;
import com.ghgande.j2mod.modbus.io.ModbusSerialTransaction;
import com.ghgande.j2mod.modbus.msg.ReadInputRegistersRequest;
import com.ghgande.j2mod.modbus.msg.ReadInputRegistersResponse;
import com.ghgande.j2mod.modbus.net.AbstractSerialConnection;
import com.ghgande.j2mod.modbus.net.SerialConnection;
import com.ghgande.j2mod.modbus.util.SerialParameters;
import org.pmw.tinylog.Logger;


/**
 * Created by nicholas on 2016-09-12.
 */
public class Test {
    static final String[] ROBOTIQ_FT_PORT_NAME = {"cu", "FTX"};
    static final int ROBOTIQ_FT_BAUDRATE = 19200;
    static final int ROBOTIQ_FT_DATA_BITS = 8;
    static final int ROBOTIQ_FT_STOP_BIT = 1;
    static final int ROBOTIQ_FT_ID = 9;
    static final int ROBOTIQ_FT_COUNT = 6;
    static final int ROBOTIQ_FT_REGISTER = 180;
    static final String ROBOTIQ_FT_PARITY = "None";
    static final String ROBOTIQ_FT_ENCODING = "rtu";
    static final int NUMBER_OF_MESSAGES = 10000;

    public static void main(String[] args) {

        String portName = null;

        // find serial port
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

        if (portName == null) {
            Logger.error("No Robotiq sensor found... exiting");
            return;
        }

        AbstractSerialConnection con = null;

        try {
            Logger.info("Setup serial parameters");
            SerialParameters params = new SerialParameters();
            params.setPortName(portName);
            params.setBaudRate(ROBOTIQ_FT_BAUDRATE);
            params.setDatabits(ROBOTIQ_FT_DATA_BITS);
            params.setParity(ROBOTIQ_FT_PARITY);
            params.setStopbits(ROBOTIQ_FT_STOP_BIT);
            params.setEncoding(ROBOTIQ_FT_ENCODING);
            params.setEcho(false);

            Logger.info(String.format("Encoding [%s]", params.getEncoding()));

            Logger.info("Open the connection");
            con = new SerialConnection(params);
            con.open();

            Logger.info("Prepare a request");
            ReadInputRegistersRequest req = new ReadInputRegistersRequest(ROBOTIQ_FT_REGISTER, ROBOTIQ_FT_COUNT);
            req.setUnitID(ROBOTIQ_FT_ID);
            req.setHeadless();
            Logger.info(String.format("Request: %s", req.getHexMessage()));

            Logger.info("Prepare the transaction");
            ModbusSerialTransaction trans = new ModbusSerialTransaction(con);
            trans.setRequest(req);

            Logger.info("Execute the transaction repeat times");
            for (int i = 0; i < NUMBER_OF_MESSAGES; i++) {
                trans.execute();

                ReadInputRegistersResponse res = (ReadInputRegistersResponse) trans.getResponse();
                System.out.println(String.format("%d\t%d\t%d\t%d\t%d\t%d",
                        res.getRegister(0).toShort() / 100,
                        res.getRegister(1).toShort() / 100,
                        res.getRegister(2).toShort() / 100,
                        res.getRegister(3).toShort() / 100,
                        res.getRegister(4).toShort() / 100,
                        res.getRegister(5).toShort() / 100
                ));
            }

            Logger.info("Close the connection");
            con.close();

        } catch (Exception ex) {
            Logger.error(ex);
            // Close the connection
            if (con != null) {
                con.close();
            }
        }
    }
}
