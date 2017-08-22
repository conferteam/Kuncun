(function() {
    'use strict';

    angular
        .module('kucunApp')
        .controller('UseLogDetailController', UseLogDetailController);

    UseLogDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'UseLog'];

    function UseLogDetailController($scope, $rootScope, $stateParams, previousState, entity, UseLog) {
        var vm = this;

        vm.useLog = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('kucunApp:useLogUpdate', function(event, result) {
            vm.useLog = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
