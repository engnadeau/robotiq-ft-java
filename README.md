<br />
<p align="center">
<a href="https://github.com/nnadeau/robotiq-ft-java">
<img src="https://raw.githubusercontent.com/nnadeau/robotiq-ft-java/master/media/logo.jpg" alt="Logo" width="300">
</a>
<h3 align="center">Robotiq FT Sensor - Java</h3>
<p align="center">
An unofficial Java package for cross-platform serial communication with a Robotiq FT Sensor
</p>
</p>


[![Release](https://github.com/nnadeau/robotiq-ft-java/workflows/Release/badge.svg)](https://github.com/nnadeau/robotiq-ft-java/actions)

[![GitHub tag](https://img.shields.io/github/tag/nnadeau/robotiq-ft-java.svg?maxAge=2592000?style=flat-square)](https://github.com/nnadeau/robotiq-ft-java/releases)
[![GitHub issues](https://img.shields.io/github/issues/nnadeau/robotiq-ft-java)](https://github.com/nnadeau/robotiq-ft-java/issues)
[![GitHub forks](https://img.shields.io/github/forks/nnadeau/robotiq-ft-java)](https://github.com/nnadeau/robotiq-ft-java/network)
[![GitHub stars](https://img.shields.io/github/stars/nnadeau/robotiq-ft-java)](https://github.com/nnadeau/robotiq-ft-java/stargazers)
[![GitHub license](https://img.shields.io/github/license/nnadeau/robotiq-ft-java)](https://github.com/nnadeau/robotiq-ft-java/blob/master/LICENSE)

[![Twitter](https://img.shields.io/twitter/url?style=social&url=https%3A%2F%2Fgithub.com%2Fnnadeau%2Frobotiq-ft-java)](https://twitter.com/intent/tweet?text=Wow:&url=https%3A%2F%2Fgithub.com%2Fnnadeau%2Frobotiq-ft-java)

## Communication Protocol

The communication protocol (and basis for `config.properties`) for using a FT Sensor can be found [here](http://support.robotiq.com/pages/viewpage.action?pageId=9601256).

## Usage

### Tested Platforms
- Windows 10 x64
- OSX 10.11.5 x64

### Quick Tests

- Run `SerialUtilities.java` to list available serial ports (sensor should be connected and seen here)
- Update `comm_port` in `config.properties` to the proper serial port (e.g., `COM3` for Windows, `cu.usbserial-FTXU0M1B` for OSX)
- Run `RobotiqFtMaster.java` for a 20 second burst of data printed to the console

### Example

```java
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
```
