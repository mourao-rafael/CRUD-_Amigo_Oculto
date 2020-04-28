import java.util.Scanner;

/**
 * Classe abstrata para a implementação do MENU DE ACESSO ao sistema
 */
abstract class MenuDeAcesso{
	// Atributos:
	private static Scanner leitor = new Scanner(System.in);

	// Metodos:
	/**
	 * Pagina inicial do menu de acesso ao sistema.
	 * @return ID do usuario, caso acesso seja realizado || -1 caso o acesso ao sistema seja abortado
	 */
	public static int inicio() throws Exception{
		char opcao;
		boolean erro;
		
		// Ler opcao do usuario:
		do{
			AmigoOculto.limparTela();

			// Imprimir tela inical do menu:
			System.out.print(AmigoOculto.cabecalho + "\n");
			System.out.print("ACESSO\n\n");
			System.out.print("1) Acesso ao sistema\n");
			System.out.print("2) Novo usuário\n\n");
			System.out.print("0) Sair\n\n");
			System.out.print("Opção: ");

			// Ler opcao:
			String in;
			do{ in = leitor.nextLine(); } while(in.length() == 0);
			opcao = in.charAt(0); // ler apenas o primeiro caractere da entrada

			// Caso a opcao seja invalida:
			erro = opcao!='1' && opcao!='2' && opcao!='0';
			if(erro){
				System.out.println("\nErro! Opção '" + opcao + "' Inválida!");
				AmigoOculto.aguardarReacao();
			}
		}while(erro);

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

	//ROTINAS:

	/**
	 * Rotina para realizar o cadastro de um novo usuário
	 */
	private static void novoUsuario() throws Exception{
		boolean erro = true;

		do{
			AmigoOculto.limparTela();
			System.out.print(AmigoOculto.cabecalho + "\n");
			System.out.print("ACESSO > NOVO USUÁRIO\n\n");
			System.out.print("(Pressione [enter] para voltar)\n");
			System.out.print("Email: ");
	
			// Solicitar email do novo usuario:
			String email = leitor.nextLine();
			if(email.length() == 0) erro = false;
			else if(AmigoOculto.Usuarios.read(email) != null){ // caso o email ja exista
				System.out.println("\nEste email já foi cadastrado!");
				AmigoOculto.aguardarReacao();
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
			AmigoOculto.limparTela();
			System.out.print(AmigoOculto.cabecalho + "\n");
			System.out.print("ACESSO > ACESSO AO SISTEMA\n\n");
			System.out.print("(Pressione [enter] para voltar)\n");

			// Solicitar email:
			System.out.print("Por favor, entre com os seus dados:\n");
			System.out.print("Email: ");
			String email = leitor.nextLine();
			if(email.length() == 0) erro = false; // se o email estiver vazio, retornar
			else if( (u = AmigoOculto.Usuarios.read(email)) == null ){ // se o usuario nao estiver cadastrado
				System.out.println("\nO email informado não está cadastrado!");
			}
			else{
				// Solicitar senha:
				System.out.println("Senha: ");
				if( !leitor.nextLine().equals( u.getSenha()) ){
					System.out.println("A senha inserida está incorreta.");
					AmigoOculto.aguardarReacao();
				}
				else erro = false;
			}
		}while(erro);

		return u.getId();
	}
}
