package Tarea_3;
/**
 ** Practica: Laboratorio1
 *
 ** Autores: Jordi Lazo Florensa, Alejandro Clavera Poza
 */
import Jerarquia.Employee;

import java.util.Comparator;

public class EmployeeComparator implements Comparator<Employee>{

    @Override
    public int compare(Employee e1, Employee e2) {
        if(e1.name.equals(e2.name) && e1.dni == e2.dni && e1.age == e2.age){
            return 0;
        }else if(e1.dni < e2.dni){
            return -1;
        }else {
            return 1;
        }
    }
}
