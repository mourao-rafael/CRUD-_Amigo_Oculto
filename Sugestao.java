import java.io.*;

/**
 * * Classe para criar objetos que representam a entidade "Sugestao".
 */
public class Sugestao implements Entidade{
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