(function() {
    'use strict';

    angular
        .module('kucunApp')
        .controller('TypeDetailController', TypeDetailController);

    TypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Type'];

    function TypeDetailController($scope, $rootScope, $stateParams, previousState, entity, Type) {
        var vm = this;

        vm.type = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('kucunApp:typeUpdate', function(event, result) {
            vm.type = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
