import java.util.Scanner;
import java.util.ArrayList;
import java.util.Date;

/**
 * Classe para o gerenciamento da TUI (Text User Interface).
 */
public abstract class TUI extends AmigoOculto{
    protected static final Scanner leitor = new Scanner(System.in);
    protected static final String titulo = "AMIGO OCULTO " + version;
    protected static String lista[]; // String[] compartilhado entre os menus (armazenara as listagens realizadas)

    // VALORES PADRAO:
    protected static final String erroPadrao = "Erro! Valor Inválido!"; // mensagem de erro padrao

    // CODIGOS ANSI:
    protected static final String negrito = "\u001b[1m";
    protected static final String vermelho = "\u001b[31m";
    protected static final String reset = "\u001b[0m";
    protected static final String savePos = "\u001b[s"; // salva a posicao do cursor
    protected static final String loadPos = "\u001b[u"; // restaura a ultima posicao salva do cursor
    protected static final String limparAposCursor = "\u001b[J"; // apaga tudo que estiver depois do cursor
    
    // ATRIBUTOS/VARIAVEIS DE CONTROLE:
    protected static final String novaEtapa = "\u001b[5;1H" + limparAposCursor + "\n\n"; // apaga tudo apos o PATH e move para a linha 7
    protected static final String gotoPathLine = "\u001b[4;1H"; // move cursor para a linha do PATH (Linha 4)
    protected static String currentPath = "";


    // METODOS:
    /**
     * Inicializa a interface de usuario.
     */
    public static void start(){
        System.out.print("\u001b[1;1H"); // move cursor para o inicio da tela (Linha 1; Coluna 1)
        System.out.print(limparAposCursor); // limpa a tela inteira
        // Printar o cabecalho da interface de usuario:
        System.out.print(negrito+vermelho+ (titulo +'\n'+ "=".repeat(titulo.length()-1) ) +reset +"\n\n");
        addToPath("ACESSO"); // inicializa o path para o menu de acesso
    }

    /**
     * Metodo para aguardar uma recao do usuario.
     */
    protected static void aguardarReacao(){
        System.out.println("\n\nPressione [enter] para continuar...");
        leitor.nextLine();
    }

    /**
     * Adiciona um novo "elemento" no PATH.
     * @param add "elemento" a ser adicionado. (Exemplo: "Sugestoes")
     */
    protected static void addToPath(String add){
        currentPath += add.toUpperCase(); // adiciona o novo elemento ao path
        System.out.print(gotoPathLine + currentPath); // escreve o novo path
        System.out.print(novaEtapa); // apaga tudo apos o path e move para a linha 7
    }

    /**
     * Remove o ultimo elemento do PATH.
     */
    protected static void returnPath(){
        // Encontrar o indice (coluna) de retorno do path:
        int returnIndex = currentPath.lastIndexOf(" > ");

        if(returnIndex != -1){
            currentPath = currentPath.substring(0, returnIndex); // remove ultimo elemento do path
            System.out.print(gotoPathLine+limparAposCursor+ currentPath +"\n\n\n"); // escreve o novo path e move para a linha 7
        }
    }

    /**
     * Apaga tudo depois do path, e move o cursor para a linha 7.
     */
    protected static void novaEtapa(){
        System.out.print(novaEtapa);
    }




    // METODOS AUXILIARES ===================================================================================:
    /**
     * Rotina a ser executada quando um valor invalido for inserido
     * @param s mensagem de erro a ser exibida
     */
    protected static void valorInvalido(String s){
        System.out.print(loadPos + limparAposCursor + s + " Tecle [enter] para tentar novamente: ");
        leitor.nextLine();
        System.out.print(loadPos + limparAposCursor);
    }

    /**
     * Rotina padrao de execucao de um menu - solicita uma opçao do usuario.
     * @param mensagem mensagem de solicitacao a ser exibida. PADRAO: "Selecione uma opção"
     * @param opcoes arranjo com os textos de cada opcao do menu (NAO INCLUIR A OPCAO DE SAIDA (opcao 0)!!!)
     * @return opcao selecionada pelo usuario
     */
    protected static int selecionarOpcao(String mensagem, String []opcoes){
        System.out.println(mensagem + '\n');

        // Imprimir opcoes:
        for(int i=0; i<opcoes.length; i++) System.out.println("["+ (i+1) +"] " + opcoes[i]);
        System.out.print("\n[0] Sair/Cancelar\n");
        
        // Solicitar opcao ao usuario:
        System.out.print("\nOpção: " + savePos); // salva posicao do cursor
        // Validar opcao escolhida:
        String in;
        while( (in = leitor.nextLine()).length()==0  ||  !in.replaceAll("[^0-9]", "").equals(in)  ||  Integer.parseInt(in)>opcoes.length){
            valorInvalido(erroPadrao); // exibir mensagem de valor invalido.
        }
        
        return Integer.parseInt(in); // retornar opcao selecionada pelo usuario
    }
    protected static int selecionarOpcao(String[] opcoes){
        return selecionarOpcao("Selecione uma opção", opcoes);
    }

    /**
     * Imprime mensagem de operacao cancelada e aguarda reacao do usuario
     */
    protected static void operacaoCancelada(){
        System.out.print("Operação cancelada!");
        aguardarReacao();
    }

    /**
     * Confirma a conclusao de uma operacao (se a operacao for cancelada, exibe mensagem notificando o cancelamento)
     * @return TRUE se operacao confirmada, FALSE se nao.
     */
    protected static boolean confirmarOperacao(){
        System.out.print("Pressione [enter] para confirmar / qualquer outro valor para cancelar operação: ");
        boolean confirmada = leitor.nextLine().length() == 0;

        if(!confirmada) operacaoCancelada();
        return confirmada;
    }

    /**
     * Le uma sequencia de entradas (semelhante a uma rotina de cadastro). Se o usuario enviar uma entrada vazia, a operacao eh cancelada.
     * @param title titulo da rotina de leitura.
     * @param solicitacoes String[] com os dados que serao solicitados (exemplo: {"Nome", "Senha"}).
     * @return String[] com cada dado lido para cada solicitacao feita || NULL caso a operacao seja abortada.
     */
    protected static String[] lerEntradas(String title, ArrayList<Solicitacao> s) throws Exception{
        String[] dados = new String[ s.size() ];
        System.out.println(title);

        for(int i=0; i<s.size(); i++){
            System.out.print( s.get(i).getSolicitacao()+ ": " + savePos); // salva posicao do cursor
            
            // Ler entrada:
            boolean valida = false;
            while(!valida){
                dados[i] = leitor.nextLine(); // le entrada
                // Validar entrada:
                if(dados[i].length() == 0){
                    if(!s.get(i).acceptEmptyLine()) return null;
                    else valida = true;
                }
                else valida = s.get(i).validar(dados[i]);
            }
        }

        return dados;
    }
    protected static String[] lerEntradas(ArrayList<Solicitacao> s) throws Exception{
        return lerEntradas(Solicitacao.solicitacaoPadrao, s); // mensagem se solicitacao padrao
    }

    /**
     * Lista entidades de relacionamento no String[] de destino "lista" (compartilhado entre os menus).
     * @param relacionamento arvore de relacionamento em questao.
     * @param idChave id da chave de busca para a arvore de relacionamento
     * @param crud CRUD da entidade do relacionamento em questao.
     * @return int[] lista de ids das respectivas entidades vinculadas ao usuario.
     */
    protected static int[] listagem(ArvoreBMais_Int_Int relacionamento, int idChave, CRUD<?> crud) throws Exception{
        ArrayList <Integer> idsValidos = new ArrayList<>();
        int[] ids = relacionamento.read( idChave ); // obter a lista de IDs das entidades
        String listaAux[] = new String[ ids.length ]; // inicializar array destino

        // Realizar listagem das sugestoes:
        int count = 0;
        for(int i=0; i<ids.length; i++){
            if(crud.read(ids[i]).toString() != null){
                listaAux[count++] = "\t" + crud.read(ids[i]).toString().replace("\n", "\n\t") + "\n"; // armazenar os dados da sugestao atual na lista
                idsValidos.add(ids[i]);
            }
        }
        
        // Copiar lista no array compartilhado:
        lista = new String[count];
        for(int i=0; i<count; i++) lista[i] = listaAux[i];
        
        // Settar ids validos:
        ids = new int[ idsValidos.size() ];
        for(int i=0; i<ids.length; i++) ids[i] = idsValidos.get(i);

        return ids;
    }
    protected static int[] listagem(ArvoreBMais_Int_Int relacionamento, CRUD<?> crud) throws Exception{
        return listagem(relacionamento, idUsuario, crud);
    }

    /**
     * Lista entidades de relacionamento na tela.
     * @param relacionamento arvore de relacionamento em questao.
     * @param idChave id da chave de busca para a arvore de relacionamento
     * @param crud CRUD da entidade do relacionamento em questao.
     */
    protected static void listarEntidade(ArvoreBMais_Int_Int relacionamento, int idChave, CRUD<?> crud) throws Exception{
        listagem(relacionamento, idChave, crud); // charmar metodo que realiza a listagem das respectivas entidades no array compartilhado "lista[]"
        for(int i=0; i<lista.length; i++) System.out.print( (i+1) + lista[i]);
    }
    protected static void listarEntidade(ArvoreBMais_Int_Int relacionamento, CRUD<?> crud) throws Exception{
        listarEntidade(relacionamento, idUsuario, crud);
    }

    /**
     * Solicita ao usuario que escolha uma das entidades em questao.
     * @param ids lista de ids das opcoes de entidades.
     * @param idChave id da chave de busca para a arvore de relacionamento
     * @return id da entidade selecionada pelo usuario || -1, caso o usuario cancele a operacao
     */
    protected static int selecionarEntidade(int[] ids){
        int opcao = selecionarOpcao(lista); // solicita ao usuario que escolha uma entidade
        return (opcao==0 ? -1 : ids[ --opcao ]); // retornar id da entidade escolhida pelo usuario ou -1, caso operacao seja cancelada
    }

    /**
     * Retorna a data atual em long.
     * @return
     */
    protected static long dataAtual(){
        return new Date().getTime();
    }

}