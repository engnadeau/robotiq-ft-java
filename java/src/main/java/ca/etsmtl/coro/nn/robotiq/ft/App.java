package ca.etsmtl.coro.nn.robotiq.ft;

import java.io.IOException;

/**
 * Created by nicholas on 2016-07-05.
 */
public class App {
    public static void main(String[] args) {

        // get and check OS
        String os = System.getProperty("os.name");

        if (os.contains("Windows")) {
            runWindows();
        } else {
            System.out.println(os);
        }
    }

    static void runWindows() {
        // get path to exe
        String projectRoot = System.getProperty("user.dir");
        System.out.println(projectRoot);

        String robotiqApp = projectRoot + "\\robotiq-bin\\robotiq_ft_simulator.exe";
        System.out.println(robotiqApp);

        Process process;
        try {
            process = Runtime.getRuntime().exec(robotiqApp);
            process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
