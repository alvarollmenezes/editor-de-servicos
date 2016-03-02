package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.conteudo.ConteudoVersionado;
import br.gov.servicos.editor.conteudo.ConteudoVersionadoFactory;
import br.gov.servicos.editor.conteudo.TipoPagina;
import br.gov.servicos.editor.security.TipoPermissao;
import br.gov.servicos.editor.security.UserProfiles;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.access.AccessDeniedException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;

@RunWith(MockitoJUnitRunner.class)
public class DespublicarPaginaControllerTest {

    @Mock
    ConteudoVersionado carta;

    @Mock
    ConteudoVersionadoFactory factory;

    @Mock
    UserProfiles userProfiles;

    @Test(expected = AccessDeniedException.class)
    public void retornarAcessoNegadoCasoUsuarioNaoTenhaAcesso() throws ConteudoInexistenteException {
        DespublicarPaginaController controller = new DespublicarPaginaController(factory, userProfiles);
        given(factory.pagina(anyString(), any())).willReturn(carta);
        given(carta.existe()).willReturn(true);
        given(userProfiles.temPermissaoParaTipoPagina(eq(TipoPermissao.DESPUBLICAR), any(TipoPagina.class))).willReturn(false);
        given(userProfiles.temPermissaoParaTipoPaginaOrgaoEspecifico(eq(TipoPermissao.DESPUBLICAR), any(TipoPagina.class), anyString())).willReturn(false);

        controller.despublicar("servico", "");
    }

}