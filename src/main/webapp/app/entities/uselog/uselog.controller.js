(function() {
    'use strict';

    angular
        .module('kucunApp')
        .controller('UselogController', UselogController);

    UselogController.$inject = ['Uselog'];

    function UselogController(Uselog) {

        var vm = this;

        vm.uselogs = [];

        loadAll();

        function loadAll() {
            Uselog.query(function(result) {
                vm.uselogs = result;
                vm.searchQuery = null;
            });
        }
    }
})();
