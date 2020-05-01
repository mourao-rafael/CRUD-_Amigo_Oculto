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
     * @param path path ate o metodo atual
     */
    private static void listar(String path) throws Exception{
        cabecalho(path);
        System.out.print("MINHAS SUGESTÕES:\n\n");
        listarEntidade(RelSugestao, Sugestoes);
        aguardarReacao();
    }

    /**
     * Rotina para incluir uma nova sugestao.
     * @param path path ate o metodo atual
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
     * @param path path ate o metodo atual
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
            if(!dados[0].isEmpty() || !dados[1].isEmpty() || !dados[2].isEmpty() || !dados[3].isEmpty()){
                cabecalho(path);
                System.out.println("Dados atualizados:\n" + s.toString() + '\n');
    
                // Confirmar alteracao:
                if( confirmarOperacao() ){
                    // Alterar dados modificados no objeto:
                    if(!dados[0].isEmpty()) sug.setProduto(dados[0]);
                    if(!dados[1].isEmpty()) sug.setLoja(dados[1]);
                    if(!dados[2].isEmpty()) sug.setValor(Float.parseFloat(dados[2]));
                    if(!dados[3].isEmpty()) sug.setObservacoes(dados[3]);

                    // Realizar alteracao:
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
     * @param path path ate o metodo atual
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
                    gerenciamento( addToPath(MenuGrupos.path, "GERENCIAMENTO DE GRUPOS"));
                    break;
                case 2:
                    //
                    break;
            }
        }while(opcao != 0);
    }

    private static void gerenciamento(String path) throws Exception{
        int opcao;

        do{
            opcao = selecionarOpcao(path, "Grupos,Convites,Participação,Sorteio".split(","));
            switch(opcao){
                case 1:
                    grupos( addToPath(MenuGrupos.path, "GRUPOS"));
                    break;
                case'2':
                    convite( addToPath(MenuGrupos.path, "CONVITE"));
                    break;
                case'3':
                    //
                    break;
                case 4:
                    //
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
                    listar( addToPath(MenuGrupos.path, "LISTAR"));
                    break;
                case 2:
                    incluir( addToPath(MenuGrupos.path, "INCLUIR"));
                    break;
                case 3:
                    alterar( addToPath(MenuGrupos.path, "ALTERAR"));
                    break;
                case 4:
                    desativar( addToPath(MenuGrupos.path, "DESATIVAR"));
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
                    listarConvites( addToPath(MenuGrupos.path, "LISTAR"));
                    break;
                case 2:
                    emitir( addToPath(MenuGrupos.path, "EMITIR"));
                    break;
                case 3:
                    cancelar( addToPath(MenuGrupos.path, "CANCELAR"));
                    break;
            }
        }while(opcao != 0);
    }

    // OPERACOES" ===========================================================================================
    /**
     * Obtem a data atual do sistema.
     * @param String in data inserida pelo usuario
     * @return dataAtual do sistema, ja comparada com a data inserida pelo usuario.
     */
    private static String dataAtual(){
        String retorno = "";
        
        Calendar c1 = Calendar.getInstance();
    
        int ano = c1.get(Calendar.YEAR);
        int mes = c1.get(Calendar.MONTH);
        mes+=01;
        int dia = c1.get(Calendar.DAY_OF_MONTH);
        int hora= c1.get(Calendar.HOUR_OF_DAY);
        int min = c1.get(Calendar.MINUTE);

        if((mes >= 1 || mes <= 9) || (dia >= 1 || dia <= 9) || (min >= 0 || min <= 9) || (hora >= 0 || hora <= 9)){
            retorno = "0" + dia + "/0" +  mes + "/" + ano + "/0" + hora + "/0" + min;
        }else{
            retorno = dia + "/" +  mes + "/" + ano + "/" + hora + "/" + min;
        }
        
        System.out.println(retorno);
        return retorno;
    }
    /**
     * Lista todos os grupos cadastrados pelo usuario.
     */
    private static void listar() throws Exception{
        cabecalho("INICIO > GRUPOS > GERENCIAMENTO DE GRUPOS > GRUPOS > LISTAR");
        System.out.println("MEUS GRUPOS");

        aguardarReacao();
    }

    /**
     * Rotina para incluir uma novo grupo.
     */
    private static void incluir() throws Exception{
        String path = "INICIO > GRUPOS > GERENCIAMENTO DE GRUPOS > GRUPOS > INCLUIR";
        cabecalho(path);
        System.out.println("Entre com os dados do grupo: ");
        
        // Solicitar nome do grupo:
        System.out.print("Grupo (aperte [enter] para cancelar): ");
        String in = leitor.nextLine(); 
        
        if(in.length() > 0){ // se o nome do grupo estiver em branco, retornar
            // Solicitar demais dados do grupo:
            Grupo novo = new Grupo(); novo.setNome(in);
            System.out.print("Data e horario do sorteio (DD/MM/AAAA HH:MM:SS): "); in = leitor.nextLine();
            novo.setMomentoSorteio(Long.valueOf(dataAtual(in)).longValue()); // cadastrar momentoSorteio
            System.out.print("Valor: "); novo.setValor( leValor() ); // cadastrar valor
            System.out.print("Data e horario do encontro (DD/MM/AAAA HH:MM:SS): "); in= leitor.nextLine();
            novo.setMomentoSorteio(Long.valueOf(dataAtual(in)).longValue());// cadastrar momentoEncontro
            System.out.print("Local do encontro: "); novo.setLocalEncontro(leitor.nextLine()); // cadastrar local encontro
            System.out.print("Observacoes: "); novo.setObservacoes(leitor.nextLine()); // cadastrar observacoes
        
            //Confirmar inclusao do grupo
            cabecalho(path);
            System.out.println("Dados inseridos:\n" + novo.toString());
            System.out.print("\nDigite [enter] para confirmar cadastro OU qualquer valor para CANCELAR: ");
            if(leitor.nextLine().length() == 0){
                int idGrupo = Grupo.create( novo.toByteArray() );
                RelGrupo.create(idUsuario, idGrupo); // inserir par [idUsuario, idGrupo] na arvore de relacionamento
            }
        }
        else{
            System.out.println("Cadastro cancelado!");
            aguardarReacao();
        }
    }
    /**
     * Rotina para alterar determinado grupo.
     */
    private static void alterar() throws Exception{
        String path = "INICIO > GRUPOS > GERENCIAMENTO DE GRUPOS > GRUPOS > ALTERAR";
        cabecalho(path);

        // Solicitar numero do grupo que o usuario deseja alterar:
        int id = selecionarGrupo();

        // Alterar grupo selecionado:
        if(id >= 0 && Grupo.read(id).getAtivo()){
            Grupo novo = Grupo.read(id);

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
            System.out.print("Nova data e novo horario para o sorteio (DD/MM/AAAA HH:MM:SS): "); in = leitor.nextLine();
            String dataAtual = dataAtual(in);
            if(in.length() > 0){ novo.setMomentoSorteio(Long.valueOf(dataAtual).longValue()); alteracao = true; }
            // Valor:
            float v = leValor();
            if(v >= 0){ novo.setValor(v); alteracao=true; }
            // Momento Encontro:
            dataAtual = dataAtual(in);
            System.out.print("Nova data e novo horario para o encontro (DD/MM/AAAA HH:MM:SS): "); in = leitor.nextLine();
            if(in.length() > 0){ novo.setMomentoEncontro(Long.valueOf(dataAtual).longValue()); alteracao = true; }
            // Novo local:
            System.out.print("Novo local: "); in = leitor.nextLine();
            if(in.length() > 0){ novo.setLocalEncontro(in); alteracao = true; }
            // Observacoes:
            System.out.print("Observações: "); in = leitor.nextLine();
            if(in.length() > 0){ novo.setObservacoes(in); alteracao=true; }

            // Se houve alteracao:
            if(alteracao){
                cabecalho(path);
                System.out.println("Dados atualizados:\n" + novo.toString() + '\n');
            
                // Confirmar alteracao:
                System.out.print("Pressione [enter] para confirmar / qualquer outro valor para cancelar alteração: ");
                if(leitor.nextLine().length() == 0){
                    if( Grupo.update(novo) ){
                            System.out.println("Alteração realizada com sucesso!");
                            aguardarReacao();
                    }
                    else throw new Exception("Houve um erro ao tentar alterar o grupo!");
                }
                else{
                    System.out.println("Alteração abortada!");
                    aguardarReacao();
                }
                
            }

        }
    }

    /**
     * Rotina para excluir determinado grupo.
     */
    private static void desativar() throws Exception{
        String path = "INICIO > GRUPOS > GERENCIAMENTO DE GRUPOS > GRUPOS > DESATIVAR";
        cabecalho(path);

        // Solicitar numero do grupo que o usuario deseja desativar:
        int id = selecionarGrupo(); 
        
        //Realizar desativacao do grupo selecionado:
        if(id >= 0){
            Grupo g = Grupo.read(id);
            cabecalho(path);
            System.out.println("Grupo Selecionado:\n" + g.toString() + '\n');

            // Confirmar desativacao:
            System.out.print("Pressione [enter] para confirmar / qualquer outro valor para cancelar alteração: ");
            if(leitor.nextLine().length() == 0){
                // Desativar o  grupo
                g.setAtivo(false);
                if(Grupo.update(g)){// desativar o grupo pelo CRUD
                    // Notificar sucesso da operacao:
                    System.out.println("Desativação realizada com sucesso!");
                    aguardarReacao();
                }
                else throw new Exception("Houve um erro ao tentar desativar o grupo!");
            }
            else{
                System.out.println("Operação abortada!");
                aguardarReacao();
            }
        }
    }

    /**
     * Operacao de listagem de todos os convites cadastradas pelo usuario.
     * @param path path ate o metodo atual
     */
    private static void listarConvites(String path) throws Exception{
        cabecalho(path);
        System.out.print("ESCOLHA O GRUPO:\n\n");
        
        // Solicitar numero do grupo que o usuario deseja selecionar:
        int id = selecionarEntidade(path, listagem(RelGrupo, Grupo)); // se operacao cancelada, retorna -1
        
        if(id != -1){ 
            Grupo grup = Grupo.read(id);
            System.out.print("CONVITES DO GRUPO "+ grup.getNome() +"\n\n");
            listarEntidade(RelConvite, Convite);
        } 
    }

    private static ArvoreBMais_ChaveComposta_String_Int listaInvertida;
    
    /**
     * Operacao de emissão dos convites.
     * @param path path ate o metodo atual
     */
    private static void emitir(String path) throws Exception{
        cabecalho(path);
        System.out.print("ESCOLHA O GRUPO:\n\n");
        // Solicitar numero do grupo que o usuario deseja selecionar:
        int id = selecionarEntidade(path, listagem(RelGrupo, Grupo)); // se operacao cancelada, retorna -1
        
        if(id != -1){
            Grupo grup = Grupo.read(id);
            if(grup.getSorteado() == false && grup.getAtivo()){ // verifica se o grupo ja foi sorteado e se esta ativo
                System.out.print("CONVITES DO GRUPO "+ grup.getNome() + "\n\n");
                
                // Criar novas solicitações:
                ArrayList <Solicitacao> s = new ArrayList<>();
                s.add( new Solicitacao("Email", Validacao.class.getDeclaredMethod("emailNaoCadastrado", String.class), "Erro! Email já cadastrado!"));
                String[] dados = lerEntradas("Entre com os dados do convite", s); // solicita os dados ao usuario
                
                while(dados != null){
                    Convite convite = Convite.read(id);
                    String aux = id + "|" + dados[0];
                    if(aux.contains(Convite.read(convite.chaveSecundaria()).chaveSecundaria())){
                        if(convite.getEstado() == 0 || convite.getEstado() == 1){
                            System.out.println("Convite ja foi emitido para o esse email.");
                        } else {
                            System.out.println("Convite recusado.");                    
                            dados = lerEntradas("Por favor, entre com os novos dados se desejar reenviar o convite (tecle [enter] para nao reenviar)", s);
                        }
                    } else {
                        Convite conv = new Convite(idUsuario, id, dados[0], Long.valueOf(dataAtual()), (byte)0); // criar novo convite
                        // Confirmar inclusao:
                        cabecalho(path);
                        System.out.print("Dados inseridos:\n" + conv.toString() + '\n');
                        if( confirmarOperacao() ){
                            int idConvite = Convite.create( conv.toByteArray() );
                            listaInvertida.create(dados[0], idConvite);
                            RelConvite.create(id, idConvite); // inserir par [idUsuario, idSugestao] na arvore de relacionamento
                        }
                    }
                }
            }

            
        }
    }

    /**
     * Operacao de cancelamento dos convites.
     * @param path path ate o metodo atual
     */
    private static void cancelar(String path) throws Exception{
        cabecalho(path);
        System.out.print("ESCOLHA O GRUPO:\n\n");
        // Solicitar numero do grupo que o usuario deseja selecionar:
        int id = selecionarEntidade(path, listagem(RelGrupo, Grupo)); // se operacao cancelada, retorna -1
        
        if(id != -1){ 
            Grupo grup = Grupo.read(id);
            if(grup.getSorteado() == false && grup.getAtivo()){ // verifica se o grupo ja foi sorteado e se esta ativo
                System.out.print("CONVITES DO GRUPO "+ grup.getNome() +"\n\n");
                int id1 = selecionarEntidade(path, listagem(RelConvite, Convite));
                    if(id1 != -1){
                    // Apresentar novamente os dados do convite escolhido na tela:
                    Convite convite = Convite.read(id1);
                    cabecalho(path);
                    System.out.println("Convite Selecionado:\n" + convite.getEmail() + '\n');

                    // Confirmar cancelamento:
                    if( confirmarOperacao() ){
                        // Cancelar o convite:
                        if(Convite.update(convite)) convite.setEstado((byte) 3);
                        if(listaInvertida.delete(convite.getEmail(), id1)){ // verifica se o cancelamento foi realizado com sucesso
                            System.out.println("Cancelamento realizado com sucesso!"); // notifica sucesso da operacao
                            aguardarReacao();
                        }
                        else throw new Exception("Houve um erro ao tentar cancelar o convite!"); // caso haja algum problema no cancelamento
                    }
                }
            } 
        }
    }
}