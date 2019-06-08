var baseURL = ""
var basePath= "/api"
angular.module('DynamicLoggerApp', []).controller('DynamicLoggerController',function($scope, $http) {
    		baseURL = location.protocol + "//" + location.host + basePath;
    		console.info("baseURL " , baseURL)
			$scope.isError=false;
			$scope.loading = true;
			$scope.loggerLevels = ["TRACE", "DEBUG", "INFO", "WARN", "ERROR"]
			fetchAllLoggers($scope, $http);
			$scope.selectedLevel = 'INFO'
			$scope.changeLevel = function(appName, logger, level) {
			console.debug("Changing level for app [", appName, "] logger [",logger, "] level [", level, "]")
			var putUrl = baseURL + "/loggers"
			var payload = createLoggerPayload(logger, level,appName)
			console.info("Put URL ", putUrl, " putData ", JSON.stringify(payload))
			$http.put(putUrl, payload).then(function(response) {
				fetchAllLoggers($scope, $http);
			},
			function(data, status){
				console.info("Error in updating logger" , status)
				$scope.isError=true;
				$scope.errorMessage="Error while updating logger"
			});

	}
	  $scope.addNewLogger = function (appName, loggerName, level) {
	        console.info("Adding level for app [", appName, "] logger [", loggerName, "] level [", level, "]")
			var postURL = baseURL + "/loggers/" + appName
			var payload = createLoggerPayload(loggerName, level,appName)
			console.info("Post URL ", postURL, " payload ", JSON.stringify(payload))
			$http.post(postURL, payload).then(function(response) {
				fetchAllLoggers($scope, $http);
			},
			function(data, status){
				console.info("Error in adding logger" , status)
				$scope.isError=true;
				$scope.errorMessage="Error while adding logger"
			});
	       
	       };
	});
function fetchAllLoggers($scope, $http) {
	$http.get(baseURL + "/loggers").then(function(response) {
		console.debug("Http success", JSON.stringify(response.data))
		$scope.selectedApp = response.data[0].appName
		$scope.applications = response.data;
		$scope.selectedLevel = 'INFO'
		$scope.loggers = sortResult(response.data)
	},
	function(errorResoonse){
		console.info("ErrorResponse ", errorResoonse.status)
		$scope.isError=true;
		$scope.errorMessage="Error while fetching loggers"
	});
}
function createLoggerPayload(loggerName, level, appName) {
	var loggerPayload = []
	var loggers = [ {
		name : loggerName,
		level : level
	} ]

	loggerPayload.push({
		appName : appName,
		loggers : loggers

	});
	return loggerPayload

}
function sortResult(loggers) {
	var sortedResult = []
	loggers.forEach(function(application) {
		application.loggers.forEach(function(logger) {
			sortedResult.push({
				actualName: application.appName,
				applicationName : application.appName.split("/").pop(-1),
				name : logger.name,
				level : logger.level
			})
		})

	})
	return sortedResult;
}
