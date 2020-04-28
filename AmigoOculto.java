import java.util.Scanner;

/**
 * Projeto CRUD - Amigo Oculto (Projeto de um sistema baseado em arquivos)
 * (Projeto feito na disciplina de AEDs-III)
 * @author Rafael Mourão Cerqueira Figueiredo
 * @version 1.0
 */
public class AmigoOculto{
    private static final Scanner leitor = new Scanner(System.in);
    private static final String version = "1.0"; // Guarda a versao atual do sistema
    public static final String cabecalho = "AMIGO OCULTO " + version + "================\n";
    public static CRUD<Usuario> Usuarios;
    public static CRUD<Sugestao> Sugestoes;

    public static void main(String args[]) throws Exception{
        start();
        int id = MenuDeAcesso.inicio();
    }

    /**
     * Limpa a tela do terminal.
     */
    public static void limparTela(){
        System.out.print("\033[H\033[2J");  
        System.out.flush();
    }

    /**
     * Metodo para aguardar uma recao do usuario (para que o mesmo tenha tempo de ler as mensagens de erro).
     */
    public static void aguardarReacao(){
        System.out.println("Pressione [enter] para continuar...");
        leitor.nextLine();
    }

    /**
     * Inicializa o sistema.
     */
    private static void start() throws Exception{
        Usuarios = new CRUD<>( "users.db", Usuario.class.getDeclaredConstructor( byte[].class) );
        Sugestoes = new CRUD<>( "sugs.db", Sugestao.class.getDeclaredConstructor( byte[].class) );
    }
}