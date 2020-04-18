package domain;

import utils.PackUtils;

import java.util.Arrays;


public class Client {

    private static final int MAX_PRODUCTS = 3;
    public static final int NAME_LIMIT = 10;
    public static final int SIZE = 2*NAME_LIMIT+8+4+MAX_PRODUCTS*(8+4); // TODO: Compute real size

    private final long id;
    private final String name;
    private int balance;
    private long  id_product [] = new long[MAX_PRODUCTS];
    private int stock_product [] = new int [MAX_PRODUCTS];

    /**
     * Inicializamos los atributos de la clase Client
     * @param id
     * @param name
     * @param balance
     */
    public Client(long id, String name, int balance) {
        this.id=id;
        this.name=name;
        this.balance=balance;
    }

    /**
     * Retorma ID del cliente
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     * Retorna nombre del cliente
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Retorna la cantidad dinero del cliente
     * @return
     */
    public int getBalance() {
        return balance;
    }

    /**
     * Incrementa el saldo del client
     * @param amount
     */
    public void addBalance(int amount) {
        if(amount>0){
            balance+=amount;
        }
    }

    /**
     * Resta el balance del cliente
     * @param amount
     */
    public void subBalance(int amount){
        if(this.balance>=amount && amount>0){
            balance=balance-amount;
        }
    }

    /**
     * Comprueba si el cliente puede añadir un nuevo producto
     * @param idProduct
     * @return
     */
    public boolean canAddProduct(long idProduct) {
        if (hasProduct(idProduct) || getProductIds().length < 3) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Comprueba si el cliente tiene algun producto con el identificador
     * @param idProduct
     * @return
     */

    public boolean hasProduct(long idProduct) {
        for(int i=0;i<MAX_PRODUCTS;i++){
            if(idProduct==id_product[i]){
                return true;
            }
        }
        return false;
    }

    /**
     * Añade, si puede, una nueva unidad del producto en el cliente
     * @param idProduct
     */
    public void addProduct(long idProduct) {
            boolean isAdd = false;
            for (int i = 0; i < MAX_PRODUCTS && !isAdd; i++) {
                if (id_product[i] == idProduct) {
                    stock_product[i] += 1;
                    isAdd=true;
                }else if(id_product[i]==0){
                    id_product[i]=idProduct;
                    stock_product[i]+=1;
                    isAdd =true;
                }
            }

    }

    /**
     * Elimina 1 unidad del identificador del producto
     * @param idProduct
     */
    public void removeProduct(long idProduct) {
        for(int i=0;i<MAX_PRODUCTS;i++){

            if(id_product[i]==idProduct){
                if(stock_product[i]==1){
                    id_product[i]=0;
                    stock_product[i]=0;
                }else {
                    stock_product[i]-=1;
                }
            }
        }
    }

    /**
     * Devuelve el stock de unidades de producte de un cliente
     * @param idProduct
     * @return
     */

    public int getProductStock(long idProduct) {
        for(int i=0;i<MAX_PRODUCTS;i++){
            if(id_product[i]==idProduct){
                return stock_product[i];
            }
        }
        return 0;
    }

    /**
     * Devuelve un array con los productos de un cliente
     * @return
     */
    public long[] getProductIds() {
        long products_client[] = new long[0];
        long aux[] = new long[0];
        for (int i = 0; i < MAX_PRODUCTS; i++) {
            if (id_product[i] != 0) {
                aux=products_client;
                products_client=new long[products_client.length+1];
                for (int j = 0; j < aux.length; j++) {
                    products_client[j]=aux[j];
                }
                products_client[aux.length]=id_product[i];
            }
        }
        return products_client;
    }

    /**
     * Empaqueta los bytes de la instancia Client
     * @return
     */
    public byte[] toBytes() {

        byte [] record= new byte[SIZE];
        int offset=0;
        PackUtils.packLong(this.id,record,offset);
        offset+=8;
        PackUtils.packLimitedString(this.name,NAME_LIMIT,record,offset);
        offset+=2*NAME_LIMIT;
        PackUtils.packInt(this.balance,record,offset);
        offset+=4;
        for(int i=0;i<id_product.length;i++){
            PackUtils.packLong(this.id_product[i],record,offset);
            offset+=8;
            PackUtils.packInt(this.stock_product[i],record,offset);
            offset+=4;

        }
        return record;
    }

    /**
     * Desempaqueta los bytes de la instancia Client
     * @param record
     * @return
     */
    public static Client fromBytes(byte[] record) {
        int offset=0;
        long id=PackUtils.unpackLong(record,offset);
        offset+=8;
        String name =PackUtils.unpackLimitedString(NAME_LIMIT,record,offset);
        offset+=2*NAME_LIMIT;
        int balance= PackUtils.unpackInt(record,offset);
        offset+=4;

        Client client= new Client(id,name,balance);
        for(int i=0;i<MAX_PRODUCTS;i++){
            client.id_product[i]= PackUtils.unpackLong(record,offset);
            offset+=8;
            client.stock_product[i]= PackUtils.unpackInt(record,offset);
            offset+=4;
        }
        return client;
    }

    public boolean isEqualTo(Client other) {
        if (id != other.id
                || !name.equals(other.name)
                || balance != other.balance) {
            return false;
        }
        long[] myProductIds = getProductIds();
        long[] theirProductIds = other.getProductIds();
        Arrays.sort(myProductIds);
        Arrays.sort(theirProductIds);
        int[] myStocks = getStocks(myProductIds);
        int[] theirStocks = getStocks(theirProductIds);
        return Arrays.equals(myProductIds, theirProductIds)
                && Arrays.equals(myStocks, theirStocks);
    }

    @Override
    public String toString() {
        long[] productIds = getProductIds();
        Arrays.sort(productIds);
        int[] stocks = getStocks(productIds);
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                ", productIds=" + Arrays.toString(productIds) +
                ", stocks=" + Arrays.toString(stocks) +
                '}';
    }

    private int[] getStocks(long[] productIds) {
        int[] stocks = new int[productIds.length];
        for (int i = 0; i < stocks.length; i++) {
            stocks[i] = this.getProductStock(productIds[i]);
        }
        return stocks;
    }
}
