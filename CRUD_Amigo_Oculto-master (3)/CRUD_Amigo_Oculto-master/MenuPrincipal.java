import java.util.Calendar;

public abstract class MenuPrincipal extends Menu{
    // EXECUCAO DO MENU: ====================================================================================
    public static void inicio() throws Exception{
        // Ler opcao:
        char opcao = lerOpcao("INICIO", "Sugestões de presentes,Grupos,Novos convites: 0".split(","));

        switch(opcao){
            case '1':
                SubMenu_Sugestoes.inicio();
                break;
            case '2':
                SubMenu_Grupos.inicio();
                break;
            case '3':
                //
                break;
            default:
                return;
        }

        MenuPrincipal.inicio(); // repetir processo
    }
}

/**
 * Classe abstrata para a implementação do submenu de sugestões.
 */
abstract class SubMenu_Sugestoes extends Menu{
    // EXECUCAO DO MENU: ====================================================================================
    public static void inicio() throws Exception{
        char opcao = lerOpcao("INICIO > SUGESTÕES", "Listar,Incluir,Alterar,Excluir".split(","));

        switch(opcao){
            case '1':
                listar();
                break;
            case '2':
                incluir();
                break;
            case '3':
                alterar();
                break;
            case '4':
                excluir();
                break;
            default:
                return;
        }

        SubMenu_Sugestoes.inicio(); // repetir processo
    }

    // OPERACOES" ===========================================================================================
    /**
     * Lista todas as sugestões cadastradas pelo usuario.
     */
    private static void listar() throws Exception{
        cabecalho("INICIO > SUGESTÕES > LISTAR");
        int[] ids = AmigoOculto.Relacionamento_SugestaoUsuario.read( AmigoOculto.idUsuario ); // obter a lista de IDs das sugestoes ligadas ao usuario

        // Realizar listagem das sugestoes:
        for(int i=0; i<ids.length; i++){
            String dados[] = AmigoOculto.Sugestoes.read(ids[i]) .toString().split("\n"); // extrair os dados de cada sugestao, e sera-los por linha
            System.out.print((i+1));
            for(String s : dados) System.out.print('\t' + s + '\n');
            System.out.println();
        }

        aguardarReacao();
    }

    /**
     * Rotina para incluir uma nova sugestao.
     */
    private static void incluir() throws Exception{
        String path = "INICIO > SUGESTÕES > INCLUIR";
        cabecalho(path);
        System.out.println("Entre com os dados do produto: ");

        // Solicitar nome do produto:
        System.out.print("Produto (aperte [enter] para cancelar): ");
        String in = leitor.nextLine();

        if(in.length() > 0){ // se o produto estiver em branco, retornar
            // Solicitar demais dados do produto:
            Sugestao nova = new Sugestao(); nova.setProduto(in);
            System.out.print("Loja: "); nova.setLoja( leitor.nextLine() ); // cadastrar loja
            do{ // cadastrar valor aproximado:
                System.out.print("Valor aproximado: ");
                in = leitor.nextLine().replace(',', '.');
                if( in.replaceAll("[^0-9.]", "").length() != in.length() ) System.out.println("\nErro! Valor invalido!");
                else if(in.length() > 0) nova.setValor( Float.parseFloat( in ) );
                else break;
            }while(nova.getValor() < 0);
            System.out.print("Observações: "); nova.setObservacoes( leitor.nextLine() ); // cadastrar observacoes

            // Confirmar inclusao da sugestao:
            limparTela();
            cabecalho(path);
            System.out.println("Dados inseridos:\n" + nova.toString());
            System.out.print("\nDigite [enter] para confirmar cadastro OU qualquer valor para CANCELAR: ");

            if(leitor.nextLine().length() == 0){
                int idSugestao = AmigoOculto.Sugestoes.create( nova.toByteArray() );
                AmigoOculto.Relacionamento_SugestaoUsuario.create(AmigoOculto.idUsuario, idSugestao); // inserir par [idUsuario, idSugestao] na arvore de relacionamento
            }
        }
        else{
            System.out.println("Cadastro cancelado!");
            aguardarReacao();
        }

    }

    /**
     * Rotina para alterar determinada sugestao.
     */
    private static void alterar(){
        cabecalho("INICIO > SUGESTÕES > ALTERAR");
    }

    /**
     * Rotina para excluir determinada sugestao.
     */
    private static void excluir(){
        cabecalho("INICIO > SUGESTÕES > EXCLUIR");
    }
}

/**
 * Classe abstrata para a implementação do submenu de grupos.
 */
abstract class SubMenu_Grupos extends Menu{
    // EXECUCAO DO MENU: ====================================================================================
    public static void inicio() throws Exception{
        char opcao = lerOpcao("INICIO > GRUPOS", "Criação e gerenciamento de gupos,Participação nos grupos".split(","));

        switch(opcao){
            case '1':
                gerenciamento();
                break;
            case '2':
                participacaoGrupos();
                break;
            default:
                return;
        }
        MenuPrincipal.inicio();
    }

    private static void participacaoGrupos(){
        cabecalho("INICIO > GRUPOS > PARTICIPAÇÃO GRUPOS");
    }

    private static void gerenciamento() throws Exception{
        char opcao = lerOpcao("INICIO > GRUPOS > GERENCIAMENTO DE GRUPOS", "Grupos,Convites,Participação,Sorteio".split(","));

        switch(opcao){
            case '1':
                grupos();
                break;
            case'2':
                convite();
                break;
            case'3':
                participacao();
                break;
            case '4':
                sorteio();
                break;
            default:
                return;
        }
        SubMenu_Grupos.inicio();
    }

    private static void grupos() throws Exception{
        char opcao = lerOpcao("INICIO > GRUPOS > GERENCIAMENTO DE GRUPOS > GRUPOS", "Listar,Incluir,Alterar,Desativar".split(","));

        switch(opcao){
            case '1':
                listar();
                break;
            case '2':
                incluir();
                break;
            case '3':
                alterar();
                break;
            case '4':
                desativar();
                break;
            default:
                return;
        }
        SubMenu_Grupos.inicio();
    }

    private static void convite() throws Exception{
        char opcao = lerOpcao("INICIO > GRUPOS > GERENCIAMENTO DE GRUPOS > CONVITES", "Listagem dos convites,Emissão de convites,Cancelamento de convites".split(","));

        switch(opcao){
            case '1':
                listarConvites();
                break;
            case '2':
                emitir();
                break;
            case '3':
                cancelar();
                break;
            default:
                return; 
        }
        SubMenu_Grupos.gerenciamento();
    }

    private static void participacao(){
        cabecalho("INICIO > GRUPOS > GERENCIAMENTO DE GRUPOS > PARTICIPACAO");
    }

    private static void sorteio(){
        cabecalho("INICIO > GRUPOS > GERENCIAMENTO DE GRUPOS > SORTEIO");
    }

    // OPERACOES" ===========================================================================================
    /**
     * Lista todas as sugestões cadastradas pelo usuario.
     */
    private static void listar() throws Exception{
        cabecalho("INICIO > GRUPOS > GERENCIAMENTO DE GRUPOS > GRUPOS > LISTAR");
        int [] ids = AmigoOculto.Relacionamento_GrupoUsuario.read(AmigoOculto.idUsuario); // obter a lista de IDs dos grupos ligados ao usuario
        System.out.println("MEUS GRUPOS");

        //Realizar listagem das sugestoes:
        for(int i=0; i<ids.length; i++){
            String dados[] = AmigoOculto.Grupo.read(ids[i]).toString().split("\n"); // extrair os dados de cada grupo, separa-los por linha
            System.out.print((i+1));
            if(AmigoOculto.Grupo.read(ids[i]).getAtivo() == true){
                for(String s: dados) System.out.print('\t' + s + '\n');
                System.out.println();
            }
        }

        aguardarReacao();
    }

    /**
     * Rotina para incluir uma nova grupo.
     */
    private static void incluir() throws Exception{
        String path = "INICIO > GRUPOS > GERENCIAMENTO DE GRUPOS > GRUPOS > INCLUIR";
        cabecalho(path);
        System.out.println("Entre com o nome do grupo: ");
    }

    private static void alterar() throws Exception{
        String path = "INICIO > GRUPOS > GERENCIAMENTO DE GRUPOS > GRUPOS > ALTERAR";
        cabecalho(path);

        // Solicitar numero do grupo que o usuario deseja alterar:
        int id = selecionarGrupo();

        // Alterar grupo selecionado:
        if(id >= 0){
            Grupo novo = AmigoOculto.Grupo.read(id);

            // Apresentar os dados do grupo na tela:
            cabecalho(path);
            System.out.print("Dados antigos:\n" + novo.toString() + "\n\n");

            // Solicitar novos dados:
            String in;
            boolean alteracao = false;
            System.out.print("Por favor, entre com os novos dados (digite [enter] para nao alterar):");

            // Nome:
            System.out.print("Novo nome: "); in = leitor.nextLine();
            if(in.length() > 0){ novo.setNome(in); alteracao=true; }
            // Momento Sorteio:
            System.out.println("Nova data e novo horario: "); in = leitor.nextLine();
            if(in)
        }
    }

    private static void desativar(){
        cabecalho("INICIO > GRUPOS > GERENCIAMENTO DE GRUPOS > GRUPOS > DESATIVAR");
    }

    private static void listarConvites(){
        cabecalho("INICIO > GRUPOS > GERENCIAMENTO DE GRUPOS > CONVITES > LISTAR");
    }

    private static void emitir(){
        cabecalho("INICIO > GRUPOS > GERENCIAMENTO DE GRUPOS > CONVITES > EMITIR");
    }

    private static void cancelar(){
        cabecalho("INICIO > GRUPOS > GERENCIAMENTO DE GRUPOS > CONVITES > CANCELAR");
    }
}