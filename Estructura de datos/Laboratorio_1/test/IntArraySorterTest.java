import org.junit.jupiter.api.Test;
import sort.IntArraySorter;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class IntArraySorterTest {

    // TEST INSERTSORT
    @Test
    void insertSortedArray(){
        int[] array = generateSortArray(50);
        IntArraySorter sorter = new IntArraySorter(array);
        sorter.insertionSort();
        assertEquals(array, array);
        System.out.println("InsertSort Sorted Array");
        System.out.println("Comparaciones:"+ sorter.getNumComparisons());
        System.out.println("swap:"+ sorter.getNumSwaps());
        System.out.println("---");
    }


    @Test
    void insertSortInvertArray(){
        int[] array = generateSortArray(50);
        invertArray(array);
        IntArraySorter sorter = new IntArraySorter(array);
        sorter.insertionSort();
        assertArrayEquals(generateSortArray(50), array);
        System.out.println("InsertSort Inverted Array");
        System.out.println("Comparaciones:"+ sorter.getNumComparisons());
        System.out.println("swap:"+ sorter.getNumSwaps());
        System.out.println("---");
    }

    @Test
    void insertSortOfLongMostUnsortedArray(){
        int[] array = generateSortArray(50);
        IntArraySorter sorter = new IntArraySorter(array);
        for(int i =0; i<200; i++){
            fiserYates(array);
        }
        sorter.insertionSort();
        assertArrayEquals(generateSortArray(50), array);
        System.out.println("InsertSort UnSorted++ Array ");
        System.out.println("Comparaciones:"+ sorter.getNumComparisons());
        System.out.println("swap:"+ sorter.getNumSwaps());
        System.out.println("---");
    }

    @Test
    void insertSortOfLongUnsortedArray(){
        int[] array = generateSortArray(50);
        IntArraySorter sorter = new IntArraySorter(array);
        fiserYates(array);
        sorter.insertionSort();
        assertArrayEquals(generateSortArray(50), array);
        System.out.println("InsertSort UnSorted Array ");
        System.out.println("Comparaciones:"+ sorter.getNumComparisons());
        System.out.println("swap:"+ sorter.getNumSwaps());
        System.out.println("---");
    }

    // TEST BUBBLESORT
    @Test
    void bubbleSortedArray(){
        int[] array = generateSortArray(50);
        IntArraySorter sorter = new IntArraySorter(array);
        sorter.bubbleSort();
        assertEquals(array, array);
        System.out.println("BubbleSort Sorted Array ");
        System.out.println("Comparaciones:"+ sorter.getNumComparisons());
        System.out.println("swap:"+ sorter.getNumSwaps());
        System.out.println("---");
    }

    @Test
    void bubbleSortInvertArray(){
        int[] array = generateSortArray(50);
        invertArray(array);
        IntArraySorter sorter = new IntArraySorter(array);
        sorter.bubbleSort();
        assertArrayEquals(generateSortArray(50), array);
        System.out.println("BubbleSort Invert Array ");
        System.out.println("Comparaciones:"+ sorter.getNumComparisons());
        System.out.println("swap:"+ sorter.getNumSwaps());
        System.out.println("---");
    }

    @Test
    void bubbleSortOfLongMostUnsortedArray(){
        int[] array = generateSortArray(50);
        IntArraySorter sorter = new IntArraySorter(array);
        for(int i = 0; i < 200; i++){
            fiserYates(array);
        }
        sorter.bubbleSort();
        assertArrayEquals(generateSortArray(50), array);
        System.out.println("BubbleSort UnSorted++ Array ");
        System.out.println("Comparaciones:"+ sorter.getNumComparisons());
        System.out.println("swap:"+ sorter.getNumSwaps());
        System.out.println("---");
    }

    @Test
    void bubbleSortOfLongUnsortedArray(){
        int[] array = generateSortArray(50);
        fiserYates(array);
        IntArraySorter sorter = new IntArraySorter(array);
        sorter.bubbleSort();
        assertArrayEquals(generateSortArray(50), array);
        System.out.println("BubbleSort unSorted Array ");
        System.out.println("Comparaciones:"+ sorter.getNumComparisons());
        System.out.println("swap:"+ sorter.getNumSwaps());
        System.out.println("---");
    }

    // TEST SELECTIONSORT
    @Test
    void selectionSortedArray(){
        int[] array = generateSortArray(50);
        IntArraySorter sorter = new IntArraySorter(array);
        sorter.selectionSort();
        assertEquals(array, array);
        System.out.println("Selection Sorted Array");
        System.out.println("Comparaciones:"+ sorter.getNumComparisons());
        System.out.println("swap:"+ sorter.getNumSwaps());
        System.out.println("---");
    }

    @Test
    void selectionSortInvertArray(){
        int[] array = generateSortArray(50);
        invertArray(array);
        IntArraySorter sorter = new IntArraySorter(array);
        sorter.selectionSort();
        assertArrayEquals(generateSortArray(50), array);
        System.out.println("Selection Inverted Array");
        System.out.println("Comparaciones:"+ sorter.getNumComparisons());
        System.out.println("swap:"+ sorter.getNumSwaps());
        System.out.println("---");
    }

    @Test
    void selectionSortOfLongMostUnsortedArray(){
        int[] array = generateSortArray(50);
        IntArraySorter sorter = new IntArraySorter(array);
        for(int i = 0; i < 200; i++){
            fiserYates(array);
        }
        sorter.selectionSort();
        assertArrayEquals(generateSortArray(50), array);
        System.out.println("Selection UnSorted++ Array");
        System.out.println("Comparaciones:"+ sorter.getNumComparisons());
        System.out.println("swap:"+ sorter.getNumSwaps());
        System.out.println("---");
    }


    @Test
    void selectionSortOfLongUnSortedArray(){
        int[] array = generateSortArray(50);
        IntArraySorter sorter = new IntArraySorter(array);
        fiserYates(array);
        sorter.selectionSort();
        assertArrayEquals(generateSortArray(50), array);
        System.out.println("Selection UnSorted Array");
        System.out.println("Comparaciones:"+ sorter.getNumComparisons());
        System.out.println("swap:"+ sorter.getNumSwaps());
        System.out.println("---");
    }

    // TEST QUIKSORT
    @Test
    void quikSortedArray(){
        int[] array = generateSortArray(50);
        IntArraySorter sorter = new IntArraySorter(array);
        sorter.quickSort();
        assertArrayEquals(generateSortArray(50), array);
        System.out.println("Selection Sorted Array");
        System.out.println("Comparaciones:"+ sorter.getNumComparisons());
        System.out.println("swap:"+ sorter.getNumSwaps());
        System.out.println("---");
    }

    @Test
    void quikSortInvertArray(){
        int[] array = generateSortArray(50);
        invertArray(array);
        IntArraySorter sorter = new IntArraySorter(array);
        sorter.quickSort();
        assertArrayEquals(generateSortArray(50), array);
        System.out.println("Selection Inverted Array");
        System.out.println("Comparaciones:"+ sorter.getNumComparisons());
        System.out.println("swap:"+ sorter.getNumSwaps());
        System.out.println("---");
    }

    @Test
    void quikSortOfLongMostUnsortedArray(){
        int[] array = generateSortArray(50);
        IntArraySorter sorter = new IntArraySorter(array);
        for(int i = 0; i < 200; i++){
            fiserYates(array);
        }
        sorter.quickSort();
        assertArrayEquals(generateSortArray(50), array);
        System.out.println("Selection UnSorted++ Array");
        System.out.println("Comparaciones:"+ sorter.getNumComparisons());
        System.out.println("swap:"+ sorter.getNumSwaps());
        System.out.println("---");
    }


    @Test
    void quikSortOfLongArrayUnsorted(){
        int[] array = generateSortArray(50);
        IntArraySorter sorter = new IntArraySorter(array);
        fiserYates(array);
        sorter.quickSort();
        assertArrayEquals(generateSortArray(50), array);
        System.out.println("Selection UnSorted Array");
        System.out.println("Comparaciones:"+ sorter.getNumComparisons());
        System.out.println("swap:"+ sorter.getNumSwaps());
        System.out.println("---");
    }

    //FUNCIONES AUXILIRES
    int[] generateSortArray(int length){
        int[] array = new int[length];
        for(int i = 0; i < array.length; i++){
            array[i] = i + 1;
        }
        return array;
    }

    void invertArray(int[] array){
        for(int i = array.length - 1; i>=array.length/2; i--){
            int tmp = array[i];
            array[i] = array[(array.length - 1) - i];
            array[(array.length - 1) - i] = tmp;
        }
    }

    void fiserYates(int[] array){
        int az;
        int tmp;
        Random ramdom = new Random();
        for(int k = array.length - 1; k > 0; k--){
            ramdom.setSeed(k + array[k]);
            az = Math.abs(ramdom.nextInt()) % array.length;
            tmp = array[az];
            array[az] = array[k];
            array[k] = tmp;
        }
    }
}