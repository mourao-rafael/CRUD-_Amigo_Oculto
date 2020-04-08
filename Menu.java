import java.util.List;
import java.util.ArrayList;

/**
 * Classe para representar um Menu.
 * @author Rafael Mourao Cerqueira Figueiredo
 */
class Menu{
	//Atributos estaticos:
	public static final String titulo = "AMIGO OCULTO " + AmigoOculto.version + '\n' + "================\n";

	// Atributos:
	private class Opcao{
		public String texto;
		public int line_index; // indice da linha em que a opcao encontra-se no terminal de saida.

		Opcao(String texto, int line_index){
			this.texto = texto;
			this.line_index = line_index;
		}
	}

	private String descricao;
	public List<Opcao> opcoes;
	
	// Construtor:
	Menu(){
		this.descricao = "";
		this.opcoes = new ArrayList<Opcao>();
	}

	// Setter's && Getter's:
	public void setDescricao(String descricao){ this.descricao = "\n" + descricao + "\n\n"; }

	// Metodos:
	/**
	 * Adiciona uma nova opcao no final menu.
	 * @param texto String com o texto da opcao a ser adicionada.
	 * @param id indentificador da nova opcao (PARA O USUARIO). (DEFAULT: numero_de_opcoes + 1)
	 * @param n numero de linhas para separar a nova opcao da ultima linha do menu. (DEFAULT: 0)
	 */
	public void addOpcao(String texto, String id, int n){
		texto = "[" + id + "] " + texto; // adiciona identificador da opcao
		for(int i=0; i<n; i++) texto = ('\n'+texto); // adiciona n linhas antes do texto.
		if(texto.charAt(texto.length()-1) != '\n') texto += '\n'; // garantir nova linha no final.

		this.opcoes.add( new Opcao(texto, tamMenu()-1) );
	}
	public void addOpcao(String texto, int n){
		addOpcao(texto, Integer.toString(this.opcoes.size()+1), n);
	}
	public void addOpcao(String texto){
		addOpcao(texto, Integer.toString(this.opcoes.size()+1), 0);
	}

	/**
	 * Constroi/converte o menu em texto.
	 * @return String com o menu totalmente escrito.
	 */
	private String toText(){
		String resp = titulo + this.descricao;
		for(int i=0; i<this.opcoes.size(); i++) resp += this.opcoes.get(i).texto;
		return resp;
	}

	/**
	 * Conta o tamanho do Menu corrente (em numero de LINHAS).
	 * @return int com o numero de linhas do menu corrente.
	 */
	private int tamMenu(){
		return toText().split("\n").length;
	}

	/**
	 * "Vai para" o menu corrente. (Basicamente, traz o menu corrente para a tela).
	 */
	public void call(){
		AmigoOculto.limparTela();
		System.out.println(this.toText());
	}
}
