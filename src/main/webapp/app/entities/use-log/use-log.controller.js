(function() {
    'use strict';

    angular
        .module('kucunApp')
        .controller('UseLogController', UseLogController);

    UseLogController.$inject = ['UseLog'];

    function UseLogController(UseLog) {

        var vm = this;

        vm.useLogs = [];

        loadAll();

        function loadAll() {
            UseLog.query(function(result) {
                vm.useLogs = result;
                vm.searchQuery = null;
            });
        }
    }
})();
