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
                //
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
    public static void inicio(){
        //
    }
}