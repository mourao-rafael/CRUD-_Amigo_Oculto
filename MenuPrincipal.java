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
        System.out.print("Entre com os dados do produto: ");

        // Solicitar nome do produto:
        System.out.print("Produto (aperte [enter] para cancelar): ");
        String in = leitor.nextLine();

        // Se o produto estiver em branco, retornar:
        if(in.length() > 0){
            // Solicitar demais dados do produto:
            Sugestao nova = new Sugestao(); nova.setProduto(in); // cadastrar produto
            System.out.print("Loja: "); nova.setLoja( leitor.nextLine() ); // cadastrar loja
            nova.setValor( lerFloat("Valor aproximado") ); // cadastrar valor aproximado:
            System.out.print("Observações: "); nova.setObservacoes( leitor.nextLine() ); // cadastrar observacoes

            // Confirmar inclusao:
            cabecalho(path);
            System.out.print("Dados inseridos:\n" + nova.toString() + '\n');
            if( confirmarOperacao() ){
                int idSugestao = Sugestoes.create( nova.toByteArray() );
                RelSugestao.create(idUsuario, idSugestao); // inserir par [idUsuario, idSugestao] na arvore de relacionamento
            }
        }
        else{
            System.out.println("Cadastro cancelado!");
            aguardarReacao();
        }

    }

    /**
     * Rotina para alterar determinada sugestao.
     * @param path path ate o metodo atual
     */
    private static void alterar(String path) throws Exception{
        // Solicitar numero da sugestao que o usuario deseja alterar:
        cabecalho(path);
        int id = selecionarEntidade("Digite o número da sugestão que deseja alterar", listagem(RelSugestao, Sugestoes) );

        if(id != -1){ // retorna se o usuario cancelar a operação
            // Apresentar novamente os dados da sugestao escolhida na tela:
            cabecalho(path); 
            Sugestao s = Sugestoes.read(id);
            System.out.print("Dados antigos:\n" + s.toString() + "\n");
    
            // Solicitar novos dados:
            String in;
            boolean alteracao = false;
            System.out.println("Por favor, entre com os novos dados (tecle [enter] para nao alterar):");
            // Produto:
            System.out.print("Produto: "); in = leitor.nextLine();
            if(in.length() > 0){ s.setProduto(in); alteracao=true; }
            // Loja:
            System.out.print("Loja: "); in = leitor.nextLine();
            if(in.length() > 0){ s.setLoja(in); alteracao=true; }
            // Valor:
            float v = lerFloat("Valor");
            if(v >= 0){ s.setValor(v); alteracao=true; }
            // Observacoes:
            System.out.print("Observações: "); in = leitor.nextLine();
            if(in.length() > 0){ s.setObservacoes(in); alteracao=true; }
    
            // Verificar se houve alteracao:
            if(alteracao){
                cabecalho(path);
                System.out.println("Dados atualizados:\n" + s.toString() + '\n');
    
                // Confirmar alteracao:
                if( confirmarOperacao() ){
                    if( Sugestoes.update(s) ){
                        System.out.println("Alteração realizada com sucesso!");
                        aguardarReacao();
                    }
                    else throw new Exception("Houve um erro ao tentar alterar a sugestão!");
                }
            }
            else{
                System.out.println("Nenhuma alteracao foi realizada!");
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
        cabecalho(path);
        int id = selecionarEntidade("Digite o número da sugestão que deseja excluir", listagem(RelSugestao, Sugestoes) );

        if(id >= 0){ // retorna se o usuario cancelar a operação
            // Apresentar novamente os dados da sugestao escolhida na tela:
            Sugestao s = Sugestoes.read(id);
            cabecalho(path);
            System.out.println("Sugestão Selecionada:\n" + s.toString() + '\n');

            // Confirmar exclusao:
            if( confirmarOperacao() ){
                // Excluir a sugestao:
                if( Sugestoes.delete(id) && // excluir a sugestao pelo CRUD
                    RelSugestao.delete( idUsuario, s.getId() ) // excluir a sugestao da arvore de relacionamento
                ){
                    // Notificar sucesso da operacao:
                    System.out.println("Exclusão realizada com sucesso!");
                    aguardarReacao();
                }
                else throw new Exception("Houve um erro ao tentar excluir a sugestão!");
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
                    //participacaoGrupos( addToPath(MenuGrupos.path, "PARTICIPAÇÃO GRUPOS"));
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
                    participacao( addToPath(MenuGrupos.path, "PARTICIPAÇÃO"));
                    break;
                case 4:
                    sorteio( addToPath(MenuGrupos.path, "SORTEIO"));
                    break;
            }
        }while(opcao != 0);
    }

    /*private static void participacaoGrupos(String path) throws Exception{
        int opcao;

            opcao = selecionarOpcao(path, " ".split(","));
    }*/

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

    /*private static void participacao(String path){
        cabecalho("INICIO > GRUPOS > GERENCIAMENTO DE GRUPOS > PARTICIPAÇÃO");
    }*/

    /*private static void sorteio(){
        cabecalho("INICIO > GRUPOS > GERENCIAMENTO DE GRUPOS > SORTEIO");
    }*/
    // OPERACOES" ===========================================================================================
    /**
     * Obtem a data atual do sistema.
     * @return String com a data do sistema.
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
     * Compara as datas.
     * @param data1 String com as informações que servirão como base.
     * @param data2 String com as informações que serão comparadas.
     * @return String com a data final comparada || VAZIA caso a data seja invalida.
     */
    private static String compararDatas(String data1, String data2){
        // Separa as informações recebidas por parametro
        String [] entrada1 = data1.split("/");
        String [] entrada2 = data2.split("/");
        String retorno = "";

        if(Integer.parseInt(entrada1[2]) <= Integer.parseInt(entrada2[2])){// ano
            if(Integer.parseInt(entrada1[1]) <= Integer.parseInt(entrada2[1])){ // mes
                if(Integer.parseInt(entrada1[0]) <= Integer.parseInt(entrada2[0]) ){// dia
                    if(Integer.parseInt(entrada1[0]) == Integer.parseInt(entrada2[0]) && Integer.parseInt(entrada1[1]) == Integer.parseInt(entrada2[1]) && Integer.parseInt(entrada1[2]) == Integer.parseInt(entrada2[2])){ // Dia, mes e ano iguais
                        if(Integer.parseInt(entrada1[3]) <= Integer.parseInt(entrada2[3]) && Integer.parseInt(entrada1[4]) < Integer.parseInt(entrada2[4])){
                            retorno += entrada2[0] + "/" + entrada2[1] + "/" + entrada2[2] + " " + entrada2[3] + ":" + entrada2[4];
                        }
                    } else {
                        retorno += entrada2[0] + "" + entrada2[1] + "" + entrada2[2] + "" + entrada2[3] + "" + entrada2[4];
                    }
                }
            }else if((Integer.parseInt(entrada1[1]) >= Integer.parseInt(entrada2[1]) || Integer.parseInt(entrada1[1]) <= Integer.parseInt(entrada2[1])) && (Integer.parseInt(entrada1[0]) >= Integer.parseInt(entrada2[0]) || Integer.parseInt(entrada1[0]) <= Integer.parseInt(entrada2[0]))&& Integer.parseInt(entrada1[2]) < Integer.parseInt(entrada2[2])){
                retorno += entrada2[0] + "" + entrada2[1] + "" + entrada2[2] + "" + entrada2[3] + "" + entrada2[4];
            }
        }
        System.out.println(retorno);
        return retorno;
    }

    /**
     * Operacao de listagem de todas os grupos cadastradas pelo usuario.
     * @param path path ate o metodo atual
     */
    private static void listar(String path) throws Exception{
        cabecalho(path);
        System.out.println("MEUS GRUPOS: \n\n");
        listarEntidade(RelGrupo, Grupo);
        aguardarReacao();
    }

    /**
     * Rotina para incluir um novo grupo.
     * @param path path ate o metodo atual
     */
    private static void incluir(String path) throws Exception{
        cabecalho(path);
        System.out.println("Entre com os dados do grupo: ");
        
        // Criar novas solicitações:
        ArrayList<Solicitacao> s = new ArrayList<>();
        s.add( new Solicitacao("Nome", Validacao.class.getDeclaredMethod("name", parameterTypes)) );

        // Solicitar nome do grupo:
        System.out.print("Grupo (aperte [enter] para cancelar): ");
        String in = leitor.nextLine(); 
        
        // Se o grupo estiver em branco, retornar:
        if(in.length() > 0){
            // Solicitar demais dados do grupo:
            Grupo novo = new Grupo(); novo.setNome(in); // cadastrar o grupo
            System.out.print("Data e horario do sorteio (DD/MM/AAAA/HH/MM): ");
            String data = compararDatas(dataAtual(), leitor.nextLine());
            if(!data.contains("")) // trata caso a data inserida for invalida
                novo.setMomentoSorteio(Long.parseLong(data)); // cadastrar momentoSorteio
            else 
                novo.setMomentoSorteio(-1);
            novo.setValor(lerFloat("Valor aproximado") ); // cadastrar valor aproximado
            System.out.print("Data e horario do encontro (DD/MM/AAAA/HH/MM): ");
            data = compararDatas(String.valueOf(novo.getMomentoEncontro()), leitor.nextLine());
            if(!data.contains("")) // trata caso a data inserida for invalida
                novo.setMomentoEncontro(Long.parseLong(data)); // cadastrar momentoEncontro
            else
                novo.setMomentoEncontro(-1); //
            System.out.print("Local do encontro: "); novo.setLocalEncontro(leitor.nextLine()); // cadastrar local encontro
            System.out.print("Observações: "); novo.setObservacoes(leitor.nextLine()); // cadastrar observacoes
        
            //Confirmar inclusao:
            cabecalho(path);
            System.out.println("Dados inseridos:\n" + novo.toString() + '\n');
            if( confirmarOperacao() ){
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
    private static void alterar(String path) throws Exception{
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
            String data = "";
            System.out.print("Nova data e novo horario para o sorteio (DD/MM/AAAA/HH/MM): "); in = leitor.nextLine();
            if(in.length() > 0){
                data = compararDatas(String.valueOf(novo.getMomentoSorteio()), in);
                if(!data.contains("")){ novo.setMomentoEncontro(Long.parseLong(data)); alteracao = true;}
            }
            // Valor:
            float v = leValor();
            if(v >= 0){ novo.setValor(v); alteracao=true; }
            // Momento Encontro:
            System.out.print("Nova data e novo horario para o encontro (DD/MM/AAAA/HH/MM): "); in = leitor.nextLine();
            if(in.length() > 0){
                data = compararDatas(String.valueOf(novo.getMomentoSorteio()), in);
                if(!data.contains("")){ novo.setMomentoEncontro(Long.parseLong(data)); alteracao = true; }
            }
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
                    novo.setSorteado(false);
                    novo.setAtivo(true);
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