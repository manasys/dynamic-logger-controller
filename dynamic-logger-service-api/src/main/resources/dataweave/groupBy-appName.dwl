%dw 1.0
%output application/json

---
{
	host: flowVars.host,
	loggers: (payload default [] groupBy $.appName) unless payload == null otherwise []
}