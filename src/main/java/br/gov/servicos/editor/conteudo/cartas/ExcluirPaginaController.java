package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.conteudo.ConteudoVersionado;
import br.gov.servicos.editor.conteudo.ConteudoVersionadoFactory;
import br.gov.servicos.editor.conteudo.TipoPagina;
import br.gov.servicos.editor.security.CheckOrgaoEspecificoController;
import br.gov.servicos.editor.security.TipoPermissao;
import br.gov.servicos.editor.security.UserProfiles;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static br.gov.servicos.editor.conteudo.TipoPagina.ORGAO;
import static br.gov.servicos.editor.conteudo.TipoPagina.fromNome;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

@Slf4j
@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
class ExcluirPaginaController extends CheckOrgaoEspecificoController {

    UserProfiles userProfiles;
    ConteudoVersionadoFactory factory;

    @Autowired
    public ExcluirPaginaController(UserProfiles userProfiles, ConteudoVersionadoFactory factory) {
        this.userProfiles = userProfiles;
        this.factory = factory;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/editar/api/pagina/{tipo}/{id}", method = DELETE)
    void remover(@PathVariable("tipo") String tipo, @PathVariable("id") String id) throws ConteudoInexistenteException {
        TipoPagina tipoPagina = fromNome(tipo);
        ConteudoVersionado conteudoVersionado = factory.pagina(id, tipoPagina);

        if (tipoPagina == ORGAO) {
            throw new IllegalArgumentException("tipo não pode ser: " + tipoPagina.getNome());
        }

        if (!conteudoVersionado.existe()) {
            throw new ConteudoInexistenteException(conteudoVersionado);
        }

        if (!usuarioPodeRealizarAcao(userProfiles, tipoPagina, conteudoVersionado.getOrgaoId())) {
            throw new AccessDeniedException("Usuário sem permissão");
        }

        conteudoVersionado.remover(userProfiles.get());
    }

    @Override
    public TipoPermissao getTipoPermissao() {
        return TipoPermissao.EXCLUIR;
    }
}
