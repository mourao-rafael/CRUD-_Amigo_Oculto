import java.util.ArrayList;

/**
 * Classe para armazenar metodos para a execucao de rotinas do sistema
 * (em geral, rotinas acessadas pelos menus da interface de usuario).
 */
public abstract class Rotinas extends TUI{
	// ROTINAS MENU DE ACESSO:
	/**
	 * Rotina para realizar o acesso (login) ao sistema.
	 * @return ID do usuario, caso acesso seja realizado || -1 caso o acesso ao sistema seja abortado
	 */
	public static void login() throws Exception{
		// Criar solicitacoes:
		ArrayList <Solicitacao> s = new ArrayList<>();
		s.add(new Solicitacao("Email", Validacao.class.getDeclaredMethod("emailCadastrado", String.class), "Erro! O email informado não está cadastrado!"));
		s.add(new Solicitacao("Senha", Validacao.class.getDeclaredMethod("senhaCorreta", String.class), "A senha inserida está incorreta."));
		
        // Ler entradas:
        String[] dados = lerEntradas("", s);
		if(dados != null){
			idUsuario = Usuarios.read( dados[0] ).getId(); // armazena o id do usuario utilizando o sistema
            currentPath = ""; // limpa a variavel de controle do path
            System.out.print(gotoPathLine + limparAposCursor); // limpa o path completamente
			addToPath("INÍCIO"); // reinicia o path, agora para o menu principal
        }
    }
    
    /**
	 * Rotina para realizar o cadastro de um novo usuário
	 */
	public static void novoUsuario() throws Exception{
		// Criar novas solicitacoes:
		ArrayList <Solicitacao> s = new ArrayList<>();
		s.add( new Solicitacao("Email", Validacao.class.getDeclaredMethod("emailNaoCadastrado", String.class), "Erro! Email já cadastrado!"));
		s.add( new Solicitacao("Nome", null) );
		s.add( new Solicitacao("Senha", null) );
		
		String dados[] = lerEntradas("", s); // solicitar dados ao usuario:
		if(dados != null){
			Usuario novo = new Usuario(dados[0], dados[1], dados[2]);

			// Confirmar inclusao do usuario com os dados inseridos:
			novaEtapa();
			System.out.println("Dados inseridos:\n\n" + novo.toString());
			if( confirmarOperacao() ){
				Usuarios.create( novo.toByteArray() );
				System.out.println("Usuário registrado com sucesso!");
			}
		}
    }


	// ROTINAS MENU PRINCIPAL:
	public static void convitesPendentes() throws Exception{
        Usuario u = Usuarios.read(idUsuario);
        int ids[] = convPendentes.read( u.getEmail() ); // obter lista de ids pendentes do usuario logado

        // REALIZAR LISTAGEM DOS CONVITES PENDENTES:
        ArrayList<String> listaConvPendentes = new ArrayList<>();
        for(int id : ids){
            Convite c = Convites.read(id);
            Grupo g = Grupos.read(c.getIdGrupo());
            u = Usuarios.read(g.getIdUsuario());
            
            if(c.pendente()) listaConvPendentes.add("\t" + c.toString()); // adicionar convite na lista de convites pendentes
            else convPendentes.delete(c.getEmail(), c.getId()); // remover convite da lista invertida
        }

        // Solicitar ao usuario qual convite ele quer verificar:
        novaEtapa();
        System.out.println("VOCÊ FOI CONVIDADO PARA PARTICIPAR DOS GRUPOS ABAIXO:");
        int index = selecionarOpcao("Selecione um convite:", (String[])listaConvPendentes.toArray())-1;
        
        if(index != -1){
            int id = ids[index];

            // Solicitar qual ação o usuário deseja fazer (Aceitar ou Recusar o convite):
            novaEtapa();
            System.out.println("Convite selecionado: " + Convites.read(id).toString());
            int opcao = selecionarOpcao("Selecione uma ação:", new String[]{"Aceitar", "Recusar"});

            if(opcao == 0) operacaoCancelada(); // caso o usuario aborte a operacao
            else{
                // Atualizar estado do convite:
                Convite c = Convites.read(id);
                c.setEstado(opcao==1 ? Convite.aceito : Convite.recusado);

                // Realizar atualização do convite nos arquivos:
                if(Convites.update(c)){
                    if(opcao == 1){ // criar nova participacao:
                        int idNovaPart = Participacoes.create(new Participacao(idUsuario, c.getIdGrupo()).toByteArray());
                        RelParticipacao_Grupo.create(c.getIdGrupo(), idNovaPart);
                        RelParticipacao_Usuario.create(idUsuario, idNovaPart);
                    }

                    System.out.println("Operacão concluída com sucesso!"); // notificar sucesso da operacao
                    aguardarReacao();
                }
                else throw new Exception("Erro ao atualizar o estado do convite!");

                // Remover o par da lista de convites pendentes:
                convPendentes.delete(c.getEmail(), c.getId());
            }
        }
	}


	// ROTINAS MENU SUGESTOES:
	/**
     * Operacao de listagem de todas as sugestões cadastradas pelo usuario.
     */
	public static void listarSug() throws Exception{
        System.out.print("MINHAS SUGESTÕES:\n\n");
        listarEntidade(RelSugestao, Sugestoes);
        aguardarReacao();
	}
	
	/**
     * Rotina para incluir uma nova sugestao.
     */
    public static void incluirSug() throws Exception{
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
            novaEtapa();
            System.out.print("Dados inseridos:\n" + sug.toString() + '\n');
            if( confirmarOperacao() ){
                int idSugestao = Sugestoes.create( sug.toByteArray() );
                RelSugestao.create(idUsuario, idSugestao); // inserir par [idUsuario, idSugestao] na arvore de relacionamento
            }
        }

    }

    /**
     * Rotina para alterar determinada sugestao.
     */
    public static void alterarSug() throws Exception{
        // Solicitar numero da sugestao que o usuario deseja alterar:
        int id = selecionarEntidade(listagem(RelSugestao, Sugestoes)); // se operacao cancelada, retorna -1

        if(id != -1){
            // Apresentar novamente os dados da sugestao escolhida na tela:
            novaEtapa();
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
            if(!dados[0].isEmpty()){ sug.setProduto(dados[0]); alteracao = true; }
            if(!dados[1].isEmpty()){ sug.setLoja(dados[1]); alteracao = true; }
            if(!dados[2].isEmpty()){ sug.setValor(Float.parseFloat(dados[2])); alteracao = true; }
            if(!dados[3].isEmpty()){ sug.setObservacoes(dados[3]); alteracao = true; }

            if(alteracao){
                novaEtapa();
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
     */
    public static void excluirSug() throws Exception{
        // Solicitar numero da sugestao que o usuario deseja excluir:
        int id = selecionarEntidade(listagem(RelSugestao, Sugestoes)); // se operacao cancelada, retorna -1

        if(id != -1){
            // Apresentar novamente os dados da sugestao escolhida na tela:
            novaEtapa();
            Sugestao s = Sugestoes.read(id);
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


    // ROTINAS MENU GRUPOS:
    public static void participacao() throws Exception{
        // Solicitar ao usuário que selecione um dos grupos que participa:
        int idsGrupos[] = Participacao.getIdsGrupos(RelParticipacao_Usuario.read(idUsuario)); // recupera ids dos grupos das participacoes do usuario
        int op = selecionarOpcao("Selecione um grupo", Grupo.toOpcoes(idsGrupos)); // realizar solicitacao

        if(--op != -1){
            Grupo g = Grupos.read(idsGrupos[op]);
            Participacao p = Participacoes.read(idUsuario+"|"+g.getId()); // encontrar a participacao do usuario logado no grupo escolhido
            
            addToPath(g.getNome());
            // CRIAR SUBMENUS:
            Mensagem[] m = Mensagem.idsToMensagens( RelMensagemGrupo.read(g.getId()) );

            Opcao<?>[] opMensagens = new Opcao[]{
                //
            };
            Menu mensagens = new Menu(opMensagens);

            Opcao<?>[] opParticipacao = new Opcao[]{
                new Opcao<Rotina>("Participantes", new Rotina("verParticipantes", g.getId()) ),
                new Opcao<Rotina>("Meu amigo oculto", new Rotina("meuAmigoOculto", p.getIdAmigo()) ),
                new Opcao<Menu>("Mensagens", mensagens)
            };
            Menu menuParticipacao = new Menu(opParticipacao, "Selecione o que você deseja ver");
            menuParticipacao.executar();

            returnPath();
        }
    }

    /**
     * Rotina para listar os participantes de um determinado grupo.
     */
    public static void verParticipantes(int idGrupo) throws Exception{
        listarEntidade(RelParticipacao_Grupo, idGrupo, Participacoes);
        aguardarReacao();
    }

    /**
     * Rotina para visualizar o amigo sorteado.
     */
    public static void meuAmigoOculto(int idAmigo) throws Exception{
        System.out.println("Você tirou: " + Usuarios.read(idAmigo).getNome());
        System.out.println("Sugestões de presentes do seu amigo:");
        listarEntidade(RelSugestao, idAmigo, Sugestoes); // recuperar sugestoes do amigo do usuario

        aguardarReacao();
    }


	// ROTINAS MENU GERENCIAR GRUPOS:
	/**
     * Operacao de listagem de todas os grupos cadastradas pelo usuario.
     */
    public static void listarGrup() throws Exception{
        System.out.print("MEUS GRUPOS:\n\n");
        listarEntidade(RelGrupo, Grupos); // chamar metodo que faz a listagem em "lista"
        aguardarReacao();
    }

    /**
     * Rotina para incluir um novo grupo.
     */
    public static void incluirGrup() throws Exception{
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
            novaEtapa();
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
    public static void alterarGrup() throws Exception{
        // Solicitar numero do grupo que o usuario deseja alterar:
        int id = selecionarEntidade(listagem(RelGrupo, Grupos));

        if(id != -1){
            // Apresentar os dados do grupo na tela:
            novaEtapa();
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
            if(!dados[1].isEmpty()){ g.setMomentoSorteio( converterData(dados[1]) ); alteracao=true; }
            if(!dados[2].isEmpty()){ g.setValor( Float.parseFloat(dados[2]) ); alteracao=true; }
            if(!dados[3].isEmpty()){ g.setMomentoEncontro( converterData(dados[3]) ); alteracao=true; }
            if(!dados[4].isEmpty()){ g.setLocalEncontro(dados[4]); alteracao=true; }
            if(!dados[5].isEmpty()){ g.setObservacoes(dados[5]); alteracao=true; }

            if(alteracao){
                novaEtapa();
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
    public static void desativarGrup() throws Exception{
        // Solicitar numero do grupo que o usuario deseja desativar:
        int id = selecionarEntidade(listagem(RelGrupo, Grupos)); 
        
        //Realizar desativacao do grupo selecionado:
        if(id != -1){
            novaEtapa();
            Grupo g = Grupos.read(id);
            System.out.println("Grupo Selecionado:\n" + g.toString() + '\n');

            // Confirmar desativacao:
            if( confirmarOperacao() ){
                g.setAtivo(false); // desativa o grupo
                if(Grupos.update(g)){ // desativar o grupo pelo CRUD
                    System.out.println("Desativação realizada com sucesso!"); // notifica sucesso da operacao:
                    aguardarReacao();
                }
                else throw new Exception("Houve um erro ao tentar desativar o grupo!");
            }
        }
    }


	// ROTINAS MENU CONVITES:
	/**
     * Operacao de listagem de todos os convites cadastradas num grupo escolhido.
     */
    public static void listarConv() throws Exception{
        // Solicitar numero do grupo que o usuario deseja selecionar:
        int id = selecionarEntidade(listagem(RelGrupo, Grupos)); // se operacao cancelada, retorna -1
        
        if(id != -1){
            Grupo grup = Grupos.read(id);
            System.out.print("Convites do Grupo "+ grup.getNome() +":\n\n");
            listarEntidade(RelConvite, id, Convites);
        }
	}
	
	/**
     * Operacao de emissão dos convites.
     */
    public static void emitir() throws Exception{
        // Solicitar numero do grupo que o usuario deseja selecionar:
        Grupo grup = null; int idConvite;
        int id = selecionarEntidade(listagem(RelGrupo, Grupos)); // se operacao cancelada, retorna -1

        // Garantir que o sorteio do grupo selecionado ainda não tenha sido realizado:
        while(id != -1  &&  (grup = Grupos.read(id)).getSorteado()){
            System.out.println("O grupo selecionado já teve o sorteio realizado!");
            aguardarReacao();
            id = selecionarEntidade(listagem(RelGrupo, Grupos));
        }
        
        if(id != -1){
            // Criar novas solicitações:
            ArrayList <Solicitacao> s = new ArrayList<>();
            s.add( new Solicitacao("Email", Validacao.class.getDeclaredMethod("emailCadastrado", String.class), "Erro! Email já cadastrado!"));
            String[] dados = lerEntradas("Grupo " + grup.getNome() + ":\nEntre com os dados do convite", s); // solicita os dados ao usuario
            
            while(dados != null){
                Convite convite = Convites.read(id + '|' + dados[0]);

                // Testar se a combinacao id + grupo ja existe:
                if(convite != null){
                    if(convite.pendente() || convite.aceito()){ // se convite aceito ou pendente:
                        System.out.print("Um convite já foi emitido para este email!");
                        aguardarReacao();
                    }
                    else{ // se o convite foi recusado ou cancelado:
                        System.out.println("Um convite já foi emitido para este email! Você gostaria de reemitir?");
                        if( confirmarOperacao() ){
                            // atualizar dados:
                            convite.setEstado( Convite.pendente );
                            convite.setMomentoConvite( dataAtual() );

                            // realizar reemissao:
                            if(Convites.update(convite)){
                                System.out.print("Convite reenviado!");
                                aguardarReacao();
                            }
                            else throw new Exception("Erro ao reemitir o convite!");
                        }
                    }
                }
                else{ // se o convite nao existe:
                    idConvite = Convites.create( new Convite(-1, id, dados[0], dataAtual(), Convite.pendente).toByteArray() ); // criar novo convite
                    convPendentes.create(dados[0], idConvite); // inserir convite na lista (invertida) de convites pendentes
                    RelConvite.create(id, idConvite); // adicionar par ("idGrupo, idConvite") na arvore de relacionamento
                }

                // Repetir solicitacao de dados:
                dados = lerEntradas("Grupo " + grup.getNome() + ":\nEntre com os dados do convite", s);
            }
        }
    }

	/**
     * Operacao de cancelamento dos convites.
     */
    public static void cancelar() throws Exception{
        // Solicitar numero do grupo que o usuario deseja selecionar:
        Grupo grup = null; int idConvite;
        int id = selecionarEntidade(listagem(RelGrupo, Grupos)); // se operacao cancelada, retorna -1

        // Garantir que o sorteio do grupo selecionado ainda não tenha sido realizado:
        while(id != -1  &&  (grup = Grupos.read(id)).getSorteado()){
            System.out.println("O grupo selecionado já teve o sorteio realizado!");
            aguardarReacao();
            id = selecionarEntidade(listagem(RelGrupo, Grupos));
        }
        
        if(id != -1){ 
            // Listar os convites pendentes:
            novaEtapa();
            System.out.print("Convites pendentes do grupo \""+ grup.getNome() +"\":\n\n");
            idConvite = selecionarEntidade(listagem(RelConvite, id, Convite.class.getDeclaredMethod("pendente"), Convites));

            if(idConvite != -1){
                // Apresentar novamente os dados do convite escolhido na tela:
                novaEtapa();
                Convite convite = Convites.read(idConvite);
                System.out.println("Convite Selecionado:\n" + convite.toString() + '\n');
    
                // Confirmar cancelamento:
                if( confirmarOperacao() ){
                    convite.setEstado(Convite.cancelado); // setta estado do convite para cancelado

                    if(Convites.update(convite) && convPendentes.delete(convite.getEmail(), idConvite)){ // verifica se o cancelamento foi realizado com sucesso
                        System.out.println("Cancelamento realizado com sucesso!"); // notifica sucesso da operacao
                        aguardarReacao();
                    }
                    else throw new Exception("Houve um erro ao tentar cancelar o convite!"); // caso haja algum problema no cancelamento
                }
            }
        }
    }


    // ROTINAS MENU PARTICIPANTES:
    /**
     * Operacao de listagem de todas os participantes cadastrados no grupo.
     */
    public static void listarPart() throws Exception{
        // Solicitar grupo que o usuario deseja listar os participantes:
        int id = selecionarEntidade(listagem(RelGrupo, Grupos)); // se operacao cancelada, retorna -1

        if(id != -1){
            novaEtapa();
            System.out.println("Grupo escolhido:\n" + Grupos.read(id).toString());
            listarEntidade(RelParticipacao_Grupo, id, Participacoes); // chamar metodo que faz a listagem em "lista"
        }
    }

    /**
     * Operacao de remoção de participantes cadastrados no grupo.
     */
    public static void removerPart() throws Exception{
        // Solicitar que o usuario escolha um grupo:
        int idGrupo = selecionarEntidade( listagem(RelGrupo, Grupos) );

        if(idGrupo != -1){
            // Solicitar que o usuário selecione o participante a ser removido:
            novaEtapa();
            System.out.println("Grupo escolhido:\n" + Grupos.read(idGrupo).toString()); // apresentar os dados do grupo escolhido na tela
            int ids[] = listagem(RelParticipacao_Grupo, idGrupo, Participacoes);
            int idPart = selecionarEntidade(ids);

            if(idPart != -1){
                // Se o sorteio ja foi realizado:
                if(Grupos.read(idGrupo).getSorteado()){
                    // Identifica o usuario que seria presentado pelo participante a ser removido:
                    Participacao removida = Participacoes.read(idPart);
                    int idAmigo = Participacoes.read(idPart).getIdAmigo();

                    // Identificar a participacao do usuario que presenteria o participante a ser removido:
                    int idPartPresenteador = -1;
                    for(int i=0; i<ids.length && idPartPresenteador==-1; i++){
                        if(Participacoes.read(ids[i]).getIdAmigo() == removida.getIdUsuario()) idPartPresenteador = ids[i];
                    }

                    // Passar o idAmigo do participante removido ao usuario que o presentearia:
                    Participacao partPresenteador = Participacoes.read(idPartPresenteador);
                    partPresenteador.setIdAmigo(idAmigo);
                    Participacoes.update(partPresenteador);
                }

                // Realizar a remocao da participacao nas estruturas:
                int idUsuarioRemovido = Participacoes.read(idPart).getIdUsuario();
                Participacoes.delete(idPart);
                RelParticipacao_Grupo.delete(idGrupo, idPart);
                RelParticipacao_Usuario.delete(idUsuarioRemovido, idPart);
            }
        }
    }
}
