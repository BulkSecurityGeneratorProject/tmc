'use strict';

angular.module('tmcApp')
    .factory('OcorrenciaPrioridade', function ($resource, DateUtils) {
        return $resource('api/ocorrenciaPrioridades/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
