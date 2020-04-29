import java.util.Scanner;

public abstract class Menu {
    protected static final Scanner leitor = new Scanner(System.in);
    protected static final String cabecalho = "AMIGO OCULTO " + AmigoOculto.version + "\n================\n";

    /**
     * Limpa a tela do terminal.
     */
    protected static void limparTela(){
        System.out.print("\033[H\033[2J");  
        System.out.flush();
    }

    /**
     * Metodo para aguardar uma recao do usuario (para que o mesmo tenha tempo de ler as mensagens de erro).
     */
    protected static void aguardarReacao(){
        System.out.println("Pressione [enter] para continuar...");
        leitor.nextLine();
    }

    /**
     * Rotina de execucao de um menu.
     * @param textoMenu String com o menu propriamente dito
     * @param opcoesValidas char[] com as opcoes validas de entrada
     * @return opcao selecionada pelo usuario
     */
    protected static char lerOpcao(String textoMenu, char opcoesValidas[]){
        char opcao;
        boolean erro = true;

        do{
            // Imprimir menu:
            limparTela();
            System.out.print(textoMenu);
            System.out.print("Opção: ");

            // Ler opcao:
            String in;
            do{ in = leitor.nextLine(); } while(in.length() == 0);
            opcao = in.charAt(0); // extrair apenas o primeiro caractere de entrada

            // Validar opcao:
            for(int i=0; i<opcoesValidas.length && erro; i++) if(opcoesValidas[i] == opcao) erro = false;
            if(erro){
                System.out.println("Erro! Opcao '" + opcao + "' Invalida!");
                aguardarReacao();
            }
        }while(erro);

        return opcao;
    }
}