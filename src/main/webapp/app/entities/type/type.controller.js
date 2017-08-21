(function() {
    'use strict';

    angular
        .module('kucunApp')
        .controller('TypeController', TypeController);

    TypeController.$inject = ['Type'];

    function TypeController(Type) {

        var vm = this;

        vm.types = [];

        loadAll();

        function loadAll() {
            Type.query(function(result) {
                vm.types = result;
                vm.searchQuery = null;
            });
        }
    }
})();
