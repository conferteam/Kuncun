(function() {
    'use strict';
    angular
        .module('kucunApp')
        .factory('Alarm', Alarm);

    Alarm.$inject = ['$resource'];

    function Alarm ($resource) {
        var resourceUrl =  'api/alarms/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
