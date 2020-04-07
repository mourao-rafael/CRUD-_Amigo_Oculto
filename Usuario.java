import java.io.*;

/**
 * Classe para criar objetos que representam a entidade "Usuário" do sistema.
 * (Projeto CRUD - Parte 01)
 * @author Rafael Mourão Cerqueira Figueiredo
 * @version 1 - 19/02/2020
 */
class Usuario{
    //Atributos da entidade:
    /* ORDEM: */
    /*  (1)   */ private int id;
    /*  (2)   */ private String nome;
    /*  (3)   */ private String email;
    /*  (4)   */ private String senha;
    /*  (6)   */ private String desejo; // Armazena o que o usuário quer ganhar de presente. Somente o usuário e quem o tirou podem ver.

    //Construtores:
    Usuario(){
        this(0, "", "", "", "");
    }
    Usuario(int id, String nome, String email, String senha, String desejo){
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.desejo = desejo;
    }


    //Setter's:
    public void setId(int id){ this.id = id; }
    public void setNome(String nome){ this.nome = nome; }
    public void setEmail(String email){ this.email = email; }
    public void setSenha(String senha){ this.senha = senha; }
    public void setDesejo(String desejo){ this.desejo = desejo; }
    //Getter's:
    public int getId(){ return this.id; }
    public String getNome(){ return this.nome; }
    public String getEmail(){ return this.email; }
    public String getSenha(){ return this.senha; }
    public String getDesejo(){ return this.desejo; }

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

        //Printar os dados do usuario corrente no byte array:
        /* (1) */ printer.writeInt(this.id);
        /* (2) */ printer.writeUTF(this.nome);
        /* (3) */ printer.writeUTF(this.email);
        /* (4) */ printer.writeUTF(this.senha);
        /* (6) */ printer.writeUTF(this.desejo);

        return dados.toByteArray();
    }

    /**
     * Metodo para extrair, de um byte array, os dados referentes ao usuario corrente.
     * @param dados byte array com os dados referentes ao usuario corrente.
     * @throws IOException caso ocorra algum problema nos objetos de entrada.
     */
    public void fromByteArray(byte[] dados) throws IOException{
        DataInputStream leitor = new DataInputStream( new ByteArrayInputStream(dados) );
        
        //Ler dados referentes ao usuario corrente do byte array:
        /* (1) */ this.id = leitor.readInt();
        /* (2) */ this.nome = leitor.readUTF();
        /* (3) */ this.email = leitor.readUTF();
        /* (4) */ this.senha = leitor.readUTF();
        /* (6) */ this.desejo = leitor.readUTF();
    }
}