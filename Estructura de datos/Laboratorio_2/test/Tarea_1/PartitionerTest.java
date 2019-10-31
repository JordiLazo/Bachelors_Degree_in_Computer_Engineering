package Tarea_1;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PartitionerTest {

    @Test
    void partition() {
        ArrayList<Integer> list = new ArrayList<>(List.of(9,89,1,50,6,77,999,2,68, 9999));
        PairOfLists pair = Partitioner.partition(list, 50);

        int[] part1 = new int[pair.list1.size()];
        int[] part2 = new int[pair.list2.size()];

        for(int i = 0; i < part1.length; i++) part1[i] = pair.list1.get(i);
        for(int i = 0; i < part2.length; i++) part2[i] = pair.list2.get(i);

        assertArrayEquals(new int[]{9,1,50,6,2}, part1);
        assertArrayEquals(new int[]{89,77,999,68,9999}, part2);
    }

    @Test
    void partitioner_void_list() {
        ArrayList<Integer> list = new ArrayList<>(List.of());
        PairOfLists pair = Partitioner.partition(list,50);
        assertEquals(0, pair.list1.size());
        assertEquals(0,pair.list2.size());
    }
}