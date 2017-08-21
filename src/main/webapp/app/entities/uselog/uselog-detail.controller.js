(function() {
    'use strict';

    angular
        .module('kucunApp')
        .controller('UselogDetailController', UselogDetailController);

    UselogDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Uselog'];

    function UselogDetailController($scope, $rootScope, $stateParams, previousState, entity, Uselog) {
        var vm = this;

        vm.uselog = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('kucunApp:uselogUpdate', function(event, result) {
            vm.uselog = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
