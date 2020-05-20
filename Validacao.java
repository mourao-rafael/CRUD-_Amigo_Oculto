import java.util.Date;
import java.util.regex.Pattern;

/**
 * Classe para validacao de dados.
 */
public abstract class Validacao extends TUI{
    // Atributos de controle da classe:
    private static String ultimoEmailUsado;
    private static Date ultimaDataUsada;
    private static String mensagemErro;

    public static void setMensagemErro(String mensagemErro){
        Validacao.mensagemErro = mensagemErro;
    }
    public static String getMensagemErro(){
        return Validacao.mensagemErro;
    }

    // USADAS NAS SOLICITACOES DE DADOS:
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

    public static boolean validaEmissaoConvite(String dado) throws Exception{
        boolean valido = emailCadastrado(dado);
        if(valido){
            valido = !dado.equals( Usuarios.read(idUsuario).chaveSecundaria() ); // nao permitir que o adm envie um convite para si mesmo
            if(!valido) Validacao.mensagemErro = "Erro! Você não pode emitir um convite para si mesmo!";
        }
        return valido;
    }


    // USADAS EM OUTROS LUGARES:
    public static boolean loginRealizado(){
        return idUsuario != -1;
    }

    public static boolean validaNumero(String s, int min, int max){
        boolean valido = false;

        try{
            int i = Integer.valueOf(s);
            if(i>=min && i<=max) valido = true;
        } catch(Exception e){}

        return valido;
    }
}