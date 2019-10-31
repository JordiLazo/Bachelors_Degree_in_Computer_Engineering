package Jerarquia;
import java.lang.Comparable;

public class Employee implements Comparable<Employee>{

    public String name;
    public int dni;
    public int age;


    public Employee(String name, int dni, int age){
        this.name = name;
        this.dni = dni;
        this.age = age;
    }

    private boolean equals(Employee e1, Employee e2){
        return e1.name.equals(e2.name) && e1.dni == e2.dni && e1.age == e2.age;
    }


    @Override
    public int compareTo(Employee o) {
        Employee employee = (Employee)o;
        if(equals(this, employee)){
            return 0;
        }else if(dni < employee.dni){
            return -1;
        }
        return 1;
    }
}
