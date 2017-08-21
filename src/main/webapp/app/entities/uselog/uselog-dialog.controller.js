(function() {
    'use strict';

    angular
        .module('kucunApp')
        .controller('UselogDialogController', UselogDialogController);

    UselogDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Uselog'];

    function UselogDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Uselog) {
        var vm = this;

        vm.uselog = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.uselog.id !== null) {
                Uselog.update(vm.uselog, onSaveSuccess, onSaveError);
            } else {
                Uselog.save(vm.uselog, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('kucunApp:uselogUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
