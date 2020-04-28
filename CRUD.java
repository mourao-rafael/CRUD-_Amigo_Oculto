import java.io.*;
import java.lang.reflect.Constructor;

/**
 * Classe para a implementacao das rotinas CRUD (Create, Read, Update, Delete) GENERICO. (Será usada para as ENTIDADES no geral).
 */
public class CRUD <T extends Entidade>{
    // Atributos:
    private String nomeArquivo;
    private RandomAccessFile arq;
    private HashExtensível indice_direto;
    private ArvoreBMais_String_Int indice_indireto;
    Constructor<T> constructor; // construtor generico

    // Construtor:
    public CRUD(String nomeArquivo, Constructor<T> constructor) throws Exception{
        this.nomeArquivo = nomeArquivo;
        this.constructor = constructor;

        // Abrir arquivo:
        this.arq = new RandomAccessFile(nomeArquivo, "rw");
        if(this.arq.length() == 0) arq.writeInt(-1); // se o arquivo acabou de ser criado, inicializar o cabecalho

        // Inicializar indices:
        indice_direto = new HashExtensível(10, this.nomeArquivo + ".indiceDireto_diretorio.ind", this.nomeArquivo + ".indiceDireto_cestos.ind");
        indice_indireto = new ArvoreBMais_String_Int(10, this.nomeArquivo + ".indiceIndireto.ind");
    }

    // Metodos:
    /**
     * Cria um novo registro
     * @param dados dados do novo registro a ser realizado
     * @return id do novo registro realizado
     */
    public int create(byte[] dados) throws Exception{
        T objeto = this.constructor.newInstance(dados);

        // Definir id do novo registro:
        arq.seek(0);
        objeto.setId(arq.readInt() + 1);
        
        // Atualizar cabecalho do arquivo:
        arq.seek(0);
        arq.writeInt(objeto.getId());
        
        // Realizar novo registro:
        long endNovoReg = arq.length(); // armazenar endereco do novo registro
        arq.seek(endNovoReg); // mover ponteiro para o final do arquivo
        arq.writeChar(' '); // lapide
        arq.writeInt(dados.length); // tamanho do novo registro
        arq.write(dados); // rescrever o registro propriamente dito

        // Incluir novos indices:
        indice_direto.create(objeto.getId(), endNovoReg);
        indice_indireto.create(objeto.chaveSecundaria(), objeto.getId());

        return objeto.getId();
    }

    /**
     * Le um determinado registro do arquivo a partir do ID do mesmo.
     * @param id id do registro a ser lido
     * @return <Objeto> registro lido || NULL se o registro nao existir
     */
    public T read(int id) throws Exception{
        T lido = null;

        long end = indice_direto.read(id); // Recuperar o endereco do registro no indice direto
        
        if(end >= 0){
            arq.seek(end); // move para o endereco do registro a ser lido
            
            if(arq.readChar() == ' '){ // valida existencia do registro
                byte[] dados = new byte[ arq.readInt() ]; // le o tamanho do registro
                arq.read(dados); // le o registro propriamente dito
                lido = constructor.newInstance(dados); // cria objeto do registro em questao
            }
        }

        return lido;
    }

    /**
     * Le um determinado registro do arquivo a partir da CHAVE SECUNDARIA do mesmo.
     * @param chaveSecundaria String com a chave secundaria do registro a ser lido
     * @return <Objeto> registro lido || NULL se o registro nao existir
     */
    public T read(String chaveSecundaria) throws Exception{
        return read( this.indice_indireto.read(chaveSecundaria) ); // recupera o id no indice indireto a partir da chave secundaria
    }

    /**
     * Atualiza as informacoes de um determinado registro no arquivo.
     * @param novo <Objeto> do registro a ser atualizado
     * @return TRUE caso a operacao seja bem sucedida, FALSE caso nao.
     */
    public boolean update(T novo) throws Exception{
        boolean atualizado = false;

        long end = indice_direto.read( novo.getId() ); // recupera endereco no indice direto
        if(end >= 0){
            arq.seek(end); // move o ponteiro para o endereco do registro a ser atualizado

            if(arq.readChar() == ' '){ // valida existencia do registro
                // Criar arrays com os dados dos registros (antigo e novo):
                byte[] dadosAtualizados = novo.toByteArray();
                byte[] dadosAntigos = new byte[arq.readInt()]; // le o tamanho atual do registro
                arq.read(dadosAntigos);

                // Atualizar o registro:
                arq.seek(end);
                if(dadosAntigos.length == dadosAtualizados.length){ // se o tamanho for o mesmo:
                    arq.readChar(); // pular lapide
                    arq.readInt(); // pular tamanho do registro
                    arq.write(dadosAtualizados); // atualizar registro
                }
                else{ // se houver variacao no tamanho:
                    arq.writeChar('*'); // marcar o campo lapide

                    // Ir para o final do arquivo:
                    end = arq.length(); // armazenar o novo endereco do registro
                    arq.seek(end);

                    // Realizar novo registro:
                    arq.writeChar(' '); // lapide
                    arq.writeInt(dadosAtualizados.length); // tamanho do registro atualizado
                    arq.write(dadosAtualizados); // registro propriamente dito
                    indice_direto.update(novo.getId(), end); // atualizar indice direto
                }

                // Atualizar o indice indireto (caso a chave secundaria tiver sido alterada):
                if(!novo.chaveSecundaria() .equals( constructor.newInstance(dadosAntigos).chaveSecundaria() )) indice_indireto.update(novo.chaveSecundaria(), novo.getId());
                atualizado = true;
            }
        }

        return atualizado;
    }

    /**
     * Remove um determinado registro do arquivo.
     * @param id id do registro a ser removido
     * @return TRUE caso a operacao seja bem sucedida, FALSE caso nao.
     */
    public boolean delete(int id) throws Exception{
        boolean deletado = false;

        long end = indice_direto.read(id);
        if(end >= 0){
            arq.seek(end); // move ponteiro para o endereco do registro a ser removido

            if(arq.readChar() == ' '){ // valida existencia do registro
                // Ler o registro (p/ obter a chave secundaria):
                byte[] dados = new byte[arq.readInt()];
                arq.read(dados);

                // Realizar remocao propriamente dita:
                arq.seek(end);
                arq.writeChar('*'); // marca o campo lapide

                // Excluir a entrada nos indices:
                indice_indireto.delete( constructor.newInstance(dados).chaveSecundaria() ); // excluir entrada no indice indireto
                indice_direto.delete(id); // excluir entrada no indice direto

                deletado = true;
            }
        }

        return deletado;
    }
}