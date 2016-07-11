package ca.etsmtl.coro.nn.robotiq.ft;

import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Created by nicholas on 2016-07-05.
 */
public class App {
    static final boolean IS_DEBUG = true;

    static final String PROJECT_ROOT = System.getProperty("user.dir");
    static final String APP_ROOT = PROJECT_ROOT + "\\robotiq-bin\\";
    static String ROBOTIQ_APP;

    public static void main(String[] args) {
        // set up logger
        Configurator.defaultConfig()
                .level(Level.DEBUG)
                .activate();

        // set up app
        if (IS_DEBUG) {
            ROBOTIQ_APP = "robotiq_ft_simulator.exe";
        } else {
            ROBOTIQ_APP = "driver-sensor.exe";
        }

        // get and check OS
        String os = System.getProperty("os.name");
        Logger.info("Detected:\t" + os);

        if (os.contains("Windows")) {
            runWindows();
        } else {
            Logger.error("OS not supported, exiting...");
        }
    }

    static void runWindows() {
        // get path to exe
        Logger.debug("Project Root:\t" + PROJECT_ROOT);
        Logger.debug("App Root:\t" + PROJECT_ROOT);
        Logger.debug("EXE:\t" + ROBOTIQ_APP);
        ProcessBuilder processBuilder = new ProcessBuilder(APP_ROOT + ROBOTIQ_APP);

        try {
            Logger.info("Loading runtime process.");
            Process process = processBuilder.start();
            Logger.info("Process running.");

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String s;
            while ((s = reader.readLine()) != null) {
                float[] values = parseRobotiqOutput(s);
            }
        } catch (IOException e) {
            Logger.error(e);
        }
    }

    static float[] parseRobotiqOutput(String string) {
        // remove outer brackets
        string = string.replaceAll("\\(", "").replaceAll("\\)", "");

        // remove whitespace
        string = string.replaceAll("\\s", "");

        // split wrt commas
        String[] split = string.split(",");

        // convert to float
        float[] values = new float[split.length];
        for (int i = 0; i < split.length; i++) {
            values[i] = Float.parseFloat(split[i]);
        }

        return values;
    }
}
