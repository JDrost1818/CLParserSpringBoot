app.controller('usersController', ($scope) => {
    $scope.headingTitle = "User List EDIT";
});

app.controller('rolesController', ($scope) => {
    $scope.headingTitle = "Roles List";
});

app.controller('loginController', ($scope, $http, $location) => {
    $scope.switchLoginType = (toLogin) => {
        if (toLogin) {
            $location.path("/dashboard");
        }
    }
});