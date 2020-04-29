/**
 * Classe abstrata para a implementação do MENU DE ACESSO ao sistema
 */
public abstract class MenuDeAcesso extends Menu{
	// Atributos:

	// Metodos:
	/**
	 * Pagina inicial do menu de acesso ao sistema.
	 * @return ID do usuario, caso acesso seja realizado || -1 caso o acesso ao sistema seja abortado
	 */
	public static int inicio() throws Exception{
		// Ler opcao do usuario:
		String texto = cabecalho + "\nACESSO\n\n" + "1) Acesso ao sistema\n" + "2) Novo usuário\n\n" + "0) Sair\n\n";
		char opcao = lerOpcao(texto, "012".toCharArray());

		// Realizar opcao escolhida:
		switch(opcao){
			case '1':
				int id = acesso();
				if(id != -1) return id;
			break;
			case '2':
				novoUsuario();
			break;
			case '0': return -1;
		}

		return inicio(); // Repetir procedimento
	}

	/**
	 * Rotina para realizar o cadastro de um novo usuário
	 */
	private static void novoUsuario() throws Exception{
		boolean erro = true;

		do{
			limparTela();
			System.out.print(cabecalho + "\n");
			System.out.print("ACESSO > NOVO USUÁRIO\n\n");
			System.out.print("(Pressione [enter] para voltar)\n");
			System.out.print("Email: ");
	
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
				System.out.print("Por favor, digite seus dados:\n");
				System.out.print("Nome: ");
				String nome = leitor.nextLine();
				System.out.print("Senha: ");
				String senha = leitor.nextLine();

				// Confirmar inclusao do usuario com os dados inseridos:
				System.out.println("Digite [enter] para confirmar inclusao do usuario, digite qualquer valor para CANCELAR: ");
				if(leitor.nextLine().length() == 0){
					Usuario novo = new Usuario();
					novo.setEmail(email);
					novo.setNome(nome);
					novo.setSenha(senha);
					AmigoOculto.Usuarios.create( novo.toByteArray() );
					System.out.println("Usuário registrado com sucesso!");
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
			limparTela();
			System.out.print(cabecalho + "\n");
			System.out.print("ACESSO > ACESSO AO SISTEMA\n\n");
			System.out.print("(Pressione [enter] para voltar)\n");

			// Solicitar email:
			System.out.print("Por favor, entre com os seus dados:\n");
			System.out.print("Email: ");
			String email = leitor.nextLine();
			if(email.length() == 0) return -1; // se o email estiver vazio, retornar
			else if( (u = AmigoOculto.Usuarios.read(email)) == null ){ // se o usuario nao estiver cadastrado
				System.out.println("\nO email informado não está cadastrado!");
				aguardarReacao();
			}
			else{
				// Solicitar senha:
				System.out.println("Senha: ");
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
