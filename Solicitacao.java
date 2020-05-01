import java.lang.reflect.*;
import java.util.Date;
import java.util.regex.Pattern;

public class Solicitacao extends AmigoOculto{
    public static final String solicitacaoPadrao = "Por favor, digite os dados:"; // mensagem de solicitacao padrao
    private String mensagemErro;
    private String solicitacao;
    private Method validacao;
    private boolean acceptEmptyLine; // dita se o dado a ser solicitado pode ou nao ser vazio

    // Construtores:
    Solicitacao(String solicitacao, Method validacao){
        this(solicitacao, validacao, Validacao.erroPadrao, false);
    }
    Solicitacao(String solicitacao, Method validacao, boolean acceptEmptyLine){
        this(solicitacao, validacao, Validacao.erroPadrao, acceptEmptyLine);
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
        return (this.validacao == null) || (boolean) validacao.invoke( null, new String(s) );
    }
}

/**
 * Classe para validacao de dados.
 */
abstract class Validacao extends AmigoOculto{
    public static final String erroPadrao = "Erro! Valor Inválido! Tecle [enter] para tentar novamente: "; // mensagem de erro padrao
    private static String ultimoEmailUsado;
    private static Date ultimaDataUsada;

    public static boolean ehFloat(String dado){
        return dado.length()>0 && (dado.replaceAll("[0-9]", "").length()==0 || dado.replaceAll("[0-9]", "").replace(',', '.').equals("."));
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

    public static boolean ehData(String dado){
        String dateRegex = "^[0-3]?[0-9]/[0-3]?[0-9]/(?:[0-9]{2})?[0-9]{2}$";
        return Pattern.compile(dateRegex) .matcher(dado).matches();
    }

    public static boolean ehHora(String dado){
        String dados[] = dado.split(":");
        return (dados.length==2  &&  dados[0].length()==2 && Integer.parseInt(dados[0].replaceAll("^[0-9]", ""))<24  &&  dados[1].length()==2 && Integer.parseInt(dados[1].replaceAll("^[0-9]", ""))<60);
    }

    public static boolean dataSorteio(String dado) throws Exception{
        boolean dataValida = false;
        String []dados = dado.split(" ");
        
        if( dados.length==2  &&  ehData(dados[0])  &&  ehHora(dados[1]) ){
            Date in = dateFormatter.parse(dado);
            dataValida = in.getTime() > new Date().getTime(); // garantir que a data inserida eh maior que a data atual

            ultimaDataUsada = in; // salvar data do sorteio
        }
        
        return dataValida;
    }

    public static boolean dataEncontro(String dado)  throws Exception{
        boolean dataValida = false;
        String dados[] = dado.split(" ");

        if( dados.length==2  &&  ehData(dados[0])  &&  ehHora(dados[1]) ){
            Date in = dateFormatter.parse(dado);
            dataValida = in.getTime() > ultimaDataUsada.getTime();
        }

        return dataValida;
    }
}