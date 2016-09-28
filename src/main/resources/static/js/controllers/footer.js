app.directive('footer', () => {
    return {
        restrict: 'A',
        replace: true,
        templateUrl: "/js/directives/footer.html",
        controller:  ['$scope', ($scope) => {

            function init() {
                $scope.footerText = "This is some custom text";
            }

            init();
        }]
    }
});