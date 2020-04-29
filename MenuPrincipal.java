public abstract class MenuPrincipal extends Menu{
    public static int idUsuario;

    public static void inicio(){
        // Ler opcao:
        String texto = cabecalho + "\nINICIO\n\n" +"1) Sugestões de presentes\n" +"2) Grupos\n" +"3) Novos convites: 0\n\n" +"0) Sair\n\n";
        char opcao = lerOpcao(texto, "0123".toCharArray());

        switch(opcao){
            case '1':
                sugestoes();
            break;
            case '2':
            break;
            case '3':
            break;
            case '0': return;
        }
    }

    /**
     * Submenu de Sugestoes.
     */
    private static void sugestoes(){
        boolean erro = true;
        do{
            //
        }while(erro);
    }
}