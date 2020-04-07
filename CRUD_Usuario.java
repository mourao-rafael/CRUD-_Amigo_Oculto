import java.io.*;

/**
 * Classe para a implementacao do sistema CRUD (Create, Read, Update, Delete) de Usuários.
 * (Projeto CRUD - Parte 02)
 * @author Rafael Mourão Cerqueira Figueiredo
 * @version 01 - 04/03/2020
 */
public class CRUD_Usuario{
    //Atributos:
    private RandomAccessFile arq;
    private String nomeArquivo;
    //Atributos estaticos:
    private static final String nomePadrao = "users_file.db";

    //Construtores:
    /**
     * Construtor padrao da classe.
     * Quando nao recebe nenhum parametro, cria um arquivo com o nome armazenado em "nomePadrao"
     */
    public CRUD_Usuario(){
        this(CRUD_Usuario.nomePadrao);
    }
    /**
     * Construtor da classe.
     * @param nomeArquivo nome do arquivo de usuarios a ser criado. Se nao receber nenhum, sera usado o valor de "nomePadrao"
     */
    public CRUD_Usuario(String nomeArquivo){
        this.nomeArquivo = nomeArquivo;
        try{
            this.arq = new RandomAccessFile(nomeArquivo, "rw");
            if(arq.length() == 0) arq.writeInt(-1); // se o arquivo acabou de ser criado, inicializar o cabecalho

        } catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Retorna o nome do arquivo de usuarios.
     * @return String com o nome do arquivo de usuarios.
     */
    public String getNomeArquivo(){
        return this.nomeArquivo;
    }

    //Operacoes CRUD:
    /**
     * Metodo para criar um novo usuario no sistema.
     * @param dadosUsuario byte array com os dados do usuario a ser criado.
     * @return int referente ao identificador no novo usuario. Se nao for possivel realizar a insercao, retorna -1.
     */
    public int create(byte[] dadosUsuario){
        int idNovoUsuario = -1;
        
        try{
            //Definir ID do novo usuario:
            arq.seek(0); // move ponteiro para o inicio do arquivo
            idNovoUsuario = arq.readInt()+1;

            //Atualizar cabecalho do arquivo:
            arq.seek(0);
            arq.writeInt(idNovoUsuario);

            //Identificar endereco novo registro:
            arq.seek( arq.length() ); // move ponteiro para o fim do arquivo
            long endNovoRegistro = arq.getFilePointer();

            //Realiza o novo registro:
            arq.writeChar(' '); // lapide
            arq.writeInt(dadosUsuario.length); // indicador de tamanho
            arq.write(dadosUsuario); // registro propriamente dito

            /**
             * TODO:
             * 
             * Incluir o par (ID, endereço) no índice direto (baseado em IDs);
             * Incluir o par (chave secundária, ID) no índice indireto;
            */

        } catch(IOException e){
            e.printStackTrace();
        }

        return idNovoUsuario;
    }

    /**
     * Encontra um determinado usuario no arquivo usando o respectivo identificador.
     * @param idUsuario int com o identificador do usuario a ser procurado.
     * @return Usuario com os dados do usuario pesquisado. Caso o usuario nao exista, retorna NULL.
     */
    public Usuario read(int idUsuario){
        Usuario lido;

        /**
         * PASSO A PASSO:
         * 
         * 1. Buscar o endereço do registro do usuário no índice direto usando o seu ID;
         * 2. Mover o ponteiro no arquivo de usuários para o endereço recuperado;
         * 3. Ler o registro do usuário e criar um novo objeto Usuário a partir desse registro;
         * 4. Retornar esse objeto de usuário
        */
        try{
            arq.seek( enderecoRegistro(idUsuario) ); // move ponteiro para o endereco do registro do usuario a ser lido
            arq.readChar(); // pula o campo da lapide

            //Realizar leitura do registro do usuario em questao:
            byte dadosUsuario[] = new byte[ arq.readInt() ]; // cria byteArray para receber os dados do usuario
            for(int i=0; i<dadosUsuario.length; i++) dadosUsuario[i] = arq.readByte();

            //Criar novo objeto de usuario a partir do registro lido:
            lido = new Usuario();
            lido.fromByteArray( dadosUsuario );

        } catch(IOException e){
            e.printStackTrace();
        }

        return lido;
    }

    /**
     * Encontra um determinado usuario no arquivo usando o respectivo identificador.
     * @param chaveSecundaria String com a chave secundaria do usuario a ser procurado.
     * @return Usuario com os dados do usuario pesquisado. Caso o usuario nao exista, retorna NULL.
     */
    public Usuario read(String chaveSecundaria){
        //
    }
    
    /**
     * Atualiza as informacoes de um determinado usuario no arquivo.
     * @param user Usuario que deseja-se alterar.
     * @return TRUE caso a operacao seja bem sucedida, FALSE caso nao. 
     */
    public boolean update(Usuario user){
        boolean informacoesAtualizadas = false;

        /**
         * PASSO A PASSO:
         * 
         * 1. Buscar o endereço do registro do usuário no índice direto usando o seu ID;
         * 2. Mover o ponteiro no arquivo de usuários para o endereço recuperado;
         * 3. Ler o registro do usuário e identificar se houve variação no tamanho do registro;
         * 4. Se o tamanho não tiver sido alterado,
         *     1. Retornar o ponteiro do arquivo para o endereço recuperado no índice;
         *     2. Escrever o novo registro do usuário, sobrescrevendo o atual;
         * 5. Se o tamanho tiver sido alterado,
         *     1. Retornar o ponteiro do arquivo para o endereço recuperado no índice;
         *     2. Marcar o campo lápide;
         *     3. Mover o ponteiro do arquivo para o fim do arquivo;
         *     4. Identificar o endereço em que o registro será escrito;
         *     5. Escrever o novo registro do usuário;
         *     6. Atualizar o par (ID, endereço) no índice direto.
         * 6. Se o valor da chave secundária (email) tiver sido alterado, atualizar o par (chaveSecundária, ID) no índice
         indireto.
         *
        */

        /**
         * DESAFIO 01:
         * 
         * Crie uma estratégia para gerenciar o espaço excedente em cada registro que tiver diminuído de tamanho, evitando
         que ele precise ser movimentado. Estabeleça um limite percentual máximo para esse espaço (em relação ao tamanho
         do registro), que, se estourado, deve provocar a movimentação do registro.
         *
        */

        return informacoesAtualizadas;
    }

    /**
     * Remove determinado usuario do arquivo de usuarios.
     * @param idUsuario int referente ao usuario que deseja-se remover.
     * @return TRUE caso a operacao seja bem sucedida, FALSE caso nao.
     */
    public boolean delete(int idUsuario){
        boolean usuarioRemovido = false;

        /**
         * PASSO A PASSO:
         * 
         * 1. Buscar o endereço do registro do usuário no índice direto usando o seu ID;
         * 2. Mover o ponteiro no arquivo de usuários para o endereço recuperado;
         * 3. Ler o registro do usuário, para obter a sua chave secundária;
         * 4. Retornar o ponteiro do arquivo para o endereço recuperado no índice;
         * 5. Marcar o campo lápide;
         * 6. Excluir a entrada no índice indireto, usando o valor da chave secundária desse usuário;
         * 7. Excluir a entrada no índice direto, usando o ID desse usuário.
         *
        */

        /**
         * DESAFIO 02:
         * 
         * Crie um mecanismo para aproveitar os espaços vazios deixados pelos registros movidos (ou excluídos). Uma forma
         * de se fazer isso é por meio de uma lista encadeada, ordenada por tamanho, desses espaços, cujo ponteiro para o
         * primeiro espaço deve estar no cabeçalho do arquivo.
         *
        */

        return usuarioRemovido;
    }

    //Outros metodos:
    /**
     * Recupera o endereco do registro de um dado Usuario pelo seu ID
     * @param idUsuario id do usuario em questao
     * @return long com o endereco do usuario em questao
     */
    private long enderecoRegistro(int idUsuario){
        //
    }
}