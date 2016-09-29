app.directive('header', () => {
    return {
        restrict: 'A',
        replace: true,
        templateUrl: "/js/directives/header.html",
        controller:  ['$scope', '$http', ($scope, $http) => {

            // Gets the logged in user - if there is one
            $http.get("/user").success(function(data) {
                $scope.user = data.userAuthentication.details.name;
                $scope.authenticated = true;
            }).error(function() {
                $scope.user = "N/A";
                $scope.authenticated = false;
            });

            $scope.logout = function() {
                $http.post('/logout', {}).success(function() {
                    $scope.authenticated = false;
                    $location.path("/");
                }).error(function(data) {
                    console.log("Logout failed");
                    $scope.authenticated = false;
                });
            };

        }]
    }
});