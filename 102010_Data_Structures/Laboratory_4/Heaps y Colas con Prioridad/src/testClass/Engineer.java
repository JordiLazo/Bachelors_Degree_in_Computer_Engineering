package testClass;

public class Engineer extends Employe {
    String type;
    private final int priorityEngenierPonderation = 20;
    public Engineer(String name, int dni, String type, int id){
        super(name, dni);
        this.type = type;
        setId(id * priorityEngenierPonderation);
    }
}
