'use strict';

var ultimoErro = require('utils/ultimo-erro');

module.exports = function (resposta) {
  if (typeof resposta === 'string' && _.trim(resposta) !== '') {
    ultimoErro.set(resposta);
  } else {
    ultimoErro.clear();
  }
  m.route('/editar/erro');
  throw resposta;
};
