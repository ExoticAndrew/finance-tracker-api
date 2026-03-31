package despesaspessoais.exception;

public class TransacaoNotFoundException extends RuntimeException {
    public TransacaoNotFoundException(Long id) {
        super("ID não encontrado " + id);
    }
}
