import java.io.IOException;

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