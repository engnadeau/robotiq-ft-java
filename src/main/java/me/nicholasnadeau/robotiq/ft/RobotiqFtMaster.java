package me.nicholasnadeau.robotiq.ft;

import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.facade.ModbusSerialMaster;
import com.ghgande.j2mod.modbus.procimg.InputRegister;
import com.ghgande.j2mod.modbus.util.SerialParameters;
import org.pmw.tinylog.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

/**
 * Created by nicholas on 2016-09-13.
 */
public class RobotiqFtMaster extends AbstractRobotiqFtEntity {

    private ModbusSerialMaster modbusSerialMaster;

    /**
     * Constructs a sensor object using config.properties
     */
    public RobotiqFtMaster() throws IOException {
        Properties properties = SerialUtilities.loadProperties();

        this.loadModbusParameters(properties);
        this.setSerialParameters(SerialUtilities.generateSerialParameters(properties));
        this.modbusSerialMaster = new ModbusSerialMaster(this.getSerialParameters());
    }

    /**
     * Constructs a sensor object given the input.
     *
     * @param serialParameters defined manually
     */
    public RobotiqFtMaster(SerialParameters serialParameters) {
        this.setSerialParameters(serialParameters);
        this.modbusSerialMaster = new ModbusSerialMaster(this.getSerialParameters());
    }

    /**
     * Convenient connection test
     *
     * @param args
     */
    public static void main(String[] args) {
        Logger.info("Running simple RobotiqFT connection test");
        try {
            RobotiqFtMaster robotiqFtMaster = new RobotiqFtMaster();
            robotiqFtMaster.connect();

            int maxTime = 20;
            Logger.info(String.format("Reading from sensor for %d seconds", maxTime));
            long startTime = System.currentTimeMillis();
            while ((System.currentTimeMillis() - startTime) < (maxTime * 1000)) {
                try {
                    System.out.println(Arrays.toString(robotiqFtMaster.getCompleteMeasure()));
                } catch (ModbusException e) {
                    Logger.error(e);
                }
            }
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    /**
     * @return measured force [N]
     * @throws ModbusException
     */
    public double getFx() throws ModbusException {
        InputRegister register = this.getModbusSerialMaster()
                .readInputRegisters(this.getUnitID(), this.getFxRegister(), 1)[0];
        return (register.toShort() / this.getForceDivisor());
    }

    /**
     * @return measured force [N]
     * @throws ModbusException
     */
    public double getFy() throws ModbusException {
        InputRegister register = this.getModbusSerialMaster()
                .readInputRegisters(this.getUnitID(), this.getFyRegister(), 1)[0];
        return (register.toShort() / this.getForceDivisor());
    }

    /**
     * @return measured force [N]
     * @throws ModbusException
     */
    public double getFz() throws ModbusException {
        InputRegister register = this.getModbusSerialMaster()
                .readInputRegisters(this.getUnitID(), this.getFzRegister(), 1)[0];
        return (register.toShort() / this.getForceDivisor());
    }

    /**
     * @return measured moment [Nm]
     * @throws ModbusException
     */
    public double getMx() throws ModbusException {
        InputRegister register = this.getModbusSerialMaster()
                .readInputRegisters(this.getUnitID(), this.getMxRegister(), 1)[0];
        return (register.toShort() / this.getMomentDivisor());
    }

    /**
     * @return measured moment [Nm]
     * @throws ModbusException
     */
    public double getMy() throws ModbusException {
        InputRegister register = this.getModbusSerialMaster()
                .readInputRegisters(this.getUnitID(), this.getMyRegister(), 1)[0];
        return (register.toShort() / this.getMomentDivisor());
    }

    /**
     * @return
     * @throws ModbusException
     */
    public double getMz() throws ModbusException {
        InputRegister register = this.getModbusSerialMaster()
                .readInputRegisters(this.getUnitID(), this.getMzRegister(), 1)[0];
        return (register.toShort() / this.getMomentDivisor());
    }

    /**
     * @return measured acceleration [g]
     * @throws ModbusException
     */
    public double getAx() throws ModbusException {
        InputRegister register = this.getModbusSerialMaster()
                .readInputRegisters(this.getUnitID(), this.getAxRegister(), 1)[0];
        return (register.toShort() / this.getAccelerationDivisor());
    }

    /**
     * @return measured acceleration [g]
     * @throws ModbusException
     */
    public double getAy() throws ModbusException {
        InputRegister register = this.getModbusSerialMaster()
                .readInputRegisters(this.getUnitID(), this.getAyRegister(), 1)[0];
        return (register.toShort() / this.getAccelerationDivisor());
    }

    /**
     * @return measured acceleration [g]
     * @throws ModbusException
     */
    public double getAz() throws ModbusException {
        InputRegister register = this.getModbusSerialMaster()
                .readInputRegisters(this.getUnitID(), this.getAzRegister(), 1)[0];
        return (register.toShort() / this.getAccelerationDivisor());
    }

    /**
     * @return measured wrench {Fx, Fy, Fz, Mx, My, Mz} [N, N, N, Nm, Nm, Nm]
     * @throws ModbusException
     */
    public double[] getSixAxisMeasure() throws ModbusException {
        InputRegister[] registers = this.getModbusSerialMaster()
                .readInputRegisters(this.getUnitID(), this.getFxRegister(), 6);

        assert registers.length == 6;

        double[] result = new double[6];
        for (int i = 0; i < 6; i++) {
            result[i] = registers[i].toShort();

            if (i < 3) {
                result[i] /= this.getForceDivisor();
            } else {
                result[i] /= this.getMomentDivisor();
            }
        }
        return result;
    }

    /**
     * @return measured wrench and accelerations {Fx, Fy, Fz, Mx, My, Mz, Ax, Ay, Az} [N, N, N, Nm, Nm, Nm, g, g, g]
     * @throws ModbusException
     */
    public double[] getCompleteMeasure() throws ModbusException {
        InputRegister[] wrenchRegisters = this.getModbusSerialMaster()
                .readInputRegisters(this.getUnitID(), this.getFxRegister(), 6);

        InputRegister[] accelerationRegisters = this.getModbusSerialMaster()
                .readInputRegisters(this.getUnitID(), this.getAxRegister(), 3);

        assert wrenchRegisters.length == 6;
        assert accelerationRegisters.length == 3;

        double[] result = new double[9];
        for (int i = 0; i < result.length; i++) {
            if (i < 3) {
                result[i] = wrenchRegisters[i].toShort() / this.getForceDivisor();
            } else if (i < 6) {
                result[i] = wrenchRegisters[i].toShort() / this.getMomentDivisor();
            } else {
                result[i] = accelerationRegisters[i - 6].toShort() / this.getAccelerationDivisor();
            }
        }

        return result;
    }

    /**
     * @return measured forces {Fx, Fy, Fz} [N, N, N]
     * @throws ModbusException
     */
    public double[] getForces() throws ModbusException {
        double[] data = {this.getFx(), this.getFy(), this.getFz()};
        return data;
    }

    /**
     * @return measured moments {Mx, My, Mz} [Nm, Nm, Nm]
     * @throws ModbusException
     */
    public double[] getMoments() throws ModbusException {
        double[] data = {this.getMx(), this.getMy(), this.getMz()};
        return data;
    }

    /**
     * @return measured accelerations {Ax, Ay, Az} [g, g, g]
     * @throws ModbusException
     */
    public double[] getAccelerations() throws ModbusException {
        double[] data = {this.getAx(), this.getAy(), this.getAz()};
        return data;
    }

    /**
     * @return modbus client object
     */
    public ModbusSerialMaster getModbusSerialMaster() {
        return this.modbusSerialMaster;
    }

    /**
     * @param modbusSerialMaster modbus client object
     */
    public void setModbusSerialMaster(ModbusSerialMaster modbusSerialMaster) {
        this.modbusSerialMaster = modbusSerialMaster;
    }

    /**
     * @throws Exception if connection to sensor fails
     */
    public void connect() throws Exception {
        this.getModbusSerialMaster().connect();
    }
}
