package Jerarquia;

public class Surgeon extends Doctor {
    int numSurgeries;
    public Surgeon(String name, int dni, int age, String hospitalName, int numSurgeries){
        super(name, dni, age, hospitalName);
        this.numSurgeries = numSurgeries;
    }
}
