package me.nicholasnadeau.robotiq.ft;

import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.facade.ModbusSerialMaster;
import com.ghgande.j2mod.modbus.procimg.InputRegister;
import com.ghgande.j2mod.modbus.util.SerialParameters;
import org.pmw.tinylog.Logger;

import java.util.Arrays;

/**
 * Created by nicholas on 2016-09-13.
 */
public class RobotiqFt {
    static final int BAUDRATE = 19200;
    static final int DATA_BITS = 8;
    static final int STOP_BIT = 1;
    static final int UNIT_ID = 9;

    static final int FX_REGISTER = 180;
    static final int FY_REGISTER = 181;
    static final int FZ_REGISTER = 182;
    static final int MX_REGISTER = 183;
    static final int MY_REGISTER = 184;
    static final int MZ_REGISTER = 185;
    static final int AX_REGISTER = 190;
    static final int AY_REGISTER = 191;
    static final int AZ_REGISTER = 192;

    static final short FORCE_DIVISOR = 100;
    static final short MOMENT_DIVISOR = 1000;
    static final short ACCELERATION_DIVISOR = 1000;

    static final String PARITY = "None";
    static final String ENCODING = "rtu";
    SerialParameters serialParameters;
    ModbusSerialMaster modbusSerialMaster;

    public RobotiqFt(SerialParameters serialParameters) {
        this.serialParameters = serialParameters;
        this.modbusSerialMaster = new ModbusSerialMaster(this.getSerialParameters());
    }

    public RobotiqFt() {
        this(setDefaultSerialParameters());
    }

    public static void main(String[] args) {
        Logger.info("Running simple RobotiqFT connection test");
        RobotiqFt robotiqFt = new RobotiqFt();
        try {
            robotiqFt.connect();

            int maxTime = 20;
            Logger.info(String.format("Reading from sensor for %d seconds", maxTime));
            long startTime = System.currentTimeMillis();
            while ((System.currentTimeMillis() - startTime) < (maxTime * 1000)) {
                try {
                    System.out.println(Arrays.toString(robotiqFt.getNineAxisMeasure()));
                } catch (ModbusException e) {
                    Logger.error(e);
                }
            }
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    public short getFx() throws ModbusException {
        InputRegister register = this.getModbusSerialMaster()
                .readInputRegisters(UNIT_ID, FX_REGISTER, 1)[0];
        return (short) (register.toShort() / FORCE_DIVISOR);
    }

    public short getFy() throws ModbusException {
        InputRegister register = this.getModbusSerialMaster()
                .readInputRegisters(UNIT_ID, FY_REGISTER, 1)[0];
        return (short) (register.toShort() / FORCE_DIVISOR);
    }

    public short getFz() throws ModbusException {
        InputRegister register = this.getModbusSerialMaster()
                .readInputRegisters(UNIT_ID, FZ_REGISTER, 1)[0];
        return (short) (register.toShort() / FORCE_DIVISOR);
    }

    public short getMx() throws ModbusException {
        InputRegister register = this.getModbusSerialMaster()
                .readInputRegisters(UNIT_ID, MX_REGISTER, 1)[0];
        return (short) (register.toShort() / MOMENT_DIVISOR);
    }

    public short getMy() throws ModbusException {
        InputRegister register = this.getModbusSerialMaster()
                .readInputRegisters(UNIT_ID, MY_REGISTER, 1)[0];
        return (short) (register.toShort() / MY_REGISTER);
    }

    public short getMz() throws ModbusException {
        InputRegister register = this.getModbusSerialMaster()
                .readInputRegisters(UNIT_ID, MZ_REGISTER, 1)[0];
        return (short) (register.toShort() / MOMENT_DIVISOR);
    }

    public short getAx() throws ModbusException {
        InputRegister register = this.getModbusSerialMaster()
                .readInputRegisters(UNIT_ID, AX_REGISTER, 1)[0];
        return (short) (register.toShort() / AX_REGISTER);
    }

    public short getAy() throws ModbusException {
        InputRegister register = this.getModbusSerialMaster()
                .readInputRegisters(UNIT_ID, AY_REGISTER, 1)[0];
        return (short) (register.toShort() / AY_REGISTER);
    }

    public short getAz() throws ModbusException {
        InputRegister register = this.getModbusSerialMaster()
                .readInputRegisters(UNIT_ID, AZ_REGISTER, 1)[0];
        return (short) (register.toShort() / AZ_REGISTER);
    }

    public short[] getSixAxisMeasure() throws ModbusException {
        InputRegister[] registers = this.getModbusSerialMaster()
                .readInputRegisters(UNIT_ID, FX_REGISTER, 6);

        assert registers.length == 6;

        short[] result = new short[6];
        for (int i = 0; i < 6; i++) {
            result[i] = registers[i].toShort();

            if (i < 3) {
                result[i] /= FORCE_DIVISOR;
            } else {
                result[i] /= MOMENT_DIVISOR;
            }
        }
        return result;
    }

    public short[] getNineAxisMeasure() throws ModbusException {
        InputRegister[] wrenchRegisters = this.getModbusSerialMaster()
                .readInputRegisters(UNIT_ID, FX_REGISTER, 6);

        InputRegister[] accelerationRegisters = this.getModbusSerialMaster()
                .readInputRegisters(UNIT_ID, AX_REGISTER, 3);

        assert wrenchRegisters.length == 6;
        assert accelerationRegisters.length == 3;

        short[] result = new short[9];
        for (int i = 0; i < 6; i++) {
            result[i] = wrenchRegisters[i].toShort();

            if (i < 3) {
                result[i] /= FORCE_DIVISOR;
            } else {
                result[i] /= MOMENT_DIVISOR;
            }
        }

        for (int i = 0; i < 3; i++) {
            result[6 + i] = (short) (accelerationRegisters[i].toShort() / ACCELERATION_DIVISOR);
        }

        return result;
    }

    public short[] getForces() throws ModbusException {
        short[] data = {this.getFx(), this.getFy(), this.getFz()};
        return data;
    }

    public short[] getMoments() throws ModbusException {
        short[] data = {this.getMx(), this.getMy(), this.getMz()};
        return data;
    }

    public short[] getAccelerations() throws ModbusException {
        short[] data = {this.getAx(), this.getAy(), this.getAz()};
        return data;
    }

    private static SerialParameters setDefaultSerialParameters() {
        SerialParameters serialParameters = new SerialParameters();
        serialParameters.setPortName(SerialUtilities.findSerialPort());
        serialParameters.setBaudRate(BAUDRATE);
        serialParameters.setDatabits(DATA_BITS);
        serialParameters.setStopbits(STOP_BIT);
        serialParameters.setParity(PARITY);
        serialParameters.setEncoding(ENCODING);
        serialParameters.setEcho(false);
        return serialParameters;
    }

    public ModbusSerialMaster getModbusSerialMaster() {
        return this.modbusSerialMaster;
    }

    public void setModbusSerialMaster(ModbusSerialMaster modbusSerialMaster) {
        this.modbusSerialMaster = modbusSerialMaster;
    }

    private void connect() throws Exception {
        this.getModbusSerialMaster().connect();
    }

    public SerialParameters getSerialParameters() {
        return this.serialParameters;
    }

    public void setSerialParameters(SerialParameters serialParameters) {
        this.serialParameters = serialParameters;
    }
}
