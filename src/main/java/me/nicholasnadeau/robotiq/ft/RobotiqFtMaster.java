package me.nicholasnadeau.robotiq.ft;

import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.facade.ModbusSerialMaster;
import com.ghgande.j2mod.modbus.procimg.InputRegister;
import com.ghgande.j2mod.modbus.procimg.Register;
import com.ghgande.j2mod.modbus.procimg.SimpleInputRegister;
import com.ghgande.j2mod.modbus.procimg.SimpleRegister;
import com.ghgande.j2mod.modbus.util.SerialParameters;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Logger;

public class RobotiqFtMaster extends AbstractRobotiqFtEntity {
    final Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    private ModbusSerialMaster modbusSerialMaster;

    /**
     * Constructs a sensor object using robotiq-ft.properties
     */
    public RobotiqFtMaster() throws IOException {
        Properties properties = RobotiqFtSerial.loadProperties();

        this.loadModbusParameters(properties);
        this.setSerialParameters(RobotiqFtSerial.generateSerialParameters(properties));
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

    public void startDataStream() throws ModbusException, InterruptedException {
        // stop first?
        stopDataStream();

        Register register = new SimpleInputRegister(this.getDataStreamInitValue());
        this.getModbusSerialMaster().writeSingleRegister(this.getUnitID(), this.getDataStreamRegister(), register);
    }

    public void stopDataStream() throws ModbusException, InterruptedException {
        Register register = new SimpleInputRegister(this.getDataStreamStopValue());

        for (int i = 0; i < 50; i++) {
            this.getModbusSerialMaster().writeSingleRegister(this.getUnitID(), this.getDataStreamRegister(), register);
            Thread.sleep(10);
        }
    }
}
