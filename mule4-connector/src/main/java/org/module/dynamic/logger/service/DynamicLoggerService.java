package org.module.dynamic.logger.service;

import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.jmx.LoggerConfigAdminMBean;
import org.module.dynamic.logger.api.DynamicLogger;
import org.module.dynamic.logger.utils.DynamicLoggerUtils;

import javax.management.JMException;
import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DynamicLoggerService {
    org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DynamicLoggerService.class);

    public LoggerConfigAdminMBean findLoggerByAppAndLoggerName(String searchPattern) throws Exception {
        logger.info("Search Pattern [ {} ]", searchPattern);
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName searchObjectName = new ObjectName(searchPattern);
        LoggerConfigAdminMBean loggerContextAdmin = getLoggerConfigAdminBean(searchObjectName, mBeanServer);
        if (logger.isDebugEnabled()) {
            logger.debug("LoggerConfigAdminMBean ---- {}", loggerContextAdmin);
        }
        return loggerContextAdmin;
    }

    public List<DynamicLogger> findAndCreateResult(String searchPattern) throws Exception {
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
            //String appName = DynamicLoggerUtils.getAppName(type);
            if (StringUtils.isNotBlank(name) && !type.startsWith("sun.misc") && !type.equals("domain/default")) {
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
}
