import java.util.ArrayList;

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
		
		// Criar novas solicitacoes:
		ArrayList <Solicitacao> s = new ArrayList<>();
		s.add( new Solicitacao("Email", Validacao.class.getDeclaredMethod("emailNaoCadastrado", String.class), "Erro! Email já cadastrado!"));
		s.add( new Solicitacao("Nome", null) );
		s.add( new Solicitacao("Senha", null) );
		
		String dados[] = lerEntradas("", s); // solicitar dados ao usuario:
		if(dados != null){
			Usuario novo = new Usuario(dados[0], dados[1], dados[2]);

			// Confirmar inclusao do usuario com os dados inseridos:
			cabecalho(path);
			System.out.println("Dados inseridos:\n" + novo.toString());
			if( confirmarOperacao() ){
				Usuarios.create( novo.toByteArray() );
				System.out.println("Usuário registrado com sucesso!");
			}
		}
	}

	/**
	 * Rotina para realizar o acesso (login) ao sistema.
	 * @param path path ate o metodo atual
	 * @return ID do usuario, caso acesso seja realizado || -1 caso o acesso ao sistema seja abortado
	 */
	private static int acesso(String path) throws Exception{
		String[] dados;
		cabecalho(path);

		// Criar solicitacoes:
		ArrayList <Solicitacao> s = new ArrayList<>();
		s.add(new Solicitacao("Email", Validacao.class.getDeclaredMethod("emailCadastrado", String.class), "Erro! O email informado não está cadastrado!"));
		s.add(new Solicitacao("Senha", Validacao.class.getDeclaredMethod("senhaCorreta", String.class), "A senha inserida está incorreta."));
		
		// Ler entradas:
		dados = lerEntradas("", s);
		if(dados == null) return -1;
		else return Usuarios.read( dados[0] ).getId();

	}
}
