package me.shetj.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandUtils {

    public static  String exec(String command) {
        StringBuilder resultBuilder = new StringBuilder();
        Process pro = null;
        BufferedReader input = null;
        Runtime runTime = Runtime.getRuntime();
        if (runTime == null) {
            throw new NullPointerException("reinforce task failed,Runtime is null");
        }
        try {
            pro = runTime.exec(command);
            input = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                resultBuilder.append(line).append("\n");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (pro != null) {
                pro.destroy();
            }
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultBuilder.toString();
    }
}
