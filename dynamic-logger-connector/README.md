# Dynamic Logger Connector

A MuleESB connector which connects to JMX locally and edits log level for given logger name using log4j MBeans


# Installation 
You can download the source code and build it with devkit to find it available on your local repository. Then you can add it to Studio

#Usage
This connectors exposes 3 operations
* `findLogs` : finds logs for all applications running on JVM
* `findLogsByAppName`: finds logs for given application name
* `updateLogs`: Updates log level for given name and logger name

# Reporting Issues

We use GitHub:Issues for tracking issues with this connector. You can report new issues at this link https://github.com/mulesoft-consulting/dynamic-logger-controller