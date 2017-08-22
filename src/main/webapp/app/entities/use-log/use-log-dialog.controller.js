(function() {
    'use strict';

    angular
        .module('kucunApp')
        .controller('UseLogDialogController', UseLogDialogController);

    UseLogDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'UseLog'];

    function UseLogDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, UseLog) {
        var vm = this;

        vm.useLog = entity;
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
            if (vm.useLog.id !== null) {
                UseLog.update(vm.useLog, onSaveSuccess, onSaveError);
            } else {
                UseLog.save(vm.useLog, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('kucunApp:useLogUpdate', result);
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
