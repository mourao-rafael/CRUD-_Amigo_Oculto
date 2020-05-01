import java.lang.reflect.*;

public class Solicitacao extends AmigoOculto{
    public static final String solicitacaoPadrao = "Por favor, digite os dados:"; // mensagem de solicitacao padrao
    private String mensagemErro;
    private String solicitacao;
    private Method validacao;

    // Construtores:
    Solicitacao(String solicitacao, Method validacao){
        this(solicitacao, validacao, Validacao.erroPadrao);
    }
    Solicitacao(String solicitacao, Method validacao, String mensagemErro){
        this.solicitacao = solicitacao;
        this.validacao = validacao;
        this.mensagemErro = mensagemErro;
    }

    // Getter's:
    public String getSolicitacao(){ return this.solicitacao; }
    public String getMensagemErro(){ return this.mensagemErro; }
    
    // Metodos:
    /**
     * Valida a entrada
     * @return TRUE se for valida, FALSE se nao for.
     */
    public boolean validar(String s) throws Exception{
        return (this.validacao == null) || (boolean) validacao.invoke( null, new String(s) );
    }
}

/**
 * Classe para validacao de dados.
 */
abstract class Validacao extends AmigoOculto{
    public static final String erroPadrao = "Erro! Valor Inválido! Tecle [enter] para tentar novamente: "; // mensagem de erro padrao
    private static String ultimoEmailUsado;

    public static boolean validarFloat(String dado){
        return dado.replace('.',',').replaceAll("[^0-9]", "").replaceFirst(",", "").length() == 0;
    }

    public static boolean emailNaoCadastrado(String dado) throws Exception{
        return !emailCadastrado(dado);
    }

    public static boolean emailCadastrado(String dado) throws Exception{
        ultimoEmailUsado = dado;
        return Usuarios.read(dado) != null;
    }

    public static boolean senhaCorreta(String dado) throws Exception{
        return Usuarios.read(ultimoEmailUsado).getSenha().equals( dado );
    }
}