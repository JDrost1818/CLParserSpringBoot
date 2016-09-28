app.directive('footer', () => {
    return {
        restrict: 'A',
        replace: true,
        templateUrl: "/js/directives/footer.html",
        controller:  ['$scope', 'footerService', ($scope, footerService) => {

            function init() {
                footerService.getFooterText().then((response) => {
                    $scope.footerText = response.data;
                });
            }

            init();
        }]
    }
});