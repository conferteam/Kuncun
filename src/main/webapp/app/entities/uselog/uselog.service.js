(function() {
    'use strict';
    angular
        .module('kucunApp')
        .factory('Uselog', Uselog);

    Uselog.$inject = ['$resource', 'DateUtils'];

    function Uselog ($resource, DateUtils) {
        var resourceUrl =  'api/uselogs/:id';

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
