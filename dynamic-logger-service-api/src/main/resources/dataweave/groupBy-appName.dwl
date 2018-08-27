%dw 1.0
%output application/json

---
{
	host: flowVars.host,
	applications: payload default [] map (logger) -> {
		appName: logger.appName,
		loggers:logger.loggers
	}
}