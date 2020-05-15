import java.text.*; // java.text.DateFormat && java.text.SimpleDateFormat

/**
 * Classe Main - Projeto CRUD - Amigo Oculto (Projeto de um sistema baseado em arquivos)
 * (Projeto feito na disciplina de AEDs-III)
 */
public class AmigoOculto{
    public static final String version = "1.0"; // Guarda a versao atual do sistema
    public static final String formatacaoData = "dd/MM/aa HH:mm";
    public static final DateFormat dateFormatter = new SimpleDateFormat(formatacaoData.replace('a', 'y'));
    
    // CRUDs:
    public static CRUD<Usuario> Usuarios;
    public static CRUD<Sugestao> Sugestoes;
    public static CRUD<Grupo> Grupos;
    public static CRUD<Convite> Convites;
    public static CRUD<Participacao> Participacoes;
    // Arvores de Relacionamentos:
    public static ArvoreBMais_Int_Int RelSugestao;
    public static ArvoreBMais_Int_Int RelGrupo;
    public static ArvoreBMais_Int_Int RelConvite;
    public static ArvoreBMais_Int_Int RelParticipacao_Grupo;
    public static ArvoreBMais_Int_Int RelParticipacao_Usuario;
    
    public static ArvoreBMais_ChaveComposta_String_Int convPendentes;
    public static int idUsuario = -1; // guarda o id do usuario utilizando o sistema

    public static void main(String args[]) throws Exception{
        start();

        // MENU DE ACESSO:
        Opcao<?>[] opAcesso = new Opcao[]{
            new Opcao<Rotina>("Login", new Rotina("login")),
            new Opcao<Rotina>("Novo usuário", new Rotina("novoUsuario"))
        };
        Menu menuAcesso = new Menu( opAcesso, "Selecione opção de acesso", "loginRealizado" );
        menuAcesso.executar(); // executar menu de acesso


        // INICIALIZAR TODOS OS OUTROS MENUS, E EXECUTAR O MENU PRINCIPAL:
        if(idUsuario != -1){
            // MENU PARTICIPANTES:
            Opcao<?>[] opParticipantes = new Opcao[]{
                new Opcao<Rotina>("Listagem", new Rotina("listarPart")),
                new Opcao<Rotina>("Remoção", new Rotina("removerPart"))
            };
            Menu menuParticipantes = new Menu(opParticipantes);
            
            // MENU CONVITES:
            Opcao<?>[] opConvites = new Opcao[]{
                new Opcao<Rotina>("Listagem dos convites", new Rotina("listarConv")),
                new Opcao<Rotina>("Emissão de convites", new Rotina("emitir")),
                new Opcao<Rotina>("Cancelamento de convites", new Rotina("cancelar"))
            };
            Menu menuConvites = new Menu(opConvites);
            
            // MENU GERENCIAR GRUPOS:
            Opcao<?>[] opGerenciarGrupos = new Opcao[]{
                new Opcao<Rotina>("Listar", new Rotina("listarGrup")),
                new Opcao<Rotina>("Incluir", new Rotina("incluirGrup")),
                new Opcao<Rotina>("Alterar", new Rotina("alterarGrup")),
                new Opcao<Rotina>("Desativar", new Rotina("desativarGrup"))
            };
            Menu menuGerenciarGrupos = new Menu(opGerenciarGrupos);
            
            // MENU GERENCIAMENTO:
            Opcao<?>[] opGerenciamento = new Opcao[]{
                new Opcao<Menu>("Gerenciar grupos", menuGerenciarGrupos),
                new Opcao<Menu>("Convites", menuConvites),
                new Opcao<Menu>("Participantes", menuParticipantes), // TODO
                new Opcao<Menu>("Sorteio", null) // TODO
            };
            Menu menuGerenciamento = new Menu(opGerenciamento);

            // MENU GRUPOS:
            Opcao<?>[] opGrupos = new Opcao[]{
                new Opcao<Menu>("Gerenciamento", menuGerenciamento),
                new Opcao<Rotina>("Participação", new Rotina("participacao"))
            };
            Menu menuGrupos = new Menu(opGrupos);

            // MENU SUGESTOES:
            Opcao<?>[] opSugestoes = new Opcao[]{
                new Opcao<Rotina>("Listar", new Rotina("listarSug")),
                new Opcao<Rotina>("Incluir", new Rotina("incluirSug")),
                new Opcao<Rotina>("Alterar", new Rotina("alterarSug")),
                new Opcao<Rotina>("Excluir", new Rotina("excluirSug"))
            };
            Menu menuSugestoes = new Menu(opSugestoes);

            // MENU PRINCIPAL:
            Opcao<?>[] opPrincipal = new Opcao[]{
                new Opcao<Menu>("Sugestões de presentes", menuSugestoes),
                new Opcao<Menu>("Meus grupos", menuGrupos),
                new Opcao<Rotina>("Convites pendentes", new Rotina("convitesPendentes"))
            };
            Menu menuPrincipal = new Menu(opPrincipal);

            // Por fim, executar o menu principal:
            menuPrincipal.executar();
        }

        System.out.print("\u001b[1;1H" + TUI.limparAposCursor); // limpar a tela antes de finalizar a execucao do programa
    }

    /**
     * Inicializa o sistema.
     */
    private static void start() throws Exception{
        // CRUDs:
        Usuarios = new CRUD<>( "users.db", Usuario.class.getDeclaredConstructor( byte[].class) );
        Sugestoes = new CRUD<>( "sugs.db", Sugestao.class.getDeclaredConstructor( byte[].class) );
        Grupos = new CRUD<>("grup.db", Grupo.class.getDeclaredConstructor( byte[].class) );
        Convites = new CRUD<>("conv.db", Convite.class.getDeclaredConstructor( byte[].class) );
        Participacoes = new CRUD<>("part.db", Participacao.class.getDeclaredConstructor(byte[].class));

        // ARVORES DE RELACIONAMENTO:
        RelSugestao = new ArvoreBMais_Int_Int(10, "dados/relacionamento.sug.idx");
        RelGrupo = new ArvoreBMais_Int_Int(10, "dados/relacionamento.grup.idx");
        RelConvite = new ArvoreBMais_Int_Int(10, "dados/relacionamento.conv.idx");
        RelParticipacao_Grupo = new ArvoreBMais_Int_Int(10, "dados/relacionamento.partgrup.idx");
        RelParticipacao_Usuario = new ArvoreBMais_Int_Int(10,"dados/relacionamento.partusuer.idx");

        TUI.start(); // inicializar a interface de usuario
    }
}