(function() {
    'use strict';
    angular
        .module('kucunApp')
        .factory('UseLog', UseLog);

    UseLog.$inject = ['$resource', 'DateUtils'];

    function UseLog ($resource, DateUtils) {
        var resourceUrl =  'api/use-logs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date = DateUtils.convertDateTimeFromServer(data.date);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
