import java.io.*;

/**
 * Interface para a representacao de ENTIDADES.
 */
public interface Entidade {
    public int getId();
    public void setId(int id);
    public String chaveSecundaria();
    public byte[] toByteArray() throws IOException;
    public void fromByteArray(byte[] dados) throws IOException;
}


/**
 * * Classe para criar objetos que representam a entidade "Sugestao".
 */
class Sugestao implements Entidade{
    // Atributos:
    private int id;
    private int idUsuario;
    private String produto;
    private String loja;
    private float valor;
    private String observacoes;

    // Construtores:
    Sugestao(){
        this(-1, -1, "", "", (float)-1, "");
    }
    Sugestao(int id, int idUsuario, String produto, String loja, float valor, String observacoes){
        this.id = id;
        this.idUsuario = idUsuario;
        this.produto = produto;
        this.loja = loja;
        this.valor = valor;
        this.observacoes = observacoes;
    }
    Sugestao(byte[] dados) throws IOException{
        this.fromByteArray(dados);
    }

    // Setter's:
    public void setId(int id){ this.id = id; }
    public void setIdUsuario(int idUsuario){ this.idUsuario = idUsuario; }
    public void setProduto(String produto){ this.produto = produto; }
    public void setLoja(String loja){ this.loja = loja; }
    public void setValor(float valor){ this.valor = valor; }
    public void setObservacoes(String observacoes){ this.observacoes = observacoes; }
    // Getter's:
    public int getId(){ return this.id; }
    public int getIdUsuario(){ return this.idUsuario; }
    public String getProduto(){ return this.produto; }
    public String getLoja(){ return this.loja; }
    public float getValor(){ return this.valor; }
    public String getObservacoes(){ return this.observacoes; }

    // Metodos:
    public String chaveSecundaria(){
        return this.idUsuario + "|" + this.produto;
    }

    public byte[] toByteArray() throws IOException{
        ByteArrayOutputStream dados = new ByteArrayOutputStream();
        DataOutputStream printer = new DataOutputStream(dados);

        // Escrever os dados na devida ordem:
        printer.writeInt(this.id);
        printer.writeInt(this.idUsuario);
        printer.writeUTF(this.produto);
        printer.writeUTF(this.loja);
        printer.writeFloat(this.valor);
        printer.writeUTF(this.observacoes);

        return dados.toByteArray();
    }

    public void fromByteArray(byte[] dados) throws IOException{
        DataInputStream reader = new DataInputStream( new ByteArrayInputStream(dados) );

        // Ler os dados na devida ordem:
        this.id = reader.readInt();
        this.idUsuario = reader.readInt();
        this.produto = reader.readUTF();
        this.loja = reader.readUTF();
        this.valor = reader.readFloat();
        this.observacoes = reader.readUTF();
    }

    /**
     * Metodo para imprimir os dados (nao confidenciais) da sugestao corrente em uma string.
     * @return String com os dados da sugestao
     */
    public String toString(){
        String dados = "Produto: " + this.produto + "\n";
        if(this.loja.length() > 0) dados += "Loja: " + this.loja + "\n";
        if(this.valor >= 0) dados += "Valor aproximado: " + this.valor + "\n";
        if(this.observacoes.length() > 0) dados += "Observações: " + this.observacoes + "\n";
        return dados;
    }
}

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