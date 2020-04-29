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
     * Metodo para ler o valor aproximado de uma sugestao.
     * @return float com o valor lido || -1 caso o usuario nao digite valor nenhum
     */
    private static float leValor(){
        String in;
        float valor = -1;

        do{
            System.out.print("Valor aproximado: ");
            in = leitor.nextLine().replace(',', '.');
            if( !in.replaceAll("[^0-9.]", "").equals(in) ) System.out.println("\nErro! Valor invalido!"); // se a entrar nao for um numero
            else if(in.length() > 0) valor = Float.parseFloat(in); // se a entrada nao estiver vazia
            else break;
        }while(valor < 0);

        return valor;
    }
    
    /**
     * Lista as sugestoes em uma na tela (usado em multiplas operacoes).
     * @return int[] lista de ids das sugestoes vinculadas ao usuario.
     */
    private static int[] listagem() throws Exception{
        int[] ids = AmigoOculto.Relacionamento_SugestaoUsuario.read( AmigoOculto.idUsuario ); // obter a lista de IDs das sugestoes ligadas ao usuario
        // Realizar listagem das sugestoes:
        for(int i=0; i<ids.length; i++){
            String dados[] = AmigoOculto.Sugestoes.read(ids[i]) .toString().split("\n"); // extrair os dados de cada sugestao, e sera-los por linha
            System.out.print((i+1));
            for(String s : dados) System.out.print('\t' + s + '\n');
            System.out.println();
        }

        return ids;
    }

    /**
     * Solicita ao usuario que escolha uma de suas sugestoes.
     * @return id da sugestao selecionada pelo usuario || -1, caso
     */
    private static int selecionarSugestao() throws Exception{
        int[] ids = listagem(); // listar as sugestoes e extrair a lista de ids
        int opcao = -1;
        do{
            // Solicitar que o usuario selicione uma das opcoes:
            System.out.println("Digite o número da sugestão que deseja alterar (ou digite [0] para abortar): ");
            String in;
            do{ in = leitor.nextLine(); } while(in.length() == 0);

            // Validar selecao do usuario:
            if( !in.replaceAll("[^0-9]", "").equals(in)  ||  Integer.parseInt(in)>ids.length ){ // se a entrada nao for num inteiro || se o valor for invalido
                System.out.println("Erro! O valor '"+ in +"' não é válido!");
                aguardarReacao();
            }
            else opcao = Integer.parseInt(in);
        }while(opcao == -1);

        return ids[--opcao]; // extrair id da sugestao escolhida        
    }

    /**
     * Operacao de listagem de todas as sugestões cadastradas pelo usuario.
     */
    private static void listar() throws Exception{
        cabecalho("INICIO > SUGESTÕES > LISTAR");
        System.out.print("MINHAS SUGESTÕES\n\n");
        
        listagem();
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
            nova.setValor( leValor() ); // cadastrar valor aproximado:
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
    private static void alterar() throws Exception{
        String path = "INICIO > SUGESTÕES > ALTERAR";
        cabecalho(path);

        // Solicitar numero da sugestao que o usuario deseja alterar:
        int id = selecionarSugestao();

        // Alterar sugestao selecionada:
        if(id >= 0){
            Sugestao nova = AmigoOculto.Sugestoes.read(id);
            
            // Apresentar os dados da sugestao na tela:
            cabecalho(path); 
            System.out.print("Dados antigos:\n" + nova.toString() + "\n\n");

            // Solicitar novos dados:
            String in;
            boolean alteracao = false;
            System.out.println("Por favor, entre com os novos dados (digite [enter] para nao alterar):");
            // Produto:
            System.out.print("Novo produto: "); in = leitor.nextLine();
            if(in.length() > 0){ nova.setProduto(in); alteracao=true; }
            // Loja:
            System.out.print("Loja: "); in = leitor.nextLine();
            if(in.length() > 0){ nova.setLoja(in); alteracao=true; }
            // Valor:
            float v = leValor();
            if(v >= 0){ nova.setValor(v); alteracao=true; }
            // Observacoes:
            System.out.print("Observações: "); in = leitor.nextLine();
            if(in.length() > 0){ nova.setObservacoes(in); alteracao=true; }

            // Se houve alteracao:
            if(alteracao){
                cabecalho(path);
                System.out.println("Dados atualizados:\n" + nova.toString() + '\n');

                // Confirmar alteracao:
                System.out.print("Pressione [enter] para confirmar / qualquer outro valor para cancelar alteração: ");
                if(leitor.nextLine().length() == 0){
                    if( AmigoOculto.Sugestoes.update(nova) ){
                        System.out.println("Alteração realizada com sucesso!");
                        aguardarReacao();
                    }
                    else throw new Exception("Houve um erro ao tentar alterar a sugestão!");
                }
                else{
                    System.out.println("Alteração abortada!");
                    aguardarReacao();
                }
            }
        }
    }

    /**
     * Rotina para excluir determinada sugestao.
     */
    private static void excluir() throws Exception{
        String path = "INICIO > SUGESTÕES > EXCLUIR";
        cabecalho(path);

        // Solicitar numero da sugestao que o usuario deseja excluir:
        int id = selecionarSugestao();

        // Realizar exclusao da sugestao selecionada:
        if(id >= 0){
            Sugestao s = AmigoOculto.Sugestoes.read(id);
            cabecalho(path);
            System.out.println("Sugestão Selecionada:\n" + s.toString() + '\n');

            // Confirmar exclusao:
            System.out.print("Pressione [enter] para confirmar / qualquer outro valor para cancelar alteração: ");
            if(leitor.nextLine().length() == 0){
                // Excluir a sugestao:
                if( AmigoOculto.Sugestoes.delete(id) && // excluir a sugestao pelo CRUD
                    AmigoOculto.Relacionamento_SugestaoUsuario.delete( AmigoOculto.idUsuario, s.getId() ) // excluir a sugestao da arvore de relacionamento
                ){
                    // Notificar sucesso da operacao:
                    System.out.println("Exclusão realizada com sucesso!");
                    aguardarReacao();
                }
                else throw new Exception("Houve um erro ao tentar excluir a sugestão!");
            }
            else{
                System.out.println("Operação abortada!");
                aguardarReacao();
            }
        }
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