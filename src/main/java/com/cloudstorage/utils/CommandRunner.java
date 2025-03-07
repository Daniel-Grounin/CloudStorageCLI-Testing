package com.cloudstorage.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CommandRunner {
    public static String runCommand(String command) {
        try {
            ProcessBuilder processBuilder;

            // Detect OS
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                // Windows uses cmd.exe
                processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
            } else {
                // Linux (Docker) uses bash
                processBuilder = new ProcessBuilder("bash", "-c", command);
            }

            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            process.waitFor();
            return output.toString().trim();
        } catch (Exception e) {
            return "Error executing command: " + e.getMessage();
        }
    }
}
