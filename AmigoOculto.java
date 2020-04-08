import java.io.*;

/**
 * Projeto CRUD - Amigo Oculto (Projeto de um sistema baseado em arquivos)
 * (Projeto feito na disciplina de AEDs-III)
 * @author Rafael Mourão Cerqueira Figueiredo
 * @version 1.0
 */
public class AmigoOculto{
                /* CONSIDERAR: */
    // REFERENTE AO JNI (Java Native Interface):
    // public native int SeletorMenu();
    // static{
        // System.loadLibrary("seletor");
    // }
    // FONTE: https://www.javaworld.com/article/2077520/java-tip-23--write-native-methods.html
    
    public static final String version = "1.0"; // Guarda a versao atual do sistema

    public static void main(String args[]) throws Exception{
        AmigoOculto sistema = new AmigoOculto();

        // MENU PRINCIPAL:
        Menu principal = new Menu();
        principal.setDescricao("MENU PRINCIPAL");
        principal.addOpcao("Acesso ao sistema");
        principal.addOpcao("Novo usuário (primeiro acesso)");
        principal.addOpcao("Sair do programa", "0", 1);

        // MENU ACESSO AO SISTEMA:
        Menu acesso = new Menu();
        acesso.setDescricao("ACESSO AO SISTEMA");
        acesso.addOpcao("Voltar", "Esc", 2);

        // MENU NOVO USUARIO:
        Menu novoUsuario = new Menu();
        novoUsuario.setDescricao("NOVO USUARIO");
        novoUsuario.addOpcao("Voltar", "Esc", 2);

        //========================================================

        // Parte Principal:
        sistema.SeletorMenu();
    }

    /**
     * Limpa a tela do terminal.
     */
    public static void limparTela(){
        System.out.println("\u001b[2J"); // limpa a tela
        System.out.print("\u001b[0;0H"); // move o cursor para o inicio da tela (linha 0, coluna 0)
    }
}