package Jerarquia;
public class Doctor extends Employee {
    String hospitalName;

    public Doctor(String name, int dni, int age, String hospitalName){
        super(name, dni, age);
        this.hospitalName = hospitalName;
    }
}
