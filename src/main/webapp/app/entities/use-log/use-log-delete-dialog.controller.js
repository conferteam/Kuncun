(function() {
    'use strict';

    angular
        .module('kucunApp')
        .controller('UseLogDeleteController',UseLogDeleteController);

    UseLogDeleteController.$inject = ['$uibModalInstance', 'entity', 'UseLog'];

    function UseLogDeleteController($uibModalInstance, entity, UseLog) {
        var vm = this;

        vm.useLog = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            UseLog.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
