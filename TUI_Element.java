import java.lang.reflect.Method;

/**
 * Interface para a implementacao de um elemento da TUI - pode ser um objeto Menu ou Rotina.
 * (usados nos objetos da classe Opcao)
 */
public interface TUI_Element {
    public void executar();
}

/**
 * Classe para a instanciacao de um objeto Opcao -> usado nos menus.
 */
class Opcao <T extends TUI_Element> extends TUI{
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

// IMPLEMENTACOES DA INTERFACE: =============================================================================
/**
 * Classe para a instanciacao de objetos Rotina.
 */
class Rotina implements TUI_Element{
    private Method rotina; // função com a rotina a ser executada

    // Construtor:
    Rotina(String methodName){
        try{
            this.rotina = Rotinas.class.getDeclaredMethod(methodName);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    // Execucao da rotina:
    public void executar(){
        try{
            rotina.invoke(null);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}

/**
 * Classe para a instanciacao de objetos Menu.
 */
class Menu extends Rotinas implements TUI_Element{
    private Opcao<?>[] opcoes; // arranjo com todas as opcoes do menu
    private Method validacaoExtra_saida; // armazena um metodo de validacao extra para SAIR DO menu

    // Construtor:
    Menu(Opcao<?>[] opcoes){
        this.opcoes = opcoes;
        this.validacaoExtra_saida = null;
    }
    Menu(Opcao<?>[] opcoes, String methodName_validacaoExtra){
        this.opcoes = opcoes;
        try{
            this.validacaoExtra_saida = Validacao.class.getDeclaredMethod(methodName_validacaoExtra);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    // Execucao do menu:
    public void executar(){
        int op;

        try{
            while(( op = selecionarOpcao(Opcao.toStringArray(opcoes)) ) != 0){
                opcoes[op-1].executar(); // executar opcao selecionada
                if( validarSaida() ) break;
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    // METODO(S) AUXILIARE(S):
    private boolean validarSaida() throws Exception{
        return this.validacaoExtra_saida!=null && (boolean)this.validacaoExtra_saida.invoke(null);
    }
}
