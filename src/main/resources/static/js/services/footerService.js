app.factory('footerService', ($http) => {
    return {
        getFooterText: () => {
            return $http({
                method: 'GET',
                url: '/json',
                transformResponse: [function (response) {
                    return response;
                }]
            });
        }
    }
});