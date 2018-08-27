package org.mule.modules.dynamiclogger.model;

import org.apache.logging.log4j.core.jmx.LoggerConfigAdminMBean;

import java.util.List;

public class DynamicLoggerBuilder {
    private String appName;
    private List<LoggerConfigAdminMBean> loggerConfigAdmin;

    public DynamicLoggerBuilder() {

    }

    public DynamicLoggerBuilder withName(String name) {
        this.appName = name;
        return this;
    }

    public DynamicLoggerBuilder addLoggerConfig(List<LoggerConfigAdminMBean> mBeans) {
        loggerConfigAdmin = mBeans;
        return this;
    }
    public DynamicLogger build(){
        return new DynamicLogger(appName, loggerConfigAdmin);}

    public static DynamicLoggerBuilder builder() {
        return new DynamicLoggerBuilder();
    }
}
