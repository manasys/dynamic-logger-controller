package org.module.dynamic.logger.api;

import org.apache.logging.log4j.core.jmx.LoggerConfigAdminMBean;

import java.util.List;

public class DynamicLogger {
    private String appName;
    private List<LoggerConfigAdminMBean> loggers;

    public DynamicLogger(String appName, List<LoggerConfigAdminMBean> loggers) {
        this.appName = appName;
        this.loggers = loggers;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public List<LoggerConfigAdminMBean> getLoggers() {
        return loggers;
    }

    public void setLoggers(List<LoggerConfigAdminMBean> loggers) {
        this.loggers = loggers;
    }
}
