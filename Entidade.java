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
    public String toString();
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
    Sugestao(int idUsuario, String produto, String loja, String valor, String observacoes){
        this(-1, idUsuario, produto, loja, Float.parseFloat(valor.replace(",",".")), observacoes);
    }
    Sugestao(int idUsuario, String produto, String loja, float valor, String observacoes){
        this(-1, idUsuario, produto, loja, valor, observacoes);
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
        if(this.valor >= 0) dados += "Valor aproximado: " + String.format("%.2f", this.valor) + "\n";
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
        this("", "", "");
    }
    Usuario(String nome, String email, String senha){
        this(-1, nome, email, senha);
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

/**
 * Classe para criar objetos que representam a entidade "Grupo de Amigos".
 */
class Grupo implements Entidade{
    // Atributos:
    private int id;
    private int idUsuario;
    private String nome;
    private long momentoSorteio;
    private float valor;
    private long momentoEncontro;
    private String localEncontro;
    private String observacoes;
    private boolean sorteado;
    private boolean ativo;

    // Construtores:
    Grupo(){
        this(-1, -1, "", (long)-1, (float)-1, (long)-1, "", "", false, true);
    }
    Grupo(int idUsuario, String nome, long momentoSorteio, float valor, long momentoEncontro, String localEncontro, String observacoes, boolean sorteado, boolean ativo){
        this(-1, idUsuario, nome, momentoSorteio, valor, momentoEncontro, localEncontro, observacoes, sorteado, ativo);
    }
    Grupo(int idUsuario, String nome, String momentoSorteio, String valor, String momentoEncontro, String localEncontro, String observacoes) throws Exception{
        this.id = -1;
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.momentoSorteio = momentoSorteio.isEmpty() ? -1 : TUI.converterData(momentoSorteio);
        this.valor = valor.isEmpty() ? -1 : Float.parseFloat(valor);
        this.momentoEncontro = momentoEncontro.isEmpty() ? -1 : TUI.converterData(momentoEncontro);
        this.localEncontro = localEncontro;
        this.observacoes = observacoes;
        this.sorteado = false;
        this.ativo = true;
    }
    Grupo(int id, int idUsuario, String nome, long momentoSorteio, float valor, long momentoEncontro, String localEncontro, String observacoes, boolean sorteado, boolean ativo){
        this.id = id;
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.momentoSorteio = momentoSorteio;
        this.valor = valor;
        this.momentoEncontro = momentoEncontro;
        this.localEncontro = localEncontro;
        this.observacoes = observacoes;
        this.sorteado = sorteado;
        this.ativo = ativo; 
    }
    Grupo(byte [] dados) throws IOException{
        this.fromByteArray(dados);
    }

    // Setter's 
    public void setId(int id){ this.id = id; }
    public void setIdUsuario(int idUsuario){ this.idUsuario = idUsuario; }
    public void setNome(String nome){ this.nome = nome; }
    public void setMomentoSorteio(long momentoSorteio){ this.momentoSorteio = momentoSorteio; }        
    public void setValor(float valor){ this.valor = valor; }
    public void setMomentoEncontro(long momentoEncontro){ this.momentoEncontro = momentoEncontro; }
    public void setLocalEncontro(String localEncontro){ this.localEncontro = localEncontro; }
    public void setObservacoes(String observacoes){ this.observacoes = observacoes; }
    public void setSorteado(boolean sorteado){ this.sorteado = sorteado; }
    public void setAtivo(boolean ativo){ this.ativo = ativo;}
    // Getter's
    public int getId(){ return this.id; }
    public int getIdUsuario(){ return this.idUsuario; }
    public String getNome(){ return this.nome; }
    public long getMomentoSorteio(){ return this.momentoSorteio; }
    public float getValor(){ return this.valor; }
    public long getMomentoEncontro(){ return this.momentoEncontro; }
    public String getLocalEncontro(){ return this.localEncontro; }
    public String getObservacoes(){ return this.observacoes; }
    public boolean getSorteado(){ return this.sorteado; }
    public boolean getAtivo(){ return this.ativo; }
    
    // Demais Métodos:
    public String chaveSecundaria(){
        return this.idUsuario + "|" + this.nome;
    }

    public byte[] toByteArray() throws IOException{
        ByteArrayOutputStream dados = new ByteArrayOutputStream();
        DataOutputStream printer = new DataOutputStream(dados);

        //Escrever os dados na devida ordem:
        printer.writeInt(this.id);
        printer.writeInt(this.idUsuario);
        printer.writeUTF(this.nome);
        printer.writeLong(this.momentoSorteio);
        printer.writeFloat(this.valor);
        printer.writeLong(this.momentoEncontro);
        printer.writeUTF(this.localEncontro);
        printer.writeUTF(this.observacoes);
        printer.writeBoolean(this.sorteado);
        printer.writeBoolean(this.ativo);

        return dados.toByteArray();
    }

    public void fromByteArray(byte[] dados) throws IOException{
        DataInputStream reader = new DataInputStream(new ByteArrayInputStream(dados));

        //Ler os dados na devida ordem:
        this.id = reader.readInt();
        this.idUsuario = reader.readInt();
        this.nome = reader.readUTF();
        this.momentoSorteio = reader.readLong();
        this.valor = reader.readFloat();
        this.momentoEncontro = reader.readLong();
        this.localEncontro = reader.readUTF();
        this.observacoes = reader.readUTF();
        this.sorteado = reader.readBoolean();
        this.ativo = reader.readBoolean(); 
    }

    /**
     * Metodo para imprimir os dados (nao confidenciais) da sugestao corrente em uma string.
     * @return String com os dados da sugestao || NULL, se o grupo estiver desativado
     */
    public String toString(){
        if(this.ativo){
            String dados = "Nome: " + this.nome + "\n";
            if(this.momentoSorteio >= 0) dados += "Data do sorteio: " + TUI.formatarData(this.momentoSorteio) + "\n";
            if(this.valor >= 0) dados += "Valor aproximado: " + String.format("%.2f", this.valor) + "\n";
            if(this.momentoEncontro >= 0) dados += "Data de encontro: " + TUI.formatarData(this.momentoEncontro) + "\n";
            if(this.localEncontro.length() > 0) dados +=  "Local do Encontro: " + this.localEncontro + "\n";
            if(this.observacoes.length() > 0) dados += "Observações: " + this.observacoes + "\n";
            return dados;
        }
        else return null;
    }
}

/**
 * Classe para criar objetos que representam a entidade "Convite".
 */
class Convite implements Entidade{
    // Estados do convite:
    public static final byte pendente = 0;
    public static final byte aceito = 1;
    public static final byte recusado = 2;
    public static final byte cancelado = 3;
    
    // Atributos:
    private int id;
    private int idGrupo;
    private String email;
    private long momentoConvite;
    private byte estado; // 0: pendente, 1: aceito, 2: recusado, 3: cancelado

    // Construtores:
    Convite(){
        this(-1, -1, "", (long)-1, (byte)-1);
    }
    Convite(int id, int idGrupo, String email, long momentoConvite, byte estado){
        this.id = id;
        this.idGrupo = idGrupo;
        this.email = email;
        this.momentoConvite = momentoConvite;
        this.estado = estado;
    }
    Convite(byte [] dados) throws IOException{
        this.fromByteArray(dados);
    }

    // Setter's 
    public void setId(int id){ this.id = id; }
    public void setIdGrupo(int idGrupo){ this.idGrupo = idGrupo; }
    public void setEmail(String email){ this.email = email; }
    public void setMomentoConvite(long momentoConvite){ this.momentoConvite = momentoConvite; }
    public void setEstado(byte estado){ this.estado = estado; }
    // Getter's
    public int getId(){ return this.id; }
    public int getIdGrupo(){ return this.idGrupo; }
    public String getEmail(){ return this.email; }
    public long getMomentoConvite(){ return this.momentoConvite; }
    public byte getEstado(){ return this.estado; }

    // Demais Métodos:
    public String chaveSecundaria(){
        return this.idGrupo + "|" + this.email;
    }

    public byte[] toByteArray() throws IOException{
        ByteArrayOutputStream dados = new ByteArrayOutputStream();
        DataOutputStream printer = new DataOutputStream(dados);

        //Escrever os dados na devida ordem:
        printer.writeInt(this.id);
        printer.writeInt(this.idGrupo);
        printer.writeUTF(this.email);
        printer.writeLong(this.momentoConvite);
        printer.writeByte(this.estado);

        return dados.toByteArray();
    }

    public void fromByteArray(byte[] dados) throws IOException{
        DataInputStream reader = new DataInputStream(new ByteArrayInputStream(dados));

        //Ler os dados na devida ordem:
        this.id = reader.readInt();
        this.idGrupo = reader.readInt();
        this.email = reader.readUTF();
        this.momentoConvite = reader.readLong();
        this.estado = reader.readByte();
    }

    /**
     * Metodo para imprimir os dados (nao confidenciais) da sugestao corrente em uma string.
     * @return String com os dados da sugestao
     */
    public String toString(){
        String dados = "Email: " + this.email + "(" + this.momentoConvite + "-" + this.estado + ")";
        return dados;
    }

    // Metodos para verificar o estado do convite:
    public boolean pendente(){ return this.estado == pendente; }
    public boolean aceito(){ return this.estado == aceito; }
    public boolean recusado(){ return this.estado == recusado; }
    public boolean cancelado(){ return this.estado == cancelado; }
}

/**
 * Classe para criar objetos que representam a entidade "Participação".
 */
class Participacao implements Entidade{
    // Atributos:
    private int idParticipacao;
    private int idUsuario;
    private int idGrupo;
    private int idAmigo;

    // Construtores:
    public Participacao(){
        this(-1, -1, -1, -1);
    }

    public Participacao(int idParticipacao, int idUsuario, int idGrupo, int idAmigo){
        this.idParticipacao = idParticipacao;
        this.idUsuario = idUsuario;
        this.idGrupo = idGrupo;
        this.idAmigo = idAmigo;
    }

    // Setter's
    public void setId(int idParticipacao){ this.idParticipacao = idParticipacao; }
    public void setIdUsuario(int idUsuario){ this.idUsuario = idUsuario; }
    public void setIdGrupo(int idGrupo){ this.idGrupo = idGrupo; }
    public void setIdAmigo(int idAmigo){ this.idAmigo = idAmigo; }
    // Getter's 
    public int getId(){ return this.idParticipacao; }
    public int getIdUsuario(){ return this.idUsuario; }
    public int getIdGrupo(){ return this.idGrupo; }
    public int getIdAmigo(){ return this.idAmigo; }

    // Demais Métodos:
    /**
     * Funcao para retornar a chave de ordenacao secundaria (email) do usuario corrente.
     * @return String referente ao email do usuario corrente.
     */
    public String chaveSecundaria(){
        return this.idUsuario + "|" + this.idGrupo;
    }
    
    /**
     * Metodo para escrever os dados do usuario corrente em um byte array.
     * @return byte array com os dados do usuario corrente.
     * @throws IOException caso ocorra algum problema nos objetos de saida.
     */
    public byte[] toByteArray() throws IOException{
        ByteArrayOutputStream dados = new ByteArrayOutputStream();
        DataOutputStream printer = new DataOutputStream(dados);
    
        //Escrever os dados na devida ordem:
        printer.writeInt(this.idParticipacao);
        printer.writeInt(this.idUsuario);
        printer.writeInt(this.idGrupo);
        printer.writeInt(this.idAmigo);
    
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
        this.idParticipacao = leitor.readInt();
        this.idUsuario = leitor.readInt();
        this.idGrupo = leitor.readInt();
        this.idAmigo = leitor.readInt();
    }
}