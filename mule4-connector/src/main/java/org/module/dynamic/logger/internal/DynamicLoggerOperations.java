package org.module.dynamic.logger.internal;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.jmx.LoggerConfigAdminMBean;
import org.module.dynamic.logger.api.DynamicLogger;
import org.module.dynamic.logger.service.DynamicLoggerService;
import org.module.dynamic.logger.utils.DynamicLoggerUtils;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;


/**
 * This class is a container for operations, every public method in this class will be taken as an extension operation.
 */
public class DynamicLoggerOperations {
    Logger logger = LoggerFactory.getLogger(DynamicLoggerOperations.class);


    @MediaType(value = ANY, strict = false)
    @DisplayName("List All Logs")
    public Result<List<DynamicLogger>, Map> listAllLogs() throws Exception {
        String searchPattern = String.format(LoggerConfigAdminMBean.PATTERN, "*", "*");
        DynamicLoggerService service = new DynamicLoggerService();
        List<DynamicLogger> loggerConfigAdminMBeans = service.findAndCreateResult(searchPattern);
        return Result.<List<DynamicLogger>, Map>builder()
                .attributes(Collections.EMPTY_MAP)
                .mediaType(org.mule.runtime.api.metadata.MediaType.APPLICATION_JAVA)
                .output(loggerConfigAdminMBeans)
                .build();

    }

    @MediaType(value = ANY, strict = false)
    @DisplayName("Logs by appName")

    public Result<List<DynamicLogger>, Map> findLogByAppName(String appName) throws Exception {
        logger.debug("Find Loggers for appName {}", appName);
        DynamicLoggerService service = new DynamicLoggerService();
        String searchPattern = String.format(LoggerConfigAdminMBean.PATTERN, appName, "*");
        List<DynamicLogger> result = service.findAndCreateResult(searchPattern);
        return Result.<List<DynamicLogger>, Map>builder()
                .attributes(Collections.EMPTY_MAP)
                .mediaType(org.mule.runtime.api.metadata.MediaType.APPLICATION_JAVA)
                .output(result)
                .build();

    }

    @MediaType(value = ANY, strict = false)
    @DisplayName("Update logger")
    public boolean updateLogger(String appName,
                                @Optional(defaultValue = "org.mule.service.http.impl.service.HttpMessageLogger") String loggerName,
                                @Optional(defaultValue = "DEBUG") String level) {
        boolean update = false;
        DynamicLoggerService service = new DynamicLoggerService();
        try {
            String resolveLogLevel = DynamicLoggerUtils.resolveLogLevel(level.toUpperCase());
            if (StringUtils.isBlank(resolveLogLevel)) {
                throw new Exception("Invalid Log Level [ " + level + " ]");
            }
            String searchPattern = String.format(LoggerConfigAdminMBean.PATTERN, appName, loggerName);
            LoggerConfigAdminMBean loggerBeanAdmin = service.findLoggerByAppAndLoggerName(searchPattern);
            loggerBeanAdmin.setLevel(level);
            logger.info("Updating [ app = {}, logger = {}, level = {} ]", appName, loggerName, level);
            update = true;
        } catch (Exception e) {
            String errorString = "Error while updating log level for app [" + appName + " ], loggerName [" + loggerName + " ]";
            logger.error(errorString, e);
            update = false;
        }
        return update;

    }

}
