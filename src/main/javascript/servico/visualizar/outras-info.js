'use strict';

var referencia = require('referencia');

module.exports = {

  controller: function (args) {
    this.servico = args;
    this.nomeOrgao = m.prop('');
    this.converter = new window.showdown.Converter();

    this.obterOrgao = function (orgao) {
      m.request({
        method: 'GET',
        url: '/editar/api/orgao',
        data: {
          urlOrgao: orgao
        },
        deserialize: function (data) {
          return data;
        }

      }).then(_.bind(function (nome) {
        this.nomeOrgao(nome);
      }, this));
    };

    this.ehFeminino = function () {
      return _.includes(referencia.orgaosFemininos, _.chain(this.nomeOrgao().toLowerCase()).words().first().value());
    };

    this.obterOrgao(this.servico.orgao().nome());
  },

  view: function (ctrl) {
    var nomesPopularesView = function () {
      if (!_.isEmpty(ctrl.servico.nomesPopulares())) {
        return m('.row', m('p', [
                    'Você também pode conhecer este serviço como: ',
                    ctrl.servico.nomesPopulares().join(', '),
                    '.'
                ]));
      }
      return m.component(require('servico/visualizar/view-vazia'));
    };

    var gratuidadeView = function (ehGratuito) {
      if (ehGratuito) {
        return m('', m('p', 'Este serviço é gratuito para o cidadão.'));
      }
      return '';
    };

    return m('', [
            m('h3#servico-outras-info.subtitulo-servico', 'Outras informações'),
            m('.info-extra', [
                nomesPopularesView(),
                gratuidadeView(ctrl.servico.gratuidade())
            ]),
            m('.row', m('p.separacao-orgao', [
                'Este é um serviço ',
                ctrl.ehFeminino() ? 'da ' : 'do ',
                m('a', ctrl.nomeOrgao()),
                '. Em caso de dúvidas, reclamações ou sugestões favor contactá-',
                ctrl.ehFeminino() ? 'la.' : 'lo.'
            ]),
        m('p.orgao-contato', [
                'Para mais informações ou dúvidas sobre este serviço, entre em contato: ',
                m.trust(ctrl.converter.makeHtml(ctrl.servico.orgao().contato()))
            ])),
            m('hr', {
        style: {
          'margin-top': '20px',
          border: 'none'
        }
      })
    ]);
  }
};
