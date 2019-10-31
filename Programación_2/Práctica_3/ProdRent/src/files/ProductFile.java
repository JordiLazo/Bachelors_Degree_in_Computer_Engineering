package files;

import domain.Product;

import java.io.IOException;
import java.io.RandomAccessFile;

public class ProductFile {
    private final RandomAccessFile products;

    /**
     * Crea un nuevo fichero para poder realizar las operaciones de Read and Write
     * @param fileName
     * @throws IOException
     */
    public ProductFile(String fileName) throws IOException {
        products= new RandomAccessFile(fileName,"rw");
    }

    /**
     * Escribe los datos del array en el array record
     * @param product
     * @throws IOException
     */
    public void write(Product product) throws IOException {
        byte [] record= product.toBytes();
        long pos = (product.getId()-1)*Product.SIZE;
        products.seek(pos);
        products.write(record);
    }

    /**
     * Devuelve el producto de la posicion id
     * @param id
     * @return
     * @throws IOException
     */
    public Product read(long id) throws IOException {
        byte [] record= new byte[Product.SIZE];
        long pos = (id-1)*Product.SIZE;
        products.seek(pos);
        products.read(record);
        return Product.fromBytes(record);
    }

    /**
     * Devuelve el siguiente producto para añadir
     * @return
     * @throws IOException
     */
    public long nextId() throws IOException {
        return products.length()/Product.SIZE+1;
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
     * Resetea el fichero del cliente, lo elimina dejando a 0
     * @throws IOException
     */
    public void reset() throws IOException {
        products.setLength(0);
    }

    /**
     * Cierra el fichero de los productos
     * @throws IOException
     */
    public void close() throws IOException {
        products.close();
    }
}
