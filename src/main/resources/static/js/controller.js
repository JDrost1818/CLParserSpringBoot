app.controller('usersController', ($scope) => {
    $scope.headingTitle = "User List EDIT";
});

app.controller('rolesController', ($scope) => {
    $scope.headingTitle = "Roles List";
});

app.controller('loginController', ($scope, $route) => {
    var route = $route.current['$$route'].originalPath;
    $scope.isLoggingIn = route === "/login";

    $scope.switchLoginType = (toLogin) => {
        $scope.isLoggingIn = toLogin;
    }
});