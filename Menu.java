import java.util.ArrayList;
import java.util.Scanner;

/**
 * Super-classe dos Menus. Armazena métodos e atributos auxiliares ao funcionamento dos menus.
 */
public abstract class Menu extends AmigoOculto{
    protected static final Scanner leitor = new Scanner(System.in);
    protected static final String titulo = "AMIGO OCULTO " + version + "\n================\n\n";
    protected static String lista[]; // String[] compartilhado entre os menus (armazenara as listagens realizadas)
    // CODIGOS ANSI:
    private static final String negrito = "\u001b[1m";
    private static final String vermelho = "\u001b[31m";
    private static final String reset = "\u001b[0m";
    private static final String savePos = "\u001b[s"; // salva a posicao do cursor
    private static final String loadPos = "\u001b[u"; // restaura a ultima posicao salva do cursor
    private static final String limparExcesso = "\u001b[J"; // apaga tudo que estiver depois do cursor

    /**
     * Limpa a tela do terminal.
     */
    protected static void limparTela(){
        System.out.print("\033[H\033[2J");  
        System.out.flush();
    }

    /**
     * Printa o cabecalho do programa (Titulo + PATH)
     * @param path caminho da pagina desejada (por exemplo, "INICIO > GRUPOS").
     */
    protected static void cabecalho(String path){
        limparTela();
        System.out.print(negrito + vermelho + titulo + reset);
        System.out.print(path + "\n\n\n");
    }

    /**
     * Metodo para aguardar uma recao do usuario (para que o mesmo tenha tempo de ler as mensagens de erro).
     */
    protected static void aguardarReacao(){
        System.out.println("\nPressione [enter] para continuar...");
        leitor.nextLine();
    }

    /**
     * @return path + " > " + add
     */
    protected static String addToPath(String path, String add){
        return path + " > " + add;
    }

    /**
     * Rotina a ser executada quando um valor invalido for inserido
     * @param s mensagem de erro a ser exibida
     */
    protected static void valorInvalido(String s){
        System.out.print(loadPos + limparExcesso + s);
        leitor.nextLine();
        System.out.print(loadPos + limparExcesso);
    }
    protected static void valorInvalido(){ valorInvalido(Validacao.erroPadrao); } // mensagem de erro padrao

    /**
     * Rotina padrao de execucao de um menu - solicita uma opçao do usuario.
     * @param path caminho para o menu (exemplo: "INICIO > GRUPOS")
     * @param opcoes arranjo com os textos de cada opcao do menu (NAO INCLUIR A OPCAO DE SAIDA (opcao 0)!!!)
     * @return opcao selecionada pelo usuario
     */
    protected static int selecionarOpcao(String path, String []opcoes){
        // Imprimir opcoes:
        cabecalho(path);
        for(int i=0; i<opcoes.length; i++) System.out.println("["+ (i+1) +"] " + opcoes[i]);
        System.out.print("\n[0] Sair/Cancelar\n");
        
        // Solicitar opcao ao usuario:
        String in;
        System.out.print("\nOpção: " + savePos); // salva posicao do cursor

        // Validar opcao escolhida:
        while( (in = leitor.nextLine()).length()==0  ||  !in.replaceAll("[^0-9]", "").equals(in)  ||  Integer.parseInt(in)>opcoes.length){
            System.out.print( loadPos + limparExcesso ); // restaura a posicao salva do cursor e repete a leitura
        }
        
        return Integer.parseInt(in); // retornar opcao selecionada pelo usuario
    }

    /**
     * Confirma a conclusao de uma operacao (se a operacao for cancelada, exibe mensagem notificando o cancelamento)
     * @return TRUE se operacao confirmada, FALSE se nao.
     */
    protected static boolean confirmarOperacao(){
        System.out.print("Pressione [enter] para confirmar / qualquer outro valor para cancelar alteração: ");
        boolean confirmada = leitor.nextLine().length() == 0;

        if(!confirmada){
            System.out.print("Operação cancelada!");
            aguardarReacao();
        }
        return confirmada;
    }

    /**
     * Solicita um valor float ao usuário.
     * @param txt String com o que sera solicitado (por exemplo, "Valor aproximado").
     * @return float com o valor lido || -1 caso o usuario nao digite valor nenhum
     */
    protected static float lerFloat(String txt){
        String in;
        float valor = -1;

        do{
            System.out.print(txt + ": ");
            in = leitor.nextLine().replace(',', '.');
            if( !in.replaceAll("[^0-9.]", "").equals(in) ) System.out.println("\nErro! Valor inválido!"); // se a entrar nao for um numero
            else if(in.length() > 0) valor = Float.parseFloat(in); // se a entrada nao estiver vazia
            else break;
        }while(valor < 0);

        return valor;
    }

    /**
     * Le uma sequencia de entradas (semelhante a uma rotina de cadastro). Se o usuario enviar uma entrada vazia, a operacao eh cancelada.
     * @param title titulo da rotina de leitura.
     * @param solicitacoes String[] com os dados que serao solicitados (exemplo: {"Nome", "Senha"}).
     * @param acceptEmptyLine booleana que dita se a linha vazia sera aceita ou nao. Se FALSE, uma linha vazia vai abortar a operacao.
     * @return String[] com cada dado lido para cada solicitacao feita || NULL caso a operacao seja abortada.
     */
    protected static String[] lerEntradas(String title, ArrayList<Solicitacao> s, boolean acceptEmptyLine) throws Exception{
        String[] dados = new String[ s.size() ];

        System.out.println(title);
        if(!acceptEmptyLine) System.out.println("(Aperte [enter] para cancelar)\n");

        for(int i=0; i<s.size(); i++){
            System.out.print( s.get(i).getSolicitacao()+ ": " + savePos); // salva posicao do cursor
            
            // Ler entrada:
            boolean valida = false;
            while(!valida){
                dados[i] = leitor.nextLine(); // le entrada
                if(!acceptEmptyLine  &&  dados[i].length() == 0) return null;
                // Validar entrada:
                valida = s.get(i).validar( dados[i] );
                if(!valida) valorInvalido();
            }
        }

        return dados;
    }
    protected static String[] lerEntradas(ArrayList<Solicitacao> s, boolean acceptEmptyLine) throws Exception{
        return lerEntradas(Solicitacao.solicitacaoPadrao, s, acceptEmptyLine); // mensagem se solicitacao padrao
    }

    /**
     * Lista entidades de relacionamento no String[] de destino "lista" (compartilhado entre os menus).
     * @param relacionamento arvore de relacionamento em questao.
     * @param crud CRUD da entidade do relacionamento em questao.
     * @return int[] lista de ids das respectivas entidades vinculadas ao usuario.
     */
    protected static int[] listagem(ArvoreBMais_Int_Int relacionamento, CRUD<?> crud) throws Exception{
        int[] ids = relacionamento.read( idUsuario ); // obter a lista de IDs das sugestoes ligadas ao usuario
        lista = new String[ ids.length ]; // inicializar array destino

        // Realizar listagem das sugestoes:
        for(int i=0; i<ids.length; i++){
            lista[i] = "\t" + crud.read(ids[i]).toString().replace("\n", "\n\t") + "\n"; // armazenar os dados da sugestao atual na lista
        }

        return ids;
    }

    /**
     * Lista entidades de relacionamento na tela.
     * @param relacionamento arvore de relacionamento em questao.
     * @param crud CRUD da entidade do relacionamento em questao.
     */
    protected static void listarEntidade(ArvoreBMais_Int_Int relacionamento, CRUD<?> crud) throws Exception{
        listagem(relacionamento, crud); // charmar metodo que realiza a listagem das respectivas entidades no array compartilhado "lista[]"
        for(int i=0; i<lista.length; i++) System.out.print( (i+1) + lista[i]);
    }

    /**
     * Solicita ao usuario que escolha uma das entidades em questao.
     * @param path caminho atual em que o programa se encontra.
     * @param ids lista de ids das opcoes de entidades.
     * @return id da entidade selecionada pelo usuario || -1, caso o usuario cancele a operacao
     */
    protected static int selecionarEntidade(String path, int[] ids){
        int opcao = -1;
        while(opcao == -1) opcao = selecionarOpcao(path, lista); // solicita ao usuario que escolha uma entidade

        return (opcao==0 ? -1 : ids[ --opcao ]); // retornar id da entidade escolhida pelo usuario ou -1, caso operacao seja cancelada
    }
}