package Tarea_2;


import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PartitionerTest {

    //TEST VERSION COMPARABLE

    @Test
    void partitionString(){
        ArrayList<String> list = new ArrayList<>(List.of("f","t", "e", "b","z","a"));
        PairOfLists<String> pair = Partitioner.partition(list, "e");
        String[] part1 = new String[pair.list1.size()];
        String[] part2 = new String[pair.list2.size()];

        for(int i = 0; i < part1.length; i++) part1[i] = pair.list1.get(i);
        for(int i = 0; i < part2.length; i++) part2[i] = pair.list2.get(i);

        assertArrayEquals(new String[]{"e","b","a"}, part1);
        assertArrayEquals(new String[]{"f","t", "z"}, part2);
    }

    @Test
    void partitionString_void() {
        ArrayList<String> list = new ArrayList<>();
        PairOfLists<String> pair = Partitioner.partition(list, "hola");
        String[] part1 = new String[pair.list1.size()];
        String[] part2 = new String[pair.list2.size()];

        assertEquals(0,pair.list1.size());
        assertEquals(0,pair.list2.size());
    }

    //TEST VERSION COMPARATOR

    @Test
    void partitionString_comparator(){
        ArrayList<String> list = new ArrayList<>(List.of("hola","adios", "universidad", "coche","patata","melon"));
        Comparate <String> comparator = new Comparate<>();
        PairOfLists<String> pair = Partitioner.partition(list, "ciudad",comparator);
        String[] part1 = new String[pair.list1.size()];
        String[] part2 = new String[pair.list2.size()];

        for(int i = 0; i < part1.length; i++) part1[i] = pair.list1.get(i);
        for(int i = 0; i < part2.length; i++) part2[i] = pair.list2.get(i);

        assertArrayEquals(new String[]{"adios"}, part1);
        assertArrayEquals(new String[]{"hola","universidad", "coche","patata","melon"}, part2);
    }

    @Test
    void partitionString_void_comparator() {
        ArrayList<String> list = new ArrayList<>();
        Comparate <String> comparator = new Comparate<>();
        PairOfLists<String> pair = Partitioner.partition(list,"h",comparator);
        String[] part1 = new String[pair.list1.size()];
        String[] part2 = new String[pair.list2.size()];

        assertEquals(0,pair.list1.size());
        assertEquals(0,pair.list2.size());
    }
}

