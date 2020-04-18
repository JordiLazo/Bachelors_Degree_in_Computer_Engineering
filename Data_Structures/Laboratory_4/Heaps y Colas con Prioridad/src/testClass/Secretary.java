package testClass;

public class Secretary extends Employe {
    private final int prioritySecretaryPonderation = 2;
    public Secretary(String name , int dni, int id){
        super(name, dni);
        setId(id * prioritySecretaryPonderation);
    }
}
