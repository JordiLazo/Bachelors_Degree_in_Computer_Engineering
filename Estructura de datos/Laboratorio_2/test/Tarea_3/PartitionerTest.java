package Tarea_3;

import Jerarquia.*;
import Tarea_3.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PartitionerTest {

    //TEST COMPARABLE

    @Test
    void copyPartitionComparable() {
        ArrayList<Engineer> list = new ArrayList<>(List.of(new Engineer("Juan",4788,21), new InformaticEngineer("Pepe",5000,27), new Engineer("Tom",1022,21)));
        ArrayList<Employee> l1 = new ArrayList<>();
        ArrayList<Employee>l2 = new ArrayList<>();

        Partitioner.copyPartition(list, new Engineer("Juan", 4788,21), l1, l2);
        Employee[] part1 = new Employee[]{new Engineer("Juan",4788,21), new Engineer("Tom",1022,21)};
        Employee[] part2 = new Employee[]{new InformaticEngineer("Pepe",5000,27)};

        for(int i = 0; i < part1.length; i++) assertEquals(0,l1.get(i).compareTo(part1[i]));
        for(int i = 0; i < part2.length; i++) assertEquals(0,l2.get(i).compareTo(part2[i]));

    }

    @Test
    void copyPartitionComparableVoidList() {
        ArrayList<Engineer> list = new ArrayList<>();
        ArrayList<Employee> l1 = new ArrayList<>();
        ArrayList<Employee>l2 = new ArrayList<>();

        Partitioner.copyPartition(list, new Engineer("Juan", 4788,21), l1, l2);
        assertEquals(0, l1.size());
        assertEquals(0, l2.size());

    }

    //TEST COMPARATOR

    @Test
    void CopyPartitionComparator(){
        ArrayList<Doctor> list = new ArrayList<>(List.of(new Doctor("Juan",4788,21,"SN"), new Doctor("Ignacio",10322,21,"MZ"), new Surgeon("Pepe",5000,27,"BRB", 23)));
        ArrayList<Employee> l1 = new ArrayList<>();
        ArrayList<Employee>l2 = new ArrayList<>();

        EmployeeComparator employeeComparator = new EmployeeComparator();
        Partitioner.copyPartition(list, new Surgeon("Pepe",5000,27,"BRB", 23), l1, l2, employeeComparator);
        Employee[] part1 = new Employee[]{new Doctor("Juan",4788,21,"SN") , new Surgeon("Pepe",5000,27,"BRB", 23)};
        Employee[] part2 = new Employee[]{new Doctor("Ignacio",10322,21,"MZ")};

        for(int i = 0; i < part1.length; i++) assertEquals(0,l1.get(i).compareTo(part1[i]));
        for(int i = 0; i < part2.length; i++) assertEquals(0,l2.get(i).compareTo(part2[i]));

    }

    @Test
    void CopyPartitionComparatorVoidList(){
        ArrayList<Doctor> list = new ArrayList<>();
        ArrayList<Employee> l1 = new ArrayList<>();
        ArrayList<Employee>l2 = new ArrayList<>();

        EmployeeComparator employeeComparator = new EmployeeComparator();
        Partitioner.copyPartition(list, new Surgeon("Pepe",5000,27,"BRB", 23), l1, l2, employeeComparator);
        assertEquals(0, l1.size());
        assertEquals(0, l2.size());
    }
}