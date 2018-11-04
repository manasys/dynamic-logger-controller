package org.module.dynamic.logger.utils;

import org.apache.commons.collections.map.MultiValueMap;
import org.apache.logging.log4j.core.jmx.LoggerConfigAdminMBean;
import org.module.dynamic.logger.api.DynamicLogger;
import org.module.dynamic.logger.api.DynamicLoggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DynamicLoggerUtils {
    static  Logger logger = LoggerFactory.getLogger(DynamicLoggerUtils.class);
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

    public static String getAppName(String domainAndAppName) {
        String[] nameWithDomain = domainAndAppName.split("/");
        int size = nameWithDomain.length;
        String appName = nameWithDomain[size-1];
        logger.debug("complete name {} , Resolved app name {} ",domainAndAppName, appName);
        return appName;
    }

}
