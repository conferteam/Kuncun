(function() {
    'use strict';

    angular
        .module('kucunApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('use-log', {
            parent: 'entity',
            url: '/use-log',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'kucunApp.useLog.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/use-log/use-logs.html',
                    controller: 'UseLogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('useLog');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('use-log-detail', {
            parent: 'use-log',
            url: '/use-log/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'kucunApp.useLog.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/use-log/use-log-detail.html',
                    controller: 'UseLogDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('useLog');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'UseLog', function($stateParams, UseLog) {
                    return UseLog.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'use-log',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('use-log-detail.edit', {
            parent: 'use-log-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/use-log/use-log-dialog.html',
                    controller: 'UseLogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UseLog', function(UseLog) {
                            return UseLog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('use-log.new', {
            parent: 'use-log',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/use-log/use-log-dialog.html',
                    controller: 'UseLogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nameId: null,
                                name: null,
                                type: null,
                                username: null,
                                date: null,
                                count: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('use-log', null, { reload: 'use-log' });
                }, function() {
                    $state.go('use-log');
                });
            }]
        })
        .state('use-log.edit', {
            parent: 'use-log',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/use-log/use-log-dialog.html',
                    controller: 'UseLogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UseLog', function(UseLog) {
                            return UseLog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('use-log', null, { reload: 'use-log' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('use-log.delete', {
            parent: 'use-log',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/use-log/use-log-delete-dialog.html',
                    controller: 'UseLogDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UseLog', function(UseLog) {
                            return UseLog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('use-log', null, { reload: 'use-log' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
