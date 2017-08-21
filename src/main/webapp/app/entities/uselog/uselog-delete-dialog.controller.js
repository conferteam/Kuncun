(function() {
    'use strict';

    angular
        .module('kucunApp')
        .controller('UselogDeleteController',UselogDeleteController);

    UselogDeleteController.$inject = ['$uibModalInstance', 'entity', 'Uselog'];

    function UselogDeleteController($uibModalInstance, entity, Uselog) {
        var vm = this;

        vm.uselog = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Uselog.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
