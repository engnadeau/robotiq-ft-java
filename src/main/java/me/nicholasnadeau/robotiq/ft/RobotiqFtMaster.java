package me.nicholasnadeau.robotiq.ft;

import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.facade.ModbusSerialMaster;
import com.ghgande.j2mod.modbus.procimg.InputRegister;
import com.ghgande.j2mod.modbus.util.SerialParameters;
import org.pmw.tinylog.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

/**
 * Created by nicholas on 2016-09-13.
 */
public class RobotiqFtMaster {
    private static final String CONFIG_FILE = "config.properties";
    private int unitID;
    private int fxRegister;
    private int fyRegister;
    private int fzRegister;
    private int mxRegister;
    private int myRegister;
    private int mzRegister;
    private int axRegister;
    private int ayRegister;
    private int azRegister;
    private double forceDivisor;
    private double momentDivisor;
    private double accelerationDivisor;
    private SerialParameters serialParameters;
    private ModbusSerialMaster modbusSerialMaster;

    /**
     * Constructs a sensor object using config.properties
     */
    public RobotiqFtMaster() {
        boolean isSuccess = this.loadProperties();

        if (isSuccess) {
            this.modbusSerialMaster = new ModbusSerialMaster(this.getSerialParameters());
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Constructs a sensor object given the input.
     *
     * @param serialParameters defined manually
     */
    public RobotiqFtMaster(SerialParameters serialParameters) {
        this.serialParameters = serialParameters;
        this.modbusSerialMaster = new ModbusSerialMaster(this.getSerialParameters());
    }

    /**
     * Convenient connection test
     *
     * @param args
     */
    public static void main(String[] args) {
        Logger.info("Running simple RobotiqFT connection test");
        RobotiqFtMaster robotiqFtMaster = new RobotiqFtMaster();
        try {
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
     * @return unit ID of sensor
     */
    public int getUnitID() {
        return this.unitID;
    }

    /**
     * @param unitID of sensor
     */
    public void setUnitID(int unitID) {
        this.unitID = unitID;
    }

    /**
     * @return modbus register
     */
    public int getFxRegister() {
        return this.fxRegister;
    }

    /**
     * @param fxRegister modbus register
     */
    public void setFxRegister(int fxRegister) {
        this.fxRegister = fxRegister;
    }

    /**
     * @return modbus register
     */
    public int getFyRegister() {
        return this.fyRegister;
    }

    /**
     * @param fyRegister modbus register
     */
    public void setFyRegister(int fyRegister) {
        this.fyRegister = fyRegister;
    }

    /**
     * @return modbus register
     */
    public int getFzRegister() {
        return this.fzRegister;
    }

    /**
     * @param fzRegister modbus register
     */
    public void setFzRegister(int fzRegister) {
        this.fzRegister = fzRegister;
    }

    /**
     * @return modbus register
     */
    public int getMxRegister() {
        return this.mxRegister;
    }

    /**
     * @param mxRegister modbus register
     */
    public void setMxRegister(int mxRegister) {
        this.mxRegister = mxRegister;
    }

    /**
     * @return modbus register
     */
    public int getMyRegister() {
        return this.myRegister;
    }

    /**
     * @param myRegister modbus register
     */
    public void setMyRegister(int myRegister) {
        this.myRegister = myRegister;
    }

    /**
     * @return modbus register
     */
    public int getMzRegister() {
        return this.mzRegister;
    }

    /**
     * @param mzRegister modbus register
     */
    public void setMzRegister(int mzRegister) {
        this.mzRegister = mzRegister;
    }

    /**
     * @return modbus register
     */
    public int getAxRegister() {
        return this.axRegister;
    }

    /**
     * @param axRegister modbus register
     */
    public void setAxRegister(int axRegister) {
        this.axRegister = axRegister;
    }

    /**
     * @return modbus register
     */
    public int getAyRegister() {
        return this.ayRegister;
    }

    /**
     * @param ayRegister modbus register
     */
    public void setAyRegister(int ayRegister) {
        this.ayRegister = ayRegister;
    }

    /**
     * @return modbus register
     */
    public int getAzRegister() {
        return this.azRegister;
    }

    /**
     * @param azRegister modbus register
     */
    public void setAzRegister(int azRegister) {
        this.azRegister = azRegister;
    }

    /**
     * @return modbus value divisor
     */
    public double getForceDivisor() {
        return this.forceDivisor;
    }

    /**
     * @param forceDivisor modbus value divisor
     */
    public void setForceDivisor(double forceDivisor) {
        this.forceDivisor = forceDivisor;
    }

    /**
     * @return modbus value divisor
     */
    public double getMomentDivisor() {
        return this.momentDivisor;
    }

    /**
     * @param momentDivisor modbus value divisor
     */
    public void setMomentDivisor(double momentDivisor) {
        this.momentDivisor = momentDivisor;
    }

    /**
     * @return modbus value divisor
     */
    public double getAccelerationDivisor() {
        return this.accelerationDivisor;
    }

    /**
     * @param accelerationDivisor modbus value divisor
     */
    public void setAccelerationDivisor(double accelerationDivisor) {
        this.accelerationDivisor = accelerationDivisor;
    }

    /**
     * @return success of loading process
     */
    private boolean loadProperties() {
        Properties properties = new Properties();
        boolean isSuccess = true;

        // load a properties file
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            try (InputStream inputStream = loader.getResourceAsStream(CONFIG_FILE)) {
                properties.load(inputStream);
            }
        } catch (IOException e) {
            Logger.error(e);
            isSuccess = false;
        }

        // set the properties
        if (isSuccess) {
            // set serial parameter
            SerialParameters serialParameters = new SerialParameters();
            serialParameters.setPortName(properties.getProperty("comm_port"));
            serialParameters.setBaudRate(Integer.parseInt(properties.getProperty("baudrate")));
            serialParameters.setDatabits(Integer.parseInt(properties.getProperty("data_bits")));
            serialParameters.setStopbits(Integer.parseInt(properties.getProperty("stop_bit")));
            serialParameters.setParity(properties.getProperty("parity"));
            serialParameters.setEncoding(properties.getProperty("encoding"));
            serialParameters.setEcho(false);
            this.setSerialParameters(serialParameters);

            // set instance parameters
            this.setUnitID(Integer.parseInt(properties.getProperty("unit_id")));

            this.setFxRegister(Integer.parseInt(properties.getProperty("fx_register")));
            this.setFyRegister(Integer.parseInt(properties.getProperty("fy_register")));
            this.setFzRegister(Integer.parseInt(properties.getProperty("fz_register")));

            this.setMxRegister(Integer.parseInt(properties.getProperty("mx_register")));
            this.setMyRegister(Integer.parseInt(properties.getProperty("my_register")));
            this.setMzRegister(Integer.parseInt(properties.getProperty("mz_register")));

            this.setAxRegister(Integer.parseInt(properties.getProperty("ax_register")));
            this.setAyRegister(Integer.parseInt(properties.getProperty("ay_register")));
            this.setAzRegister(Integer.parseInt(properties.getProperty("az_register")));

            this.setForceDivisor(Short.parseShort(properties.getProperty("force_divisor")));
            this.setMomentDivisor(Short.parseShort(properties.getProperty("moment_divisor")));
            this.setAccelerationDivisor(Short.parseShort(properties.getProperty("acceleration_divisor")));
        }

        return isSuccess;
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

    /**
     * @return serial parameters
     */
    public SerialParameters getSerialParameters() {
        return this.serialParameters;
    }

    /**
     * @param serialParameters
     */
    public void setSerialParameters(SerialParameters serialParameters) {
        this.serialParameters = serialParameters;
    }
}
