'use strict';

var Gratuidade = require('servico/modelos').Gratuidade;

module.exports = {

  controller: function (args) {
    this.gratuidade = args.gratuidade;
    this.custos = args.custos;

    this.temCusto = function () {
      return !_.isEmpty(this.custos.casoPadrao().campos()) || !_.isEmpty(this.custos.outrosCasos());
    };
  },

  view: function (ctrl) {
    if (ctrl.gratuidade === Gratuidade.GRATUITO) {
      return m('');
    }

    var camposView = function (campos) {
      return campos.map(function (campo) {
        return m('li', [
                    m('span', campo.descricao()),
                    m('span', !_.isEmpty(campo.moeda()) ? campo.moeda() : ' R$ '),
                    m('span', campo.valor())
                ]);
      });
    };

    var custoPadraoView = function (cp) {
      return m('ul', camposView(cp.campos()));
    };

    var outrosCustosView = function (casos) {
      return casos.map(function (caso) {
        return m('ul.caso-descricao', [
                    m('.info-etapa', caso.descricao()),
                    camposView(caso.campos())
                ]);
      });
    };

    if (ctrl.temCusto()) {
      return m('.subtitulo-etapa', [
                    m('p.titulo-documento', 'Custos'),
                    m('p.info-etapa', ''),
                    custoPadraoView(ctrl.custos.casoPadrao()),
                    outrosCustosView(ctrl.custos.outrosCasos())
                ]);
    }
    return m.component(require('servico/visualizar/view-vazia'));

  }
};
