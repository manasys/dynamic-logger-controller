package org.mule.modules.dynamiclogger;

import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.core.jmx.LoggerConfigAdminMBean;
import org.mule.api.MuleMessage;
import org.mule.api.annotations.Config;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.param.Default;
import org.mule.modules.dynamiclogger.exception.DynamicLoggerException;
import org.mule.modules.dynamiclogger.model.DynamicLogger;
import org.mule.modules.dynamiclogger.utils.DynamicLoggerUtils;
import org.mule.transformer.types.SimpleDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.JMException;
import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Connector(name = "dynamic-logger-controller", friendlyName = "Dynamic Logger Controller")
public class DynamicLoggerConnector {
    private Logger logger = LoggerFactory.getLogger(DynamicLoggerConnector.class);


    @Config
    private DynamicLoggerConnector config;

    @Processor
    public void updateLogLevel(@Default("DEBUG") String level, @Default("org.mule.module.http.internal.HttpMessageLogger") String loggerName, String appName) throws DynamicLoggerException {

        try {
            String resolveLogLevel = DynamicLoggerUtils.resolveLogLevel(level.toUpperCase());
            if (StringUtils.isBlank(resolveLogLevel)) {
                throw new DynamicLoggerException("Invalid Log Level [ " + level + " ]");
            }
            String searchPattern = String.format(LoggerConfigAdminMBean.PATTERN, appName, loggerName);
            LoggerConfigAdminMBean loggerBeanAdmin = findLoggerByAppAndLoggerName(searchPattern);
            loggerBeanAdmin.setLevel(level);
            logger.info("Updating [ app = {}, logger = {}, level = {} ]", appName, loggerName, level);
        } catch (Exception e) {
            String errorString = "Error while updating log level for app [" + appName + " ], loggerName [" + loggerName + " ]";
            logger.error(errorString, e);
            throw new DynamicLoggerException(errorString);
        }
    }

    @Processor
    public void ListLogsByAppName(String appName, MuleMessage muleMessage) throws Exception {
        logger.info("Find Loggers for appName {}", appName);
        String searchPattern = String.format(LoggerConfigAdminMBean.PATTERN, appName, "*");
        List<DynamicLogger> result = new ArrayList<>();
        List<DynamicLogger> dynamicLoggers = findAndCreateResult(searchPattern);
        muleMessage.setPayload(result, new SimpleDataType<List<DynamicLogger>>(List.class, "application/java"));

    }

    @Processor
    public void listAllLogs(MuleMessage muleMessage) throws Exception {
        String searchPattern = String.format(LoggerConfigAdminMBean.PATTERN, "*", "*");
        List<DynamicLogger> loggerConfigAdminMBeans = findAndCreateResult(searchPattern);
        muleMessage.setPayload(loggerConfigAdminMBeans, new SimpleDataType<List<DynamicLogger>>(List.class, "application/java"));
    }

    private LoggerConfigAdminMBean findLoggerByAppAndLoggerName(String searchPattern) throws Exception {
        MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
        logger.info("Search Pattern [ {} ]", searchPattern);
        ObjectName searchObjectName = new ObjectName(searchPattern);
        LoggerConfigAdminMBean loggerContextAdmin = getLoggerConfigAdminBean(searchObjectName, platformMBeanServer);
        if (logger.isDebugEnabled()) {
            logger.debug("LoggerConfigAdminMBean ---- {}", loggerContextAdmin);
        }
        return loggerContextAdmin;
    }

    private List<DynamicLogger> findAndCreateResult(String searchPattern) throws Exception {
        logger.info("Search Pattern [ {} ]", searchPattern);
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        final Set<ObjectName> contextNames = findByPattern(searchPattern, mBeanServer);
        MultiValueMap multiMap = createLogLevelResult(contextNames, mBeanServer);
        List<DynamicLogger> result = new ArrayList<>();
        Set<String> appNames = multiMap.keySet();
        for (String appnName : appNames) {
            if (logger.isDebugEnabled()) {
                logger.debug("Reading loggers for app {} ", appNames);

            }
            DynamicLoggerUtils.createDynamicLoggers(multiMap, result, appnName);

        }
        return result;

    }

    private MultiValueMap createLogLevelResult(Set<ObjectName> contextNames, MBeanServer mBeanServer) {
        MultiValueMap multiValueMap = new MultiValueMap();
        for (final ObjectName contextName : contextNames) {
            String name = contextName.getKeyProperty("name");
            String type = contextName.getKeyProperty("type");
            if (StringUtils.isNotBlank(name) && !type.startsWith("sun.misc") && !type.equals("default")) {
                if (logger.isDebugEnabled()) {
                    logger.debug("ObjectName {}", contextName.getCanonicalKeyPropertyListString());
                }
                LoggerConfigAdminMBean loggerConfigAdminMBean = getLoggerConfigAdminBean(contextName, mBeanServer);
                multiValueMap.put(type, loggerConfigAdminMBean);
            }
        }
        return multiValueMap;
    }

    private LoggerConfigAdminMBean getLoggerConfigAdminBean(final ObjectName name, MBeanServer mBeanServer) {
        return JMX.newMBeanProxy(mBeanServer,
                name,
                LoggerConfigAdminMBean.class, false);
    }

    private Set<ObjectName> findByPattern(final String pattern, MBeanServer mBeanServer) throws JMException {
        final ObjectName searchQuery = new ObjectName(pattern);
        return mBeanServer.queryNames(searchQuery, null);

    }

    public DynamicLoggerConnector getConfig() {
        return config;
    }

    public void setConfig(DynamicLoggerConnector config) {
        this.config = config;
    }
}