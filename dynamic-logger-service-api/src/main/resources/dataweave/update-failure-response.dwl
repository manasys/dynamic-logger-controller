%dw 1.0
%output application/java
---
{
	appName: flowVars.appName,
	logger: flowVars.logName,
	level : flowVars.level,
	status: "Failure",
	errorMessage : flowVars.errorMessage
}