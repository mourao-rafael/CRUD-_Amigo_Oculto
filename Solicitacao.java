import java.lang.reflect.*;

public class Solicitacao extends TUI{
    public static final String solicitacaoPadrao = "Por favor, digite os dados:"; // mensagem de solicitacao padrao
    private String mensagemErro;
    private String solicitacao;
    private Method validacao;
    private boolean acceptEmptyLine; // dita se o dado a ser solicitado pode ou nao ser vazio

    // Construtores:
    Solicitacao(String solicitacao){
        this(solicitacao, null, erroPadrao, false);
    }
    Solicitacao(String solicitacao, Method validacao){
        this(solicitacao, validacao, erroPadrao, false);
    }
    Solicitacao(String solicitacao, Method validacao, boolean acceptEmptyLine){
        this(solicitacao, validacao, erroPadrao, acceptEmptyLine);
    }
    Solicitacao(String solicitacao, Method validacao, String mensagemErro){
        this(solicitacao, validacao, mensagemErro, false);
    }
    Solicitacao(String solicitacao, Method validacao, String mensagemErro, boolean acceptEmptyLine){
        this.solicitacao = solicitacao;
        this.validacao = validacao;
        this.mensagemErro = mensagemErro;
        this.acceptEmptyLine = acceptEmptyLine;
    }

    // Getter's:
    public String getSolicitacao(){ return this.acceptEmptyLine ? this.solicitacao : this.solicitacao + " (aperte [enter] para cancelar)"; }
    public String getMensagemErro(){ return this.mensagemErro; }
    public boolean acceptEmptyLine(){ return this.acceptEmptyLine; }
    
    // Metodos:
    /**
     * Valida a entrada
     * @return TRUE se for valida, FALSE se nao for.
     */
    public boolean validar(String s) throws Exception{
        Validacao.setMensagemErro(this.mensagemErro);
        boolean valida = (this.validacao == null) || (boolean) validacao.invoke( null, new String(s) );
        if(!valida) valorInvalido( Validacao.getMensagemErro() );
        return valida;
    }
}