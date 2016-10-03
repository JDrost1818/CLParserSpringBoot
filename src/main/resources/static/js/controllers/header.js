app.directive('header', () => {
    return {
        restrict: 'A',
        replace: true,
        templateUrl: "/js/directives/header.html",
        controller:  ['$scope', '$http', '$window', ($scope, $http, $window) => {

            // Gets the logged in user - if there is one
            $http.get("/user").success(function(data) {
                $scope.user = data;
                $scope.authenticated = data != "";
            }).error(function() {
                $scope.user = "N/A";
                $scope.authenticated = false;
            });

            $scope.logout = function() {
                $http.post('/logout', {}).success(function() {
                    $scope.user = {};
                    $scope.authenticated = false;
                    $window.location.href = '/';
                }).error(function(data) {
                    $scope.authenticated = false;
                });
            };

        }]
    }
});