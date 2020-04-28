import java.util.Scanner;

public abstract class MenuPrincipal {
    private static final Scanner leitor = new Scanner(System.in);
    public static int idUsuario;

    public static void inicio(){
        boolean erro;
        char opcao;

        do{
            // Imprimir menu principal:
            AmigoOculto.limparTela();
            System.out.print(AmigoOculto.cabecalho + '\n');
            System.out.print("INICIO\n\n");
            System.out.print("1) Sugestões de presentes\n");
            System.out.print("2) Grupos\n");
            System.out.print("3) Novos convites: 0\n\n");
            System.out.print("0) Sair\n\n");
            System.out.print("Opção: ");

            // Ler opcao:
            String in;
            do{ in = leitor.nextLine(); } while(in.length() == 0);
            opcao = in.charAt(0); // extrair apenas o primero caractere

            erro = opcao!='1' && opcao!='2' && opcao!='3' && opcao!='0';
            if(erro){
                System.out.println("\nErro! Opção '" + opcao + "' Inválida!");
				AmigoOculto.aguardarReacao();
            }
        }while(erro);

        switch(opcao){
            case '1':
            break;
            case '2':
            break;
            case '3':
            break;
            case '0':
            break;
        }
    }
}