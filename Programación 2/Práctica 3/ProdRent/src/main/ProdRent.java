package main;

import acm.program.CommandLineProgram;
import domain.Client;
import domain.Product;
import files.ClientFile;
import files.LogFile;
import files.ProductFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class ProdRent extends CommandLineProgram {

    private static final String PRODUCTS = "productsDB.dat";
    private static final String CLIENTS = "clientsDB.dat";
    private String movements;
    private String logger;

    private BufferedReader movementsFile;
    private LogFile logFile;
    private ProductFile productsDB;
    private ClientFile clientsDB;


    public static void main(String[] args) {
        new ProdRent().start(args);
    }

    public void run() {
        try {
            askFileNames();
            openFiles();
            resetFiles();
            processMovements();
        } catch (IOException ex) {
            println("ERROR");
            ex.printStackTrace();
        } finally {
            try {
                closeFiles();
            } catch (IOException ex) {
                println("ERROR Closing");
                ex.printStackTrace();
            }
        }
    }

    private void askFileNames() {
        movements = readLine("Movements file name: ");
        logger = readLine("Logging file name: ");
    }

    private void openFiles() throws IOException {
        movementsFile = new BufferedReader(new FileReader(movements));
        logFile = new LogFile(logger);
        productsDB = new ProductFile(PRODUCTS);
        clientsDB = new ClientFile(CLIENTS);
    }

    private void closeFiles() throws IOException {
        movementsFile.close();
        logFile.close();
        productsDB.close();
        clientsDB.close();
    }

    private void resetFiles() throws IOException {
        productsDB.reset();
        clientsDB.reset();
    }

    private void processMovements() throws IOException {
        String movements = read();
        StringTokenizer movement = new StringTokenizer(movements, "\n");
        while (movement.hasMoreTokens()) {
            process_movements(movement.nextToken());
        }

    }

    private String read() throws IOException {
        String movements = "";
        String movements_line = movementsFile.readLine();
        while (movements_line != null) {
            movements += movements_line+"\n";
            movements_line = movementsFile.readLine();

        }

        return movements;
    }

    private void process_movements(String movementLine) throws IOException {

        StringTokenizer movement = new StringTokenizer(movementLine, ",");
        String movementName = movement.nextToken();

        if (movementName.equals("ALTA_PRODUCTO")) {
            registerProduct(movement);

        } else if (movementName.equals("ALTA_CLIENTE")) {
            registerClient(movement);

        } else if (movementName.equals("INFO_PRODUCTO")) {
            infoProduct(movement);

        } else if (movementName.equals("INFO_CLIENTE")) {
            infoClient(movement);

        } else if (movementName.equals("ALQUILAR")) {
            rent(movement);

        } else if (movementName.equals("DEVOLVER")) {
            returnProduct(movement);

        } else {
            logFile.unknownOperation(movementName);
        }
    }

    public void registerProduct(StringTokenizer product) throws IOException{
        String descriptionProduct=product.nextToken();
        int priceProduct = Integer.parseInt(product.nextToken());
        int stockProduct = Integer.parseInt(product.nextToken());
        long idProduct = productsDB.nextId();

        if(priceProduct>0 && stockProduct>0){
            Product newProduct = new Product(idProduct,descriptionProduct,priceProduct,stockProduct);
            productsDB.write(newProduct);
            logFile.okNewProduct(newProduct);
        }

        if(priceProduct<=0){
            logFile.errorPriceCannotBeNegativeOrZero(descriptionProduct,priceProduct);
        }

        if(stockProduct<=0){
            logFile.errorStockCannotBeNegativeOrZero(descriptionProduct,priceProduct);
        }
    }
    public void registerClient (StringTokenizer client) throws IOException{
        String nameClient=client.nextToken();
        int balanceClient = Integer.parseInt(client.nextToken());
        long idClient = clientsDB.nextId();

        if(balanceClient<=0){
            logFile.errorBalanceCannotBeNegativeOrZero(nameClient,balanceClient);
        }else {
            Client newClient = new Client(idClient,nameClient,balanceClient);
            clientsDB.write(newClient);
            logFile.okNewClient(newClient);
        }
    }

    public void infoProduct (StringTokenizer product) throws IOException{
        long idProduct = Long.parseLong(product.nextToken());

        if(productsDB.isValid(idProduct)){
            Product productRead = productsDB.read(idProduct);
            logFile.infoProduct(productRead);
        }else {
            logFile.errorInvalidClientId(idProduct);
        }

    }

    public void infoClient (StringTokenizer client) throws IOException{
        long idClient = Long.parseLong(client.nextToken());

        if(clientsDB.isValid(idClient)){

            Client clientRead = clientsDB.read(idClient);
            long [] idProducts = clientRead.getProductIds();
            Product [] products = new Product[idProducts.length];

            for(int i=0;i<products.length;i++){
                products[i]= productsDB.read(idProducts[i]);
            }
            logFile.infoClient(clientRead,products);
        }else {
            logFile.errorInvalidClientId(idClient);
        }

    }

    public void rent (StringTokenizer rent ) throws IOException {
        boolean validRent = true;
        long idClient = Long.parseLong(rent.nextToken());
        long idProduct = Long.parseLong(rent.nextToken());
        if(isValid(idClient,idProduct)){
            Client client = clientsDB.read(idClient);
            Product product = productsDB.read(idProduct);

            if(product.getStock()==0){
                logFile.errorCannotRentProductWithNoStock(product);
                validRent=false;
            }

            if(client.getBalance()<product.getPrice()){
                logFile.errorClientHasNotEnoughFundsToRentProduct(client,product);
                validRent=false;
            }
            if(!client.canAddProduct(idProduct)){
                logFile.errorClientCannotAddProduct(client,product);
                validRent=false;
            }
            if(validRent){
                client.addProduct(idProduct);
                client.subBalance(product.getPrice());
                product.decrementStock();
                clientsDB.write(client);
                productsDB.write(product);
                logFile.okRent(client,product);
            }
        }
    }

    public void returnProduct(StringTokenizer returnProduct) throws IOException{
        long idClient = Long.parseLong(returnProduct.nextToken());
        long idProduct = Long.parseLong(returnProduct.nextToken());
        if(isValid(idClient,idProduct)){
            Client client = clientsDB.read(idClient);
            Product product = productsDB.read(idProduct);

            if(!client.hasProduct(product.getId())){
                logFile.errorClientHasNotProduct(client,product.getId());
            }else {
                client.removeProduct(product.getId());
                product.incrementStock();
                clientsDB.write(client);
                productsDB.write(product);
                logFile.okReturn(client,product);
            }
        }
    }

    private boolean isValid (long idClient, long idProduct) throws IOException{
        boolean validRent =true;
        if (!clientsDB.isValid(idClient)) {
            logFile.errorInvalidClientId(idClient);
            validRent = false;
        }
        if (!productsDB.isValid(idProduct)) {
            logFile.errorInvalidProductId(idProduct);
            validRent = false;
        }
        return validRent;
    }


}
