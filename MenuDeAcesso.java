/**
 * Classe abstrata para a implementação do MENU DE ACESSO ao sistema
 */
public abstract class MenuDeAcesso extends Menu{
	private static final String path = "ACESSO";
	// EXECUCAO DO MENU: ====================================================================================
	/**
	 * Pagina inicial do menu de acesso ao sistema.
	 * @return ID do usuario, caso acesso seja realizado || -1 caso o acesso ao sistema seja abortado
	 */
	public static int inicio() throws Exception{
		int opcao, id = -1;
		do{
			opcao = selecionarOpcao(path, "Acesso ao sistema,Novo usuário".split(",")); // le opcao do usuario
	
			// Realizar opcao escolhida:
			switch(opcao){
				case 1:
					id = acesso( addToPath(path, "ACESSO AO SISTEMA") );
					break;
				case 2:
					novoUsuario( addToPath(path, "NOVO USUÁRIO") );
					break;
			}
		}while(id == -1  &&  opcao != 0);

		return id;
	}

	// OPERACOES" ===========================================================================================
	/**
	 * Rotina para realizar o cadastro de um novo usuário
	 * @param path path ate o metodo atual
	 */
	private static void novoUsuario(String path) throws Exception{
		cabecalho(path);
		
		// Solicitar email do novo usuario:
		System.out.print("(Aperte [enter] para cancelar)\n");
		System.out.print("Email: ");
		String email = leitor.nextLine();
		if(email.length()!=0 && Usuarios.read(email) != null){ // caso o email ja exista
			System.out.println("\nEste email já foi cadastrado!");
			aguardarReacao();
		}
		else if(email.length() != 0){ // criar novo usuario:
			// Solicitar dados do usuario:
			String dados[] = lerEntradas("\nPor favor, digite seus dados:", "Nome,Senha".split(","), false);
			if(dados != null){
				Usuario novo = new Usuario(); novo.setEmail(email); // cadastrar email
				novo.setNome( dados[0] );
				novo.setSenha( dados[1] );
	
				// Confirmar inclusao do usuario com os dados inseridos:
				cabecalho(path);
				System.out.println("Dados inseridos:\n" + novo.toString());
				if( confirmarOperacao() ){
					Usuarios.create( novo.toByteArray() );
					System.out.println("Usuário registrado com sucesso!");
				}
			}
		}
	}

	/**
	 * Rotina para realizar o acesso (login) ao sistema.
	 * @param path path ate o metodo atual
	 * @return ID do usuario, caso acesso seja realizado || -1 caso o acesso ao sistema seja abortado
	 */
	private static int acesso(String path) throws Exception{
		Usuario u = new Usuario();
		boolean erro = true;

		do{
			cabecalho(path);

			// Solicitar email:
			System.out.print("Por favor, entre com os seus dados:\n");
			System.out.print("Email: (aperte [enter] para voltar)");
			String email = leitor.nextLine();
			if(email.length() == 0) return -1; // se o email estiver vazio, retornar
			else if( (u = Usuarios.read(email)) == null ){ // se o usuario nao estiver cadastrado
				System.out.println("\nO email informado não está cadastrado!");
				aguardarReacao();
			}
			else{
				// Solicitar senha:
				System.out.print("Senha: ");
				if( !leitor.nextLine().equals( u.getSenha()) ){
					System.out.println("A senha inserida está incorreta.");
					aguardarReacao();
				}
				else erro = false;
			}
		}while(erro);

		return u.getId();
	}
}
