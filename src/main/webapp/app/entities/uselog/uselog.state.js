(function() {
    'use strict';

    angular
        .module('kucunApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('uselog', {
            parent: 'entity',
            url: '/uselog',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'kucunApp.uselog.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/uselog/uselogs.html',
                    controller: 'UselogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('uselog');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('uselog-detail', {
            parent: 'uselog',
            url: '/uselog/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'kucunApp.uselog.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/uselog/uselog-detail.html',
                    controller: 'UselogDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('uselog');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Uselog', function($stateParams, Uselog) {
                    return Uselog.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'uselog',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('uselog-detail.edit', {
            parent: 'uselog-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/uselog/uselog-dialog.html',
                    controller: 'UselogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Uselog', function(Uselog) {
                            return Uselog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('uselog.new', {
            parent: 'uselog',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/uselog/uselog-dialog.html',
                    controller: 'UselogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name_id: null,
                                name: null,
                                type: null,
                                count: null,
                                username: null,
                                date: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('uselog', null, { reload: 'uselog' });
                }, function() {
                    $state.go('uselog');
                });
            }]
        })
        .state('uselog.edit', {
            parent: 'uselog',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/uselog/uselog-dialog.html',
                    controller: 'UselogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Uselog', function(Uselog) {
                            return Uselog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('uselog', null, { reload: 'uselog' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('uselog.delete', {
            parent: 'uselog',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/uselog/uselog-delete-dialog.html',
                    controller: 'UselogDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Uselog', function(Uselog) {
                            return Uselog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('uselog', null, { reload: 'uselog' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
