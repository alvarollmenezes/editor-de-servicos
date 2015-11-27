package br.gov.servicos.editor.usuarios.recuperarsenha;

import br.gov.servicos.editor.usuarios.*;
import br.gov.servicos.editor.usuarios.token.TokenRepository;
import br.gov.servicos.editor.usuarios.token.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.lang.Long.valueOf;

@Component
public class RecuperacaoSenhaService {

    @Autowired
    private GeradorToken geradorToken;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenRepository repository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RecuperacaoSenhaValidator validator;

    @Value("${eds.max-tentativas-token}")
    private int maxTentativasToken;

    private Clock clock = Clock.systemUTC();

    public String gerarTokenParaUsuario(String usuarioId) {
        String token = geradorToken.gerar();
        Token tokenRecuperacaoSenha = new Token()
                                                            .withUsuario(new Usuario().withId(valueOf(usuarioId)))
                                                            .withDataCriacao(LocalDateTime.now(clock))
                                                            .withTentativasSobrando(maxTentativasToken)
                                                            .withToken(passwordEncoder.encode(token));
        repository.save(tokenRecuperacaoSenha);
        usuarioService.desabilitarUsuario(usuarioId);
        return token;
    }

    public void trocarSenha(FormularioRecuperarSenha formulario) throws TokenInvalido {
        Long usuarioId = formulario.getUsuarioId();
        Token token = findLatestTokenByUsuarioId(usuarioId);
        Usuario usuario = token.getUsuario();

        Optional<TokenError> tokenError = validator.hasError(formulario, token);
        if(!tokenError.isPresent()) {
            usuarioService.save(usuario
                    .withSenha(passwordEncoder.encode(formulario.getCamposSenha().getSenha()))
                    .withHabilitado(true));
            repository.delete(token.getId());
        } else if(tokenError.get().equals(TokenError.INVALIDO)){
            Token novoToken = token.decrementarTentativasSobrando();
            repository.save(novoToken);
            throw new TokenExpirado.CpfTokenInvalido(novoToken.getTentativasSobrando());
        } else {
            throw new TokenExpirado();
        }
    }

    private Token findLatestTokenByUsuarioId(Long usuarioId) {
        Iterable<Token> tokensUsuario = repository.findByUsuarioIdOrderByDataCriacaoAsc(usuarioId);
        if(!tokensUsuario.iterator().hasNext()) {
            throw new UsuarioInexistenteException();
        }
        return tokensUsuario.iterator().next();
    }
}