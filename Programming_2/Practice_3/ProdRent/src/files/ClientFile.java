package files;

import domain.Client;
import domain.Product;

import java.io.IOException;
import java.io.RandomAccessFile;

public class ClientFile {
    private final RandomAccessFile clients;

    /**
     * Crea un nuevo fichero para poder realizar las operaciones de Read and Write
     * @param fileName
     * @throws IOException
     */
    public ClientFile(String fileName) throws IOException {
        clients=new RandomAccessFile(fileName,"rw");
    }

    /**
     * Escribe los datos del cliente en el array record
     * @param client
     * @throws IOException
     */

    public void write(Client client) throws IOException {
        byte [] record= client.toBytes();
        long pos = (client.getId()-1)*Client.SIZE;
        clients.seek(pos);
        clients.write(record);
    }

    /**
     * Devuelve el cliente de la posicion id
     * @param id
     * @return
     * @throws IOException
     */

    public Client read(long id) throws IOException {
        byte [] record= new byte[Client.SIZE];
        long pos = (id-1)*Client.SIZE;
        clients.seek(pos);
        clients.read(record);
        return Client.fromBytes(record);
    }

    /**
     * Devuelve el siguiente cliente para añadir
     * @return
     * @throws IOException
     */

    public long nextId() throws IOException {
         return clients.length()/Client.SIZE+1;
    }

    /**
     * Compruba si el valor id esta entre 1L y el numero de registros del fichero
     * @param id
     * @return
     * @throws IOException
     */
    public boolean isValid(long id) throws IOException {
        if(id>=1L && id<nextId()){
            return true;
        }
        return false;
    }

    /**
     * Resetea el fichero de un cliente, lo elimina dejando a 0
     * @throws IOException
     */
    public void reset() throws IOException {
        clients.setLength(0);
    }

    /**
     * Cierra el fichero de los clientes
     * @throws IOException
     */
    public void close() throws IOException {
        clients.close();
    }
}
