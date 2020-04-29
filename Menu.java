import java.util.Scanner;

public abstract class Menu {
    protected static final Scanner leitor = new Scanner(System.in);
    protected static final String titulo = "AMIGO OCULTO " + AmigoOculto.version + "\n================\n\n";

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
        System.out.print(titulo);
        System.out.print(path + "\n\n");
    }

    /**
     * Metodo para aguardar uma recao do usuario (para que o mesmo tenha tempo de ler as mensagens de erro).
     */
    protected static void aguardarReacao(){
        System.out.println("\n\nPressione [enter] para continuar...");
        leitor.nextLine();
    }

    /**
     * Rotina de execucao de um menu - le uma opçao do usuario.
     * @param path caminho para o submenu (exemplo: "INICIO > GRUPOS")
     * @param opcoes arranjo com os textos de cada opcao do menu (NAO INCLUIR A OPCAO DE SAIDA (opcao 0)!!!)
     * @return opcao selecionada pelo usuario
     */
    protected static char lerOpcao(String path, String []opcoes){
        char opcao;
        boolean erro = true;

        do{
            // Imprimir menu:
            cabecalho(path);
            for(int i=0; i<opcoes.length; i++){
                System.out.println( (i+1) +") " + opcoes[i]);
            }
            System.out.println("\n0) Sair");
            System.out.print("\nOpção: ");

            // Ler opcao:
            String in;
            do{ in = leitor.nextLine().replace("-", ""); } while(in.length() == 0); // desconsiderar numeros negativos (remove sinal '-')
            opcao = in.charAt(0); // extrair apenas o primeiro caractere de entrada

            // Validar opcao:
            erro = (!Character.isDigit(opcao) || Character.getNumericValue(opcao)>opcoes.length);
            if(erro){
                System.out.println("Erro! Opcao '" + opcao + "' Invalida!");
                aguardarReacao();
            }
        }while(erro);

        return opcao;
    }
}