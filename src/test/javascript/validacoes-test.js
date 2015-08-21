'use strict';

var validador = require('validacoes');

describe('validação >', function () {
  describe('servico >', function () {
    it('deve ter nome válido', function () {
      expect(validador.nome('nome de testes')).toBeUndefined();
    });
    it('deve obrigar ter nome', function () {
      expect(validador.nome()).toBe('nome-obrigatorio');
      expect(validador.nome('')).toBe('nome-obrigatorio');
    });
    it('nome deve ter no máximo 150 caracteres', function () {
      expect(validador.nome('nome maior que 150????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????')).toBe('nome-max-150');
    });

    it('deve permitir não ter sigla', function () {
      expect(validador.sigla('')).toBeUndefined();
      expect(validador.sigla()).toBeUndefined();
    });

    it('sigla deve ter no máximo 15 caracteres', function () {
      expect(validador.sigla('012345678901234')).toBeUndefined();
      expect(validador.sigla('0123456789012345')).toBe('sigla-max-15');
    });

  });

});