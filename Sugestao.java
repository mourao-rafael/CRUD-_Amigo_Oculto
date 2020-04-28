import java.io.*;

/**
 * * Classe para criar objetos que representam a entidade "Sugestao".
 */
public class Sugestao implements Entidade{
    // Atributos:
    private int idSugestao;
    private int idUsuario;
    private String produto;
    private String loja;
    private float valor;
    private String observacoes;

    // Construtores:
    Sugestao(){
        this(-1, -1, "", "", (float)-1, "");
    }
    Sugestao(int idSugestao, int idUsuario, String produto, String loja, float valor, String observacoes){
        this.idSugestao = idSugestao;
        this.idUsuario = idUsuario;
        this.produto = produto;
        this.loja = loja;
        this.valor = valor;
        this.observacoes = observacoes;
    }
    Sugestao(byte[] dados) throws IOException{
        this.fromByteArray(dados);
    }

    // Setter's && Getter's:
    public int getId(){    return this.idSugestao;    }
    public void setId(int idSugestao){    this.idSugestao = idSugestao;    }

    // Metodos:
    public String chaveSecundaria(){
        return this.idUsuario + "|" + this.produto;
    }

    public byte[] toByteArray() throws IOException{
        ByteArrayOutputStream dados = new ByteArrayOutputStream();
        DataOutputStream printer = new DataOutputStream(dados);

        // Escrever os dados na devida ordem:
        printer.writeInt(this.idSugestao);
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
        this.idSugestao = reader.readInt();
        this.idUsuario = reader.readInt();
        this.produto = reader.readUTF();
        this.loja = reader.readUTF();
        this.valor = reader.readFloat();
        this.observacoes = reader.readUTF();
    }
}