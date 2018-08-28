var baseURL = ""
var basePath= "/api"
angular.module('DynamicLoggerApp', []).controller('DynamicLoggerController',function($scope, $http) {
    		baseURL = location.protocol + "//" + location.host + basePath;
    		console.info("baseURL " , baseURL)
			$scope.isError=false;
			$scope.loading = true;
			fetchAllLoggers($scope, $http);
			$scope.changeLevel = function(appName, logger, level) {
				console.debug("Changing level for app [", appName, "] logger [",logger, "] level [", level, "]")
				var putData = []
				putData.push({
					name : logger,
					level : level
				})
				var putUrl = baseURL + "/loggers/" + appName
				console.info("Put URL ", putUrl, " putData ", JSON.stringify(putData))
				$http.put(putUrl, putData).then(function(response) {
					fetchAllLoggers($scope, $http);
				},
				function(data, status){
					console.info("Error in updating logger" , status)
					$scope.isError=true;
					$scope.errorMessage="Error while updating logger"
				});

			}
		});
function fetchAllLoggers($scope, $http) {
	$http.get(baseURL + "/loggers").then(function(response) {
		console.debug("Http success", JSON.stringify(response.data))
		$scope.loggers = sortResult(response.data)
	},
	function(errorResoonse){
		console.info("ErrorResponse ", errorResoonse.status)
		$scope.isError=true;
		$scope.errorMessage="Error while fetching loggers"
	});
}

function sortResult(loggers) {
	var sortedResult = []
	loggers.applications.forEach(function(application) {
		application.loggers.forEach(function(logger) {
			sortedResult.push({
				applicationName : application.appName,
				name : logger.name,
				level : logger.level
			})
		})

	})
	return sortedResult;
}
