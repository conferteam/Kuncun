(function() {
    'use strict';

    angular
        .module('kucunApp')
        .controller('AlarmDetailController', AlarmDetailController);

    AlarmDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Alarm'];

    function AlarmDetailController($scope, $rootScope, $stateParams, previousState, entity, Alarm) {
        var vm = this;

        vm.alarm = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('kucunApp:alarmUpdate', function(event, result) {
            vm.alarm = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
