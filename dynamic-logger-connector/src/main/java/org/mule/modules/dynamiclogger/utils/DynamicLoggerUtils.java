package org.mule.modules.dynamiclogger.utils;

import org.apache.commons.collections.map.MultiValueMap;
import org.apache.logging.log4j.core.jmx.LoggerConfigAdminMBean;
import org.mule.modules.dynamiclogger.model.DynamicLogger;
import org.mule.modules.dynamiclogger.model.DynamicLoggerBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    public static void createDynamicLoggers(MultiValueMap multiValueMap, List<DynamicLogger> result, String appName) {
        Collection collection = multiValueMap.getCollection(appName);
        List<LoggerConfigAdminMBean> mBeans = new ArrayList<>();
        for (Object mBean : collection) {
            mBeans.add((LoggerConfigAdminMBean) mBean);
        }
        result.add(dynamicLoggerBuilder(appName, mBeans));
    }

    private static DynamicLogger dynamicLoggerBuilder(String appName, List<LoggerConfigAdminMBean> mBeans) {
        return DynamicLoggerBuilder.builder()
                .withName(appName)
                .addLoggerConfig(mBeans)
                .build();
    }
}
