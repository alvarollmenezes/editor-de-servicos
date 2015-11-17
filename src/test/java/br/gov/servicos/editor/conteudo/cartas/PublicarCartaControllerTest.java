package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.conteudo.ConteudoVersionadoFactory;
import br.gov.servicos.editor.fixtures.UserProfileConfigParaTeste;
import br.gov.servicos.editor.git.Metadados;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PublicarCartaControllerTest {

    @Mock
    Carta carta;

    @Mock
    ConteudoVersionadoFactory factory;

    PublicarCartaController controller;

    UserProfileConfigParaTeste userProfiles = new UserProfileConfigParaTeste();


    @Before
    public void setUp() throws Exception {
        controller = new PublicarCartaController(factory, userProfiles);
        given(carta.getMetadados())
                .willReturn(new Metadados<>());
        given(factory.pagina(anyString(), any()))
                .willReturn(carta);
    }

    @Test
    public void publicaCartaExistente() throws Exception {
        controller.publicar("");
        verify(carta).publicar(userProfiles.get());
    }

}