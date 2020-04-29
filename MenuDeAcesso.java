/**
 * Classe abstrata para a implementação do MENU DE ACESSO ao sistema
 */
public abstract class MenuDeAcesso extends Menu{
	// EXECUCAO DO MENU: ====================================================================================
	/**
	 * Pagina inicial do menu de acesso ao sistema.
	 * @return ID do usuario, caso acesso seja realizado || -1 caso o acesso ao sistema seja abortado
	 */
	public static int inicio() throws Exception{
		char opcao = lerOpcao("ACESSO", "Acesso ao sistema,Novo usuário".split(",")); // le opcao do usuario

		// Realizar opcao escolhida:
		switch(opcao){
			case '1':
				int id = acesso();
				if(id != -1) return id;
				break;
			case '2':
				novoUsuario();
				break;
			case '0':
				return -1;
		}

		return MenuDeAcesso.inicio(); // repetir processo
	}

	// OPERACOES" ===========================================================================================
	/**
	 * Rotina para realizar o cadastro de um novo usuário
	 */
	private static void novoUsuario() throws Exception{
		String path = "ACESSO > NOVO USUÁRIO";
		boolean erro = true;

		do{
			cabecalho(path);
			System.out.print("(Aperte [enter] para cancelar) Email: ");
	
			// Solicitar email do novo usuario:
			String email = leitor.nextLine();
			if(email.length() == 0) erro = false;
			else if(AmigoOculto.Usuarios.read(email) != null){ // caso o email ja exista
				System.out.println("\nEste email já foi cadastrado!");
				aguardarReacao();
			}
			else{ // criar novo usuario:
				erro = false;

				// Solicitar dados do usuario:
				Usuario novo = new Usuario(); novo.setEmail(email); // cadastrar email
				System.out.print("Por favor, digite seus dados:\n");
				System.out.print("Nome: "); novo.setNome( leitor.nextLine() ); // cadastrar nome
				System.out.print("Senha: "); novo.setSenha( leitor.nextLine() ); // cadastrar senha

				// Confirmar inclusao do usuario com os dados inseridos:
				limparTela();
				cabecalho(path);
				System.out.println("Dados inseridos:\n" + novo.toString());
				System.out.print("\nDigite [enter] para confirmar cadastro OU qualquer valor para CANCELAR: ");
				if(leitor.nextLine().length() == 0){
					AmigoOculto.Usuarios.create( novo.toByteArray() );
					System.out.println("Usuário registrado com sucesso!");
				}
				else{
					System.out.println("Cadastro cancelado!");
					aguardarReacao();
				}
			}
		}while(erro);
	}

	/**
	 * Rotina para realizar o acesso (login) ao sistema.
	 * @return ID do usuario, caso acesso seja realizado || -1 caso o acesso ao sistema seja abortado
	 */
	private static int acesso() throws Exception{
		Usuario u = new Usuario();
		boolean erro = true;

		do{
			cabecalho("ACESSO > ACESSO AO SISTEMA");

			// Solicitar email:
			System.out.print("Por favor, entre com os seus dados:\n");
			System.out.print("Email: (aperte [enter] para voltar)");
			String email = leitor.nextLine();
			if(email.length() == 0) return -1; // se o email estiver vazio, retornar
			else if( (u = AmigoOculto.Usuarios.read(email)) == null ){ // se o usuario nao estiver cadastrado
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
