import java.io.*;

/**
 * Classe para a implementação do MENU DE ACESSO ao sistema
 * (Projeto CRUD - Parte 03)
 * @author Rafael Mourão Cerqueira Figueiredo
 * @version 1 - 11/03/2020
 */
class MenuDeAcesso{
	//Atributos estáticos:
	private static Scanner leitor = new Scanner(System.in);
	private static final String version = "1.0";
	private static final String title = "AMIGO OCULTO v" + MenuDeAcesso.version;

	/**
	 * OBSERVAÇÕES:
	 * 1 - Forma de Acesso (do usuário no sistema): através do email e senha. Se estiver cadastrado, retornar os dados. Se não, cadastrar.
	 * 2 - Primeiro Acesso/Cadastro: o usuário deverá se cadastrar através da rotina de primeiro acesso.
	 *   2.1 - Talvez algum usuário não chegue a se cadastrar até a data do sorteio do amigo oculto. Assim, a operação de sorteio
	           deverá excluir da brincadeira todos aqueles que não tiverem se cadastrado.
	 */

	/**
	 * DESAFIO 3: Crie uma terceira opção no menu de acesso que permita ao usuário cadastrar uma sua senha, caso ele tenha
	   esquecido a atual. Para isso, você deveria simular uma operação de envio de email com uma nova senha temporária.
	 */


	//MÉTODOS:
	private static void cabecalho(){ //Imprime o cabeçalho do sistema
		System.out.println(MenuDeAcesso.title);
		for(int i=0; i<MenuDeAcesso.title.length(); i++) System.out.print("=");
		System.out.print("\n\n");
	}

	private static String solicitarEmail(){
		String email;
		boolean erro;
		do{
			System.out.print("Por favor, digite seu endereço de email: ");
			erro = emailJaCadastrado( (email = leitor.nextLine()) );
		}while();
		return email;
	}

	/**
	 * Verifica se um email está ou não cadastrado no sistema
	 * @param email String com o email a ser verificado
	 * @return TRUE caso o email esteja cadastrado no sistema, FALSE caso não.
	 */
	private static boolean emailJaCadastrado(String email){
	}


	//ROTINAS:
	private static void MenuPrincipal(){
		cabecalho(); //Imprimir o cabeçalho
	}

	/**
	 * Rotina para realizar o cadastro de um novo usuário
	 */
	private static void NovoUsuario(){
		/**
		 * PASSO A PASSO:
		 * 1) Solicitar o email do novo usuário;
		 *   1.1) Se o email informado estiver em branco (nenhum valor preenchido), retornar ao menu de acesso;
		 * 2) Buscar se um usuário com esse email já existe, por meio do método read(email) do CRUD;
		 * 3) Se o email já existir para algum usuário,
		 *   3.1) Apresentar mensagem de email já cadastrado;
		 *   3.2) Voltar ao passo 1 do laço principal;
		 * 4) Se o email não existir,
		 *   4.1) Solicitar o nome do usuário;
		 *   4.2) Solicitar a senha do usuário;
		 *   4.3) Solicitar a confirmação da inclusão do novo usuário com esses dados;
		 *     4.3.1) Se o usuário não confirmar a inclusão, voltar ao menu de acesso;
		 *   4.4) Incluir o usuário no arquivo, por meio do método create() do CRUD;
		 *   4.5) Apresentar mensagem de confirmação da inclusão;
		 *   4.6) Voltar ao menu de acesso.
		 */
		//
	}
}
