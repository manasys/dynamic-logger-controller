package org.mule.modules.logger.model;

import org.apache.logging.log4j.core.jmx.LoggerConfigAdminMBean;

public class DynamicLogger {

    private LoggerConfigAdminMBean loggerConfigAdminMBean;
    private String appName;

    public LoggerConfigAdminMBean getLoggerConfigAdminMBean() {
        return loggerConfigAdminMBean;
    }

    public void setLoggerConfigAdminMBean(LoggerConfigAdminMBean loggerConfigAdminMBean) {
        this.loggerConfigAdminMBean = loggerConfigAdminMBean;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public String toString() {
        return "LogLevelConfiguration{" +
                "loggerConfigAdminMBean=" + loggerConfigAdminMBean +
                ", appName='" + appName + '\'' +
                '}';
    }
}
