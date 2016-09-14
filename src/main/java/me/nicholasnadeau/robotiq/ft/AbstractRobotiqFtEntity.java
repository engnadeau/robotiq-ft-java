package me.nicholasnadeau.robotiq.ft;

import com.ghgande.j2mod.modbus.util.SerialParameters;

import java.util.Properties;

/**
 * Created on 2016-09-13.
 * <p>
 * Copyright Nicholas Nadeau 2016.
 */
public abstract class AbstractRobotiqFtEntity {
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

    void loadModbusParameters(Properties properties) {
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
}
