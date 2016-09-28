app.directive('header', () => {
    return {
        restrict: 'A',
        replace: true,
        templateUrl: "/js/directives/header.html",
        controller:  ['$scope', ($scope) => {

            function init() {
                $scope.headerText = "this is custom header text";
            }

            init();
        }]
    }
});