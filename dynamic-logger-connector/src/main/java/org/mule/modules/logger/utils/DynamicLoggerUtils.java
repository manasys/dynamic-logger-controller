package org.mule.modules.logger.utils;

public class DynamicLoggerUtils {

    public static String resolveLogLevel(String logLevel) {
        String level = "";
        switch (logLevel) {
            case "TRACE":
                level = "TRACE";
                break;
            case "DEBUG":
                level = "DEBUG";
                break;

            case "INFO":
                level = "INFO";
                break;
            case "WARN":
                level = "WARN";
                break;

            case "ERROR":
                level = "ERROR";
                break;


        }
        return level;
    }

}
