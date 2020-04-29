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
    private static void listar(){
        cabecalho("INICIO > SUGESTÕES > LISTAR");

        /**
         * TODO:
         * 1. Obter a lista de IDs de sugestões na Árvore B+ usando o ID do usuário;
         * 2. Para cada ID nessa lista:
              2.1. Obter os dados da sugestão usando o método read(ID) do CRUD;
              2.2. Apresentar os dados da sugestão na tela
         */

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
                else nova.setValor( Float.parseFloat(in) );
            }while(nova.getValor() < 0);
            System.out.print("Observações: "); nova.setObservacoes( leitor.nextLine() ); // cadastrar observacoes

            // Confirmar inclusao da sugestao:
            limparTela();
            cabecalho(path);
            System.out.println("Dados inseridos:\n" + nova.toString());
            System.out.print("\nDigite [enter] para confirmar cadastro OU qualquer valor para CANCELAR: ");

            if(leitor.nextLine().length() == 0){
                int idSugestao = AmigoOculto.Sugestoes.create( nova.toByteArray() );
                // TODO - Incluir o par ID usuario e ID sugestao na arvore B+ de relacionamento
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