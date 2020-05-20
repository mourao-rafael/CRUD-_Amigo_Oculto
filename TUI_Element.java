import java.lang.reflect.Method;

/**
 * Interface para a implementacao de um elemento da TUI - pode ser um objeto Menu ou Rotina.
 * (usados nos objetos da classe Opcao)
 */
public interface TUI_Element {
    public void executar();
}

// IMPLEMENTACOES DA INTERFACE: =============================================================================
/**
 * Classe para a instanciacao de objetos Rotina.
 */
class Rotina implements TUI_Element{
    private Method rotina; // função com a rotina a ser executada
    private Integer idChave; // algumas rotinas receberao um idChave por parametro

    // Construtor:
    Rotina(String methodName){
        this(methodName, null);
    }
    Rotina(String methodName, Integer idChave){
        try{
            this.idChave = idChave;
            if(this.idChave == null) this.rotina = Rotinas.class.getDeclaredMethod(methodName);
            else this.rotina = Rotinas.class.getDeclaredMethod(methodName, int.class);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    // Execucao da rotina:
    public void executar(){
        try{
            if(this.idChave == null) this.rotina.invoke(null);
            else this.rotina.invoke(null, this.idChave);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}

/**
 * Classe para a instanciacao de objetos Menu.
 */
class Menu extends Rotinas implements TUI_Element{
    protected Opcao<?>[] opcoes; // arranjo com todas as opcoes do menu
    protected String mensagemSolicitacao; // mensagem de solicitacao a ser exibida ao usuario
    protected Method validacaoExtra_saida; // armazena um metodo de validacao extra para SAIR DO menu

    // Construtores:
    Menu(){
        this(null, null, null);
    }
    Menu(Opcao<?>[] opcoes){
        this(opcoes, mensagemSolicitacaoPadrao, null);
    }
    Menu(Opcao<?>[] opcoes, String mensagemSolicitacao){
        this(opcoes, mensagemSolicitacao, null);
    }
    Menu(Opcao<?>[] opcoes, String mensagemSolicitacao, String methodName_validacaoExtra){
        try{
            this.opcoes = opcoes;
            this.mensagemSolicitacao = mensagemSolicitacao;
            this.validacaoExtra_saida = (methodName_validacaoExtra==null ? null : Validacao.class.getDeclaredMethod(methodName_validacaoExtra));
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    // Execucao do menu:
    public void executar(){
        int op;

        try{
            while(( op = selecionarOpcao(mensagemSolicitacao, Opcao.toStringArray(opcoes)) ) != 0){
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
