(function() {
    'use strict';

    angular
        .module('kucunApp')
        .controller('AlarmController', AlarmController);

    AlarmController.$inject = ['Alarm'];

    function AlarmController(Alarm) {

        var vm = this;

        vm.alarms = [];

        loadAll();

        function loadAll() {
            Alarm.query(function(result) {
                vm.alarms = result;
                vm.searchQuery = null;
            });
        }
    }
})();
