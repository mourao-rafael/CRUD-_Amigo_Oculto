/**
 * Classe para a instanciacao de um objeto Opcao -> usado nos menus.
 */
public class Opcao <T extends TUI_Element> extends TUI{
    private String nome; // nome da opcao (texto que aparecera no menu)
    private T destino; // destino da opcao -> pode ser um objeto Menu (submenu) ou Method (rotina)

    /**
     * Construtor da classe.
     * @param nome nome da opcao a ser criada (nome que aparecera na interface de usuario)
     * @param destino Menu submenu || Method rotina
     */
    Opcao(String nome, T destino){
        this.nome = nome;
        this.destino = destino; // 5 = numero de linhas do cabecalho (olhar comentario do metodo 'cabecalho()')
    }

    /**
     * Executa a opcao corrente - usar este metodo quando a opcao corrente for selecionada.
     */
    public void executar(){
        addToPath(" > " + this.nome);
        this.destino.executar();
        returnPath();
    }

    /**
     * "Transforma" um arranjo e opcoes em um arranjo de Strings, com os nomes das opcoes.
     * @param ops Opcao[] com as opcoes a serem convertidas 
     * @return String[] com o nome de cada opcao
     */
    public static String[] toStringArray(Opcao<?>[] ops){
        String[] opNames = new String[ops.length];
        for(int i=0; i<ops.length; i++) opNames[i] = ops[i].nome;
        return opNames;
    }
}