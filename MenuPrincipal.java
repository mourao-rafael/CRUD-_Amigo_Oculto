import java.util.*;
public abstract class MenuPrincipal extends Menu{
    protected static final String path = "INÍCIO";

    // EXECUCAO DO MENU: ====================================================================================
    public static void inicio() throws Exception{
        int opcao;

        do{
            // Ler opcao:
            opcao = selecionarOpcao(path, "Sugestões de presentes,Grupos,Novos convites: 0".split(","));
    
            switch(opcao){
                case 1:
                    MenuSugestoes.inicio();
                    break;
                case 2:
                    MenuGrupos.inicio();
                    break;
                case 3:
                    //
                    break;
            }
        }while(opcao != 0);
    }
}


/**
 * Classe abstrata para a implementação do submenu de sugestões.
 */
abstract class MenuSugestoes extends Menu{
    protected static final String path = addToPath(MenuPrincipal.path, "SUGESTÕES");

    // EXECUCAO DO MENU: ====================================================================================
    public static void inicio() throws Exception{
        int opcao;

        do{
            opcao = selecionarOpcao(path, "Listar,Incluir,Alterar,Excluir".split(",")); // ler opcao

            switch(opcao){
                case 1:
                    listar( addToPath(MenuSugestoes.path, "LISTAR") );
                    break;
                case 2:
                    incluir( addToPath(MenuSugestoes.path, "INCLUIR") );
                    break;
                case 3:
                    alterar( addToPath(MenuSugestoes.path, "ALTERAR") );
                    break;
                case 4:
                    excluir( addToPath(MenuSugestoes.path, "EXCLUIR") );
                    break;
            }
        }while(opcao != 0);
    }

    // OPERACOES" ===========================================================================================
    /**
     * Operacao de listagem de todas as sugestões cadastradas pelo usuario.
     * @param path path ate a operacao atual
     */
    private static void listar(String path) throws Exception{
        cabecalho(path);
        System.out.print("MINHAS SUGESTÕES:\n\n");
        listarEntidade(RelSugestao, Sugestoes);
        aguardarReacao();
    }

    /**
     * Rotina para incluir uma nova sugestao.
     * @param path path ate a operacao atual
     */
    private static void incluir(String path) throws Exception{
        cabecalho(path);

        // Criar novas solicitacoes:
        ArrayList <Solicitacao> s = new ArrayList<>();
        s.add(new Solicitacao("Produto", null));
        s.add(new Solicitacao("Loja", null, true));
        s.add(new Solicitacao("Valor aproximado", Validacao.class.getDeclaredMethod("ehFloat", String.class), true));
        s.add(new Solicitacao("Observações", null, true) );

        String[] dados = lerEntradas("Entre com os dados do produto", s); // solicita os dados ao usuario
        if(dados != null){
            Sugestao sug = new Sugestao(idUsuario, dados[0], dados[1], dados[2], dados[3]); // criar nova sugestao
            // Confirmar inclusao:
            cabecalho(path);
            System.out.print("Dados inseridos:\n" + sug.toString() + '\n');
            if( confirmarOperacao() ){
                int idSugestao = Sugestoes.create( sug.toByteArray() );
                RelSugestao.create(idUsuario, idSugestao); // inserir par [idUsuario, idSugestao] na arvore de relacionamento
            }
        }

    }

    /**
     * Rotina para alterar determinada sugestao.
     * @param path path ate a operacao atual
     */
    private static void alterar(String path) throws Exception{
        // Solicitar numero da sugestao que o usuario deseja alterar:
        int id = selecionarEntidade(path, listagem(RelSugestao, Sugestoes)); // se operacao cancelada, retorna -1

        if(id != -1){
            // Apresentar novamente os dados da sugestao escolhida na tela:
            cabecalho(path); 
            Sugestao sug = Sugestoes.read(id);
            System.out.print("Dados antigos:\n" + sug.toString() + "\n");
    
            // Criar novas solicitacoes:
            ArrayList <Solicitacao> s = new ArrayList<>();
            s.add(new Solicitacao("Produto", null, true));
            s.add(new Solicitacao("Loja", null, true));
            s.add(new Solicitacao("Valor aproximado", Validacao.class.getDeclaredMethod("ehFloat", String.class), true));
            s.add(new Solicitacao("Observações", null, true));

            // Solicitar novos dados:
            String[] dados = lerEntradas("Por favor, entre com os novos dados (tecle [enter] para nao alterar)", s);
    
            // Verificar se houve alteracao:
            boolean alteracao = false;
            if(!dados[0].isEmpty()) sug.setProduto(dados[0]);
            if(!dados[1].isEmpty()) sug.setLoja(dados[1]);
            if(!dados[2].isEmpty()) sug.setValor(Float.parseFloat(dados[2]));
            if(!dados[3].isEmpty()) sug.setObservacoes(dados[3]);

            if(alteracao){
                cabecalho(path);
                System.out.println("Dados atualizados:\n" + sug.toString() + '\n');
    
                // Confirmar alteracao:
                if( confirmarOperacao() ){
                    if( Sugestoes.update(sug) ){ // verifica se alteracao foi realizada com sucesso
                        System.out.println("Alteração realizada com sucesso!"); // notifica sucesso da operacao
                        aguardarReacao();
                    }
                    else throw new Exception("Houve um erro ao tentar alterar a sugestão!");
                }
            }
            else{
                System.out.println("Nenhuma alteracao foi realizada!"); // notifica que nenhum dado foi modificado
                aguardarReacao();
            }
        }
    }

    /**
     * Rotina para excluir determinada sugestao.
     * @param path path ate a operacao atual
     */
    private static void excluir(String path) throws Exception{
        // Solicitar numero da sugestao que o usuario deseja excluir:
        int id = selecionarEntidade(path, listagem(RelSugestao, Sugestoes)); // se operacao cancelada, retorna -1

        if(id != -1){
            // Apresentar novamente os dados da sugestao escolhida na tela:
            Sugestao s = Sugestoes.read(id);
            cabecalho(path);
            System.out.println("Sugestão Selecionada:\n" + s.toString() + '\n');

            // Confirmar exclusao:
            if( confirmarOperacao() ){
                // Excluir a sugestao:
                if( Sugestoes.delete(id) && RelSugestao.delete( idUsuario, s.getId() )){ // verifica se a exclusão foi realizada com sucesso
                    System.out.println("Exclusão realizada com sucesso!"); // notifica sucesso da operacao
                    aguardarReacao();
                }
                else throw new Exception("Houve um erro ao tentar excluir a sugestão!"); // caso haja algum problema na remocao
            }
        }
    }
}


/**
 * Classe abstrata para a implementação do submenu de grupos.
 */
abstract class MenuGrupos extends Menu{
    protected static final String path = addToPath(MenuPrincipal.path, "GRUPOS");
    
    // EXECUCAO DO MENU: ====================================================================================
    public static void inicio() throws Exception{
        int opcao;
        do{
            opcao = selecionarOpcao(path, "Gerenciamento de gupos,Participação".split(",")); // ler opcao
            
            switch(opcao){
                case 1:
                    gerenciamento( addToPath(path, "GERENCIAMENTO DE GRUPOS"));
                    break;
                case 2:
                    //participacaoGrupos( addToPath(path, "PARTICIPAÇÃO GRUPOS"));
                    break;
            }
        }while(opcao != 0);
    }

    private static void gerenciamento(String path) throws Exception{
        int opcao;

        do{
            opcao = selecionarOpcao(path, "Grupos,Convites,Participantes,Sorteio".split(","));
            switch(opcao){
                case 1:
                    grupos( addToPath(path, "GRUPOS"));
                    break;
                case'2':
                    convite( addToPath(path, "CONVITE"));
                    break;
                case'3':
                    //participacao( addToPath(path, "PARTICIPAÇÃO"));
                    break;
                case 4:
                    //sorteio( addToPath(path, "SORTEIO"));
                    break;
            }
        }while(opcao != 0);
    }

    private static void grupos(String path) throws Exception{
        int opcao;
        do{
            opcao = selecionarOpcao(path, "Listar,Incluir,Alterar,Desativar".split(","));
            switch(opcao){
                case 1:
                    listar( addToPath(path, "LISTAR"));
                    break;
                case 2:
                    incluir( addToPath(path, "INCLUIR"));
                    break;
                case 3:
                    alterar( addToPath(path, "ALTERAR"));
                    break;
                case 4:
                    desativar( addToPath(path, "DESATIVAR"));
                    break;
            }
        }while(opcao != 0);
    }

    private static void convite(String path) throws Exception{
        int opcao;

        do{
            opcao = selecionarOpcao(path, "Listagem dos convites,Emissão de convites,Cancelamento de convites".split(","));
            switch(opcao){
                case 1:
                    // listarConvites( addToPath(path, "LISTAR"));
                    break;
                case 2:
                    // emitir( addToPath(path, "EMITIR"));
                    break;
                case 3:
                    // cancelar( addToPath(path, "CANCELAR"));
                    break;
            }
        }while(opcao != 0);
    }

    // OPERACOES" ===========================================================================================
    /**
     * Operacao de listagem de todas os grupos cadastradas pelo usuario.
     * @param path path ate a operacao atual
     */
    private static void listar(String path) throws Exception{
        cabecalho(path);
        System.out.println("MEUS GRUPOS: \n\n");
        // Obeter lista de ids:
        int[] ids = listagem(RelGrupo, Grupos); // chamar metodo que faz a listagem em "lista"
        int count = 0;
        for(int id : ids){
            // Listar nomes dos grupos ativos:
            Grupo g = Grupos.read(id);
            if(g.getAtivo()) System.out.print((++count) + ". " + g.getNome());
        }
        aguardarReacao();
    }

    /**
     * Rotina para incluir um novo grupo.
     * @param path path ate a operacao atual
     */
    private static void incluir(String path) throws Exception{
        cabecalho(path);

        // Criar novas solicitações:
        ArrayList<Solicitacao> s = new ArrayList<>();
        s.add(new Solicitacao("Nome", null));
        s.add(new Solicitacao("Data e hora do sorteio "+Validacao.formatacaoData, Validacao.class.getDeclaredMethod("dataSorteio", String.class), "Erro! A data inserida já passou!", true));
        s.add(new Solicitacao("Valor médio dos presentes", Validacao.class.getDeclaredMethod("ehFloat", String.class), true));
        s.add(new Solicitacao("Data e hora do encontro "+Validacao.formatacaoData, Validacao.class.getDeclaredMethod("dataEncontro", String.class), "Erro! A data inserida é inferior à do sorteio!", true));
        s.add(new Solicitacao("Local", null, true));
        s.add(new Solicitacao("Observações adicionais", null, true));
        
        // Se o grupo estiver em branco, retornar:
        String dados[] = lerEntradas(s); // Ler entradas
        if(dados != null){
            Grupo g = new Grupo(idUsuario, dados[0], dados[1], dados[2], dados[3], dados[4], dados[5]);
            //Confirmar inclusao:
            cabecalho(path);
            System.out.println("Dados inseridos:\n" + g.toString() + '\n');
            if( confirmarOperacao() ){
                int idGrupo = Grupos.create( g.toByteArray() );
                RelGrupo.create(idUsuario, idGrupo); // inserir par [idUsuario, idGrupo] na arvore de relacionamento
            }
        }
        else operacaoCancelada();
    }

    /**
     * Rotina para alterar determinado grupo.
     */
    private static void alterar(String path) throws Exception{
        // Solicitar numero do grupo que o usuario deseja alterar:
        int id = selecionarEntidade(path, listagem(RelGrupo, Grupos));

        if(id != -1){
            // Apresentar os dados do grupo na tela:
            cabecalho(path);
            Grupo g = Grupos.read(id);
            System.out.print("Dados antigos:\n" + g.toString() + "\n\n");

            // Criar nova lista de solicitacoes:
            ArrayList <Solicitacao> s = new ArrayList<>();
            s.add(new Solicitacao("Nome", null, true));
            s.add(new Solicitacao("Data e hora do sorteio "+Validacao.formatacaoData, Validacao.class.getDeclaredMethod("dataSorteio", String.class), "Erro! A data inserida já passou!", true));
            s.add(new Solicitacao("Valor médio dos presentes", Validacao.class.getDeclaredMethod("ehFloat", String.class), true));
            s.add(new Solicitacao("Data e hora do encontro "+Validacao.formatacaoData, Validacao.class.getDeclaredMethod("dataEncontro", String.class), "Erro! A data inserida é inferior à do sorteio!", true));
            s.add(new Solicitacao("Local", null, true));
            s.add(new Solicitacao("Observações adicionais", null, true));

            // Solicitar novos dados:
            String[] dados = lerEntradas("Por favor, entre com os novos dados: \n(Ou apenas digite [enter] para não alterar)", s);

            // Verificar se houve alteracao:
            boolean alteracao = false;
            if(!dados[0].isEmpty()){ g.setNome(dados[1]); alteracao=true; }
            if(!dados[1].isEmpty()){ g.setMomentoSorteio( dateFormatter.parse(dados[1]).getTime() ); alteracao=true; }
            if(!dados[2].isEmpty()){ g.setValor( Float.parseFloat(dados[2]) ); alteracao=true; }
            if(!dados[3].isEmpty()){ g.setMomentoEncontro( dateFormatter.parse(dados[3]).getTime() ); alteracao=true; }
            if(!dados[4].isEmpty()){ g.setLocalEncontro(dados[4]); alteracao=true; }
            if(!dados[5].isEmpty()){ g.setObservacoes(dados[5]); alteracao=true; }

            if(alteracao){
                cabecalho(path);
                System.out.println("Dados atualizados:\n" + g.toString() + '\n');
                
                // Confirmar alteracao:
                if( confirmarOperacao() ){
                    if( Grupos.update(g) ){
                        System.out.println("Alteração realizada com sucesso!"); // mensagem de confirmacao de alteracao
                        aguardarReacao();
                    }
                    else throw new Exception("Houve um erro ao tentar alterar o grupo!");
                }
            }
            else{
                System.out.println("Nenhuma alteração foi realizada!");
                aguardarReacao();
            }
        }
    }

    /**
     * Rotina para desativar determinado grupo.
     */
    private static void desativar(String path) throws Exception{
        // Solicitar numero do grupo que o usuario deseja desativar:
        int id = selecionarEntidade(path, listagem(RelGrupo, Grupos)); 
        
        //Realizar desativacao do grupo selecionado:
        if(id != -1){
            cabecalho(path);
            Grupo g = Grupos.read(id);
            System.out.println("Grupo Selecionado:\n" + g.toString() + '\n');

            // Confirmar desativacao:
            if( confirmarOperacao() ){
                g.setAtivo(false); // desativa o grupo
                if(Grupos.update(g)){ // desativar o grupo pelo CRUD
                    System.out.println("Desativação realizada com sucesso!"); // notifica sucesso da operacao:
                    aguardarReacao();
                }
                else{
                    g.setAtivo(true); // reativa o grupo
                    throw new Exception("Houve um erro ao tentar desativar o grupo!");
                }
            }
        }
    }
}