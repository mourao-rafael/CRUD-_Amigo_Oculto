import java.util.*;
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
        int [] ids = AmigoOculto.Relacionamento_GrupoUsuario.read(AmigoOculto.idUsuario); // obter a lista de IDs dos grupos ligados ao usuario
        //Realizar listagem dos grupos:
        for(int i=0; i<ids.length; i++){
            String dados[] = AmigoOculto.Grupo.read(ids[i]).toString().split("\n"); // extrair os dados de cada grupo, separa-los por linha
            System.out.print((i+1));
            if(AmigoOculto.Grupo.read(ids[i]).getAtivo()){
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
            limparTela();
            cabecalho(path);
            System.out.println("Dados inseridos:\n" + novo.toString());
            System.out.print("\nDigite [enter] para confirmar cadastro OU qualquer valor para CANCELAR: ");
            if(leitor.nextLine().length() == 0){
                int idGrupo = AmigoOculto.Grupo.create( novo.toByteArray() );
                AmigoOculto.Relacionamento_GrupoUsuario.create(AmigoOculto.idUsuario, idGrupo); // inserir par [idUsuario, idGrupo] na arvore de relacionamento
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
        if(id >= 0 && AmigoOculto.Grupo.read(id).getAtivo()){
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
                    if( AmigoOculto.Grupo.update(novo) ){
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
            Grupo g = AmigoOculto.Grupo.read(id);
            cabecalho(path);
            System.out.println("Grupo Selecionado:\n" + g.toString() + '\n');

            // Confirmar desativacao:
            System.out.print("Pressione [enter] para confirmar / qualquer outro valor para cancelar alteração: ");
            if(leitor.nextLine().length() == 0){
                // Desativar o  grupo
                g.setAtivo(false);
                if(AmigoOculto.Grupo.update(g)){// desativar o grupo pelo CRUD
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