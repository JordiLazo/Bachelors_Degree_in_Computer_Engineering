package domain;

import utils.PackUtils;

public class Product {

    public static final int DESCRIPTION_LIMIT = 20;
    public static final int SIZE = 2*DESCRIPTION_LIMIT+8+4+4; // TODO: Compute real size

    private final long id;
    private final String description;
    private final int price;
    private int stock;


    /**
     * Inicializamos los atributos de la clase Product
     * @param id
     * @param description
     * @param price
     * @param stock
     */
    public Product(long id, String description, int price, int stock) {
        this.id=id;
        this.description=description;
        this.price=price;
        this.stock=stock;
    }

    /**
     * Devuelve el ID del producto
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     * Devuelve la descripcion del product
     * @return
     */
    public String getDescription() {
        return description;
    }
    /**
     * Devuelve del precio del producto
     * @return
     */
    public int getPrice() {
        return price;
    }

    /**
     * Devuelve el stock del producto
     * @return
     */
    public int getStock() {
        return stock;
    }

    /**
     * Incrementa el stock del producto en 1
     */
    public void incrementStock() {
        stock+=1;
    }

    /**
     * Decrementa el stock del producto siempre que sea superior a 0
     */
    public void decrementStock() {
        if(this.stock>0){
            stock=stock-1;
        }
    }

    /**
     * Empaqueta los bytes de la instancia de Product
     * @return
     */
    public byte[] toBytes() {
        byte[] record= new byte [Product.SIZE];
        int offset=0;
        PackUtils.packLong(this.id,record,offset);
        offset+=8;
        PackUtils.packLimitedString(this.description,Product.DESCRIPTION_LIMIT,record,offset);
        offset+=2*Product.DESCRIPTION_LIMIT;
        PackUtils.packInt(this.price,record,offset);
        offset+=4;
        PackUtils.packInt(this.stock,record,offset);
        return record;
    }

    /**
     * Desempaqueta los bytes de la instancia Product
     * @param record
     * @return
     */
    public static Product fromBytes(byte[] record) {

        int offset=0;
        long id=PackUtils.unpackLong(record,offset);
        offset+=8;
        String description=PackUtils.unpackLimitedString(DESCRIPTION_LIMIT,record,offset);
        offset+= 2*DESCRIPTION_LIMIT;
        int price=PackUtils.unpackInt(record,offset);
        offset+=4;
        int stock=PackUtils.unpackInt(record,offset);
        return new Product(id,description,price,stock);
    }

    public boolean isEqualTo(Product other) {
        return id == other.id
                && description.equals(other.description)
                && price == other.price
                && stock == other.stock;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                '}';
    }
}
