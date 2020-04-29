import java.io.*;

/**
 * Classe para criar objetos que representam a entidade "Usuário".
 */
class Usuario implements Entidade{
    // Atributos:
    private int id;
    private String nome;
    private String email;
    private String senha;

    // Construtores:
    Usuario(){
        this(-1, "", "", "");
    }
    Usuario(int id, String nome, String email, String senha){
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }
    Usuario(byte[] dados) throws IOException{
        this.fromByteArray(dados);
    }


    // Setter's:
    public void setId(int id){ this.id = id; }
    public void setNome(String nome){ this.nome = nome; }
    public void setEmail(String email){ this.email = email; }
    public void setSenha(String senha){ this.senha = senha; }
    // Getter's:
    public int getId(){ return this.id; }
    public String getNome(){ return this.nome; }
    public String getEmail(){ return this.email; }
    public String getSenha(){ return this.senha; }

    //Demais Métodos:
    /**
     * Funcao para retornar a chave de ordenacao secundaria (email) do usuario corrente.
     * @return String referente ao email do usuario corrente.
     */
    public String chaveSecundaria(){
        return this.email;
    }

    /**
     * Metodo para escrever os dados do usuario corrente em um byte array.
     * @return byte array com os dados do usuario corrente.
     * @throws IOException caso ocorra algum problema nos objetos de saida.
     */
    public byte[] toByteArray() throws IOException{
        ByteArrayOutputStream dados = new ByteArrayOutputStream();
        DataOutputStream printer = new DataOutputStream(dados);

        // Escrever os dados na devida ordem:
        printer.writeInt(this.id);
        printer.writeUTF(this.nome);
        printer.writeUTF(this.email);
        printer.writeUTF(this.senha);

        return dados.toByteArray();
    }

    /**
     * Metodo para extrair, de um byte array, os dados referentes ao usuario corrente.
     * @param dados byte array com os dados referentes ao usuario corrente.
     * @throws IOException caso ocorra algum problema nos objetos de entrada.
     */
    public void fromByteArray(byte[] dados) throws IOException{
        DataInputStream leitor = new DataInputStream( new ByteArrayInputStream(dados) );
        
        // Ler os dados na devida ordem:
        this.id = leitor.readInt();
        this.nome = leitor.readUTF();
        this.email = leitor.readUTF();
        this.senha = leitor.readUTF();
    }

    /**
     * Metodo para imprimir os dados (nao confidenciais) do usuario corrente em uma string.
     * @return String com os dados do usuario.
     */
    public String toString(){
        return ("Nome: " + this.nome) + ("\nEmail: " + this.email);
    }
}