/**
 * Classe Main - Projeto CRUD - Amigo Oculto (Projeto de um sistema baseado em arquivos)
 * (Projeto feito na disciplina de AEDs-III)
 */
public class AmigoOculto{
    public static final String version = "1.0"; // Guarda a versao atual do sistema
    public static CRUD<Usuario> Usuarios;
    public static CRUD<Sugestao> Sugestoes;

    public static void main(String args[]) throws Exception{
        start();
        int id = MenuDeAcesso.inicio();

        if(id != -1){
            MenuPrincipal.idUsuario = id;
            MenuPrincipal.inicio();
        }
    }

    /**
     * Inicializa o sistema.
     */
    private static void start() throws Exception{
        Usuarios = new CRUD<>( "users.db", Usuario.class.getDeclaredConstructor( byte[].class) );
        Sugestoes = new CRUD<>( "sugs.db", Sugestao.class.getDeclaredConstructor( byte[].class) );
    }
}