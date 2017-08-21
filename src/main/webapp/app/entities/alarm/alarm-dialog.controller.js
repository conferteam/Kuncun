(function() {
    'use strict';

    angular
        .module('kucunApp')
        .controller('AlarmDialogController', AlarmDialogController);

    AlarmDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Alarm'];

    function AlarmDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Alarm) {
        var vm = this;

        vm.alarm = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.alarm.id !== null) {
                Alarm.update(vm.alarm, onSaveSuccess, onSaveError);
            } else {
                Alarm.save(vm.alarm, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('kucunApp:alarmUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
