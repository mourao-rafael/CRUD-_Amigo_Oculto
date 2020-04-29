/**
 * Classe Main - Projeto CRUD - Amigo Oculto (Projeto de um sistema baseado em arquivos)
 * (Projeto feito na disciplina de AEDs-III)
 */
public class AmigoOculto{
    public static final String version = "1.0"; // Guarda a versao atual do sistema

    public static CRUD<Usuario> Usuarios;
    public static CRUD<Sugestao> Sugestoes;
    public static CRUD<Grupo> Grupo;
    public static ArvoreBMais_Int_Int Relacionamento_SugestaoUsuario;
    public static ArvoreBMais_Int_Int Relacionamento_GrupoUsuario;

    public static int idUsuario; // guarda o id do usuario utilizando o sistema

    public static void main(String args[]) throws Exception{
        start();

        idUsuario = MenuDeAcesso.inicio();
        System.out.println(idUsuario);

        if(idUsuario >= 0){
            MenuPrincipal.inicio();
        }
    }

    /**
     * Inicializa o sistema.
     */
    private static void start() throws Exception{
        Usuarios = new CRUD<>( "users.db", Usuario.class.getDeclaredConstructor( byte[].class) );
        Sugestoes = new CRUD<>( "sugs.db", Sugestao.class.getDeclaredConstructor( byte[].class) );
        Grupo = new CRUD<>("grup.db", Grupo.class.getDeclaredConstructor( byte[].class) );
        Relacionamento_SugestaoUsuario = new ArvoreBMais_Int_Int(10, "dados/relacionamento.sug.idx");
        Relacionamento_GrupoUsuario = new ArvoreBMais_Int_Int(10, "dados/relacionamento.grup.idx");
    }
}