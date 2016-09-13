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
public class RobotiqFt {
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
    public RobotiqFt() {
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
    public RobotiqFt(SerialParameters serialParameters) {
        this.serialParameters = serialParameters;
        this.modbusSerialMaster = new ModbusSerialMaster(this.getSerialParameters());
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
                    System.out.println(Arrays.toString(robotiqFt.getCompleteMeasure()));
                } catch (ModbusException e) {
                    Logger.error(e);
                }
            }
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    public int getUnitID() {
        return this.unitID;
    }

    public void setUnitID(int unitID) {
        this.unitID = unitID;
    }

    public int getFxRegister() {
        return this.fxRegister;
    }

    public void setFxRegister(int fxRegister) {
        this.fxRegister = fxRegister;
    }

    public int getFyRegister() {
        return this.fyRegister;
    }

    public void setFyRegister(int fyRegister) {
        this.fyRegister = fyRegister;
    }

    public int getFzRegister() {
        return this.fzRegister;
    }

    public void setFzRegister(int fzRegister) {
        this.fzRegister = fzRegister;
    }

    public int getMxRegister() {
        return this.mxRegister;
    }

    public void setMxRegister(int mxRegister) {
        this.mxRegister = mxRegister;
    }

    public int getMyRegister() {
        return this.myRegister;
    }

    public void setMyRegister(int myRegister) {
        this.myRegister = myRegister;
    }

    public int getMzRegister() {
        return this.mzRegister;
    }

    public void setMzRegister(int mzRegister) {
        this.mzRegister = mzRegister;
    }

    public int getAxRegister() {
        return this.axRegister;
    }

    public void setAxRegister(int axRegister) {
        this.axRegister = axRegister;
    }

    public int getAyRegister() {
        return this.ayRegister;
    }

    public void setAyRegister(int ayRegister) {
        this.ayRegister = ayRegister;
    }

    public int getAzRegister() {
        return this.azRegister;
    }

    public void setAzRegister(int azRegister) {
        this.azRegister = azRegister;
    }

    public double getForceDivisor() {
        return this.forceDivisor;
    }

    public void setForceDivisor(double forceDivisor) {
        this.forceDivisor = forceDivisor;
    }

    public double getMomentDivisor() {
        return this.momentDivisor;
    }

    public void setMomentDivisor(double momentDivisor) {
        this.momentDivisor = momentDivisor;
    }

    public double getAccelerationDivisor() {
        return this.accelerationDivisor;
    }

    public void setAccelerationDivisor(double accelerationDivisor) {
        this.accelerationDivisor = accelerationDivisor;
    }

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

    public double getFx() throws ModbusException {
        InputRegister register = this.getModbusSerialMaster()
                .readInputRegisters(this.getUnitID(), this.getFxRegister(), 1)[0];
        return (register.toShort() / this.getForceDivisor());
    }

    public double getFy() throws ModbusException {
        InputRegister register = this.getModbusSerialMaster()
                .readInputRegisters(this.getUnitID(), this.getFyRegister(), 1)[0];
        return (register.toShort() / this.getForceDivisor());
    }

    public double getFz() throws ModbusException {
        InputRegister register = this.getModbusSerialMaster()
                .readInputRegisters(this.getUnitID(), this.getFzRegister(), 1)[0];
        return (register.toShort() / this.getForceDivisor());
    }

    public double getMx() throws ModbusException {
        InputRegister register = this.getModbusSerialMaster()
                .readInputRegisters(this.getUnitID(), this.getMxRegister(), 1)[0];
        return (register.toShort() / this.getMomentDivisor());
    }

    public double getMy() throws ModbusException {
        InputRegister register = this.getModbusSerialMaster()
                .readInputRegisters(this.getUnitID(), this.getMyRegister(), 1)[0];
        return (register.toShort() / this.getMomentDivisor());
    }

    public double getMz() throws ModbusException {
        InputRegister register = this.getModbusSerialMaster()
                .readInputRegisters(this.getUnitID(), this.getMzRegister(), 1)[0];
        return (register.toShort() / this.getMomentDivisor());
    }

    public double getAx() throws ModbusException {
        InputRegister register = this.getModbusSerialMaster()
                .readInputRegisters(this.getUnitID(), this.getAxRegister(), 1)[0];
        return (register.toShort() / this.getAccelerationDivisor());
    }

    public double getAy() throws ModbusException {
        InputRegister register = this.getModbusSerialMaster()
                .readInputRegisters(this.getUnitID(), this.getAyRegister(), 1)[0];
        return (register.toShort() / this.getAccelerationDivisor());
    }

    public double getAz() throws ModbusException {
        InputRegister register = this.getModbusSerialMaster()
                .readInputRegisters(this.getUnitID(), this.getAzRegister(), 1)[0];
        return (register.toShort() / this.getAccelerationDivisor());
    }

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

    public double[] getForces() throws ModbusException {
        double[] data = {this.getFx(), this.getFy(), this.getFz()};
        return data;
    }

    public double[] getMoments() throws ModbusException {
        double[] data = {this.getMx(), this.getMy(), this.getMz()};
        return data;
    }

    public double[] getAccelerations() throws ModbusException {
        double[] data = {this.getAx(), this.getAy(), this.getAz()};
        return data;
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
