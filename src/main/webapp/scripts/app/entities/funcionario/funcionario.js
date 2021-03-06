'use strict';

angular.module('tmcApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('funcionario', {
                parent: 'entity',
                url: '/funcionarios',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_ADMIN_CONDOMINIO'],
                    pageTitle: 'tmcApp.funcionario.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/funcionario/funcionarios.html',
                        controller: 'FuncionarioController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('funcionario');
                        $translatePartialLoader.addPart('sexo');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('funcionario.detail', {
                parent: 'entity',
                url: '/funcionario/{id}',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_ADMIN_CONDOMINIO'],
                    pageTitle: 'tmcApp.funcionario.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/funcionario/funcionario-detail.html',
                        controller: 'FuncionarioDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('funcionario');
                        $translatePartialLoader.addPart('sexo');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Funcionario', function ($stateParams, Funcionario) {
                        return Funcionario.get({id: $stateParams.id});
                    }]
                }
            })
            .state('funcionario.new', {
                parent: 'funcionario',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_ADMIN_CONDOMINIO'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/funcionario/funcionario-dialog.html',
                        controller: 'FuncionarioDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null,
                                    nome: null,
                                    cpf: null,
                                    sexo: null,
                                    dataNascimento: null,
                                    email: null,
                                    senha: null,
                                    ativo: null,
                                    cepId: null,
                                    cep: null,
                                    logradouro: null,
                                    bairro: null,
                                    cidade: null,
                                    uf: null,
                                    numero: null,
                                    complemento: null,
                                    condominioId: null,
                                    condominioRazaoSocial: null,
                                    categorias: []
                                };
                            }
                        }
                    }).result.then(function (result) {
                        $state.go('funcionario', null, {reload: true});
                    }, function () {
                        $state.go('funcionario');
                    })
                }]
            })
            .state('funcionario.edit', {
                parent: 'funcionario',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_ADMIN_CONDOMINIO'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/funcionario/funcionario-dialog.html',
                        controller: 'FuncionarioDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Funcionario', function (Funcionario) {
                                return Funcionario.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function (result) {
                        $state.go('funcionario', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            });
    });
