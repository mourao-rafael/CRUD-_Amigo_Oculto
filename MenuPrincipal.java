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
    // EXECUCAO DO MENU: ====================================================================================
    public static void inicio() throws Exception{
        int opcao = selecionarOpcao("INICIO > GRUPOS", "Gerenciamento de gupos,Participação".split(","));

        switch(opcao){
            case 1:
                gerenciamento();
                break;
            case 2:
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
        int opcao = selecionarOpcao("INICIO > GRUPOS > GERENCIAMENTO DE GRUPOS", "Grupos,Convites,Participação,Sorteio".split(","));

        switch(opcao){
            case 1:
                grupos();
                break;
            case'2':
                convite();
                break;
            case'3':
                participacao();
                break;
            case 4:
                sorteio();
                break;
            default:
                return;
        }
        MenuGrupos.inicio();
    }

    private static void grupos() throws Exception{
        int opcao = selecionarOpcao("INICIO > GRUPOS > GERENCIAMENTO DE GRUPOS > GRUPOS", "Listar,Incluir,Alterar,Desativar".split(","));

        switch(opcao){
            case 1:
                listar();
                break;
            case 2:
                incluir();
                break;
            case 3:
                alterar();
                break;
            case 4:
                desativar();
                break;
            default:
                return;
        }
        MenuGrupos.inicio();
    }

    private static void convite() throws Exception{
        int opcao = selecionarOpcao("INICIO > GRUPOS > GERENCIAMENTO DE GRUPOS > CONVITES", "Listagem dos convites,Emissão de convites,Cancelamento de convites".split(","));

        switch(opcao){
            case 1:
                listarConvites();
                break;
            case 2:
                emitir();
                break;
            case 3:
                cancelar();
                break;
            default:
                return; 
        }
        MenuGrupos.gerenciamento();
    }

    private static void participacao(){
        cabecalho("INICIO > GRUPOS > GERENCIAMENTO DE GRUPOS > PARTICIPAÇÃO");
    }

    private static void sorteio(){
        cabecalho("INICIO > GRUPOS > GERENCIAMENTO DE GRUPOS > SORTEIO");
    }

    // OPERACOES" ===========================================================================================
    
    /**
     * Metodo para ler o valor aproximado de um grupo
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
     * Obtem a data atual do sistema.
     * @param String in data inserida pelo usuario
     * @return dataAtual do sistema, ja comparada com a data inserida pelo usuario.
     */
    private static String dataAtual(String in){
        Calendar hoje = Calendar.getInstance();
        
        int ano = hoje.get(Calendar.YEAR);
        int mes = hoje.get(Calendar.MONTH);
        int dia = hoje.get(Calendar.DAY_OF_MONTH);
        int hora= hoje.get(Calendar.HOUR_OF_DAY);
        int min = hoje.get(Calendar.MINUTE);
        int sec = hoje.get(Calendar.SECOND);
    
        String dataAtual = "" + dia + "/" + mes + "/" + ano + "-" + hora + ":" + min + ":" + sec;
        if(dataAtual.compareTo(in) < 0) 
            return dataAtual;
        else 
            return in;
    }

    /**
     * Lista as sugestoes em uma na tela (usado em multiplas operacoes).
     * @return int[] lista de ids dos grupos vinculadas ao usuario.
     */
    private static int[] listagem() throws Exception{
        int [] ids = RelGrupo.read(idUsuario); // obter a lista de IDs dos grupos ligados ao usuario
        //Realizar listagem dos grupos:
        for(int i=0; i<ids.length; i++){
            String dados[] = Grupo.read(ids[i]).toString().split("\n"); // extrair os dados de cada grupo, separa-los por linha
            System.out.print((i+1));
            if(Grupo.read(ids[i]).getAtivo()){
                for(String s: dados) System.out.print('\t' + s + '\n');
                System.out.println();
            }
        }
        return ids;
    }

    /**
     * Solicita ao usuario que escolha uma de seus grupo.
     * @return id do grupo selecionada pelo usuario || -1, caso
     */
    private static int selecionarGrupo() throws Exception{
        int[] ids = listagem(); // listar os grupos e extrair a lista de ids
        int opcao = -1;
        do{
            // Solicitar que o usuario selicione uma das opcoes:
            System.out.println("Digite o número do grupo que deseja alterar (ou digite [0] para abortar): ");
            String in;
            do{ in = leitor.nextLine(); } while(in.length() == 0);

            // Validar selecao do usuario:
            if( !in.replaceAll("[^0-9]", "").equals(in)  ||  Integer.parseInt(in)>ids.length ){ // se a entrada nao for num inteiro || se o valor for invalido
                System.out.println("Erro! O valor '"+ in +"' não é válido!");
                aguardarReacao();
            }
            else opcao = Integer.parseInt(in);
        }while(opcao == -1);

        return ids[--opcao]; // extrair id do grupo escolhido        
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