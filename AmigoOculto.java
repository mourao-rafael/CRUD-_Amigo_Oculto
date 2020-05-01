import java.text.*;

/**
 * Classe Main - Projeto CRUD - Amigo Oculto (Projeto de um sistema baseado em arquivos)
 * (Projeto feito na disciplina de AEDs-III)
 */
public class AmigoOculto{
    public static final String version = "1.0"; // Guarda a versao atual do sistema
    public static final String formatacaoData = "dd/MM/aa HH:mm";
    public static final DateFormat dateFormatter = new SimpleDateFormat(formatacaoData.replace('a', 'y'));

    //CRUDs:
    public static CRUD<Usuario> Usuarios;
    public static CRUD<Sugestao> Sugestoes;
    public static CRUD<Grupo> Grupos;
    public static CRUD<Convite> Convites;
    // Arvores de Relacionamentos:
    public static ArvoreBMais_Int_Int RelSugestao;
    public static ArvoreBMais_Int_Int RelGrupo;
    public static ArvoreBMais_Int_Int RelConvite;


    public static int idUsuario; // guarda o id do usuario utilizando o sistema

    public static void main(String args[]) throws Exception{
        start();
        
        idUsuario = MenuDeAcesso.inicio();
        
        if(idUsuario >= 0){
            MenuPrincipal.inicio();
        }

        Menu.limparTela(); // limpar a tela antes de finalizar a execucao do programa
    }

    /**
     * Inicializa o sistema.
     */
    private static void start() throws Exception{
        Usuarios = new CRUD<>( "users.db", Usuario.class.getDeclaredConstructor( byte[].class) );
        Sugestoes = new CRUD<>( "sugs.db", Sugestao.class.getDeclaredConstructor( byte[].class) );
        Grupos = new CRUD<>("grup.db", Grupo.class.getDeclaredConstructor( byte[].class) );
        RelSugestao = new ArvoreBMais_Int_Int(10, "dados/relacionamento.sug.idx");
        RelGrupo = new ArvoreBMais_Int_Int(10, "dados/relacionamento.grup.idx");
    }
}