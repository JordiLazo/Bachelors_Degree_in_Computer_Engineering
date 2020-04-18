package sort;
/**
 **Practica: Laboratorio1
 *
 **Autores: Jordi Lazo Florensa, Alejandro Clavera Poza
 */
public class IntArraySorter {
    private final int[] array;

    private int numComparisons;
    private int numSwaps;

    public int getNumComparisons() {
        return numComparisons;
    }

    public int getNumSwaps() {
        return numSwaps;
    }

    public IntArraySorter(int[] array) {
        this.array = array;
    }

    /*
        No utilizamos esta función porque queremos ver como se comportan
        los siguientes algoritmos cuando  se intenta ordenar un array ya ordenado.
     */
    public boolean isSorted() {
        for (int i = 0; i < array.length-1; i++) {
            numComparisons += 1;
            if (array[i] > array[i + 1]) {
                return false;
            }
        }
        return true;
    }

    public void swap(int i, int j) {
        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
        numSwaps += 1;
    }

    public boolean lessThanOrEqual(int num1, int num2) {
        numComparisons += 1;
        return num1 <= num2;
    }
    /*
      Añadimos esta comprobación de menor estricto que nos facilitara
      la realización del quiksort.
     */
    public boolean lessThan(int num1, int num2) {
        numComparisons += 1;
        return num1 < num2;
    }


    /*{ BUBBLESORT }*/
    /*
        Este algoritmo para ordenar el array va moviendo un elemento
        desde el principio hasta que sencuentra un elemento mayor que él.
        Repitendo esta acción en ese elemento asi hasta colocar uno
        de los elementos en su posción correcta. Una vez realizada esta
        acción se repetira con un elemento menos ya que estara ordenado.
        Esto repetira tantas veces como se necesite, hasta que el array
        este completamente ordenado.
     */

    public void bubbleSort() {
        /*
            Se repetira N-1 veces ya el ultimo elemento estara
            ordenado si el resto ya lo estan
        */
            for(int i = 0; i < array.length - 1; i++){
                for(int j = array.length -1; j > i; j--){
                    //Comparamos si elemento anterior es mayor o igual que este.
                    if(lessThanOrEqual(array[j], array[j - 1])){
                        swap(j, j - 1);
                    }
                }
            }

    }

    /*{ SELECTIONSORT }*/
    /*
        Este algoritmo ordena el array mediante la seleccion de la posición
        no ordenada y la busqueda a partir de ella del elemento mas pequeño,
        una vez encontrado lo coloca en dicha posición. Realizando este proceso
        tantas veces como sea necesario para ordenar dicho conjunto numerico
        no ordenado.

     */

    public void selectionSort() {
         /*
        Se repetira N-1 veces ya el ultimo elemento estara
        ordenado si el resto ya lo estan
        */
        for(int i = 0; i < array.length - 1; i++){
            int lowerPos = i;
            for(int j = i + 1; j < array.length; j++){
                /*Busqueda del elemento mas pequeño a partir del indice
                  de la posición no ordenada.
                 */
                if(lessThanOrEqual(array[j], array[lowerPos])){
                    lowerPos = j;
                }
            }
            /*
              Intercambio de elementos entre el elemento de la posición
              no ordenada y el elemento mas pequeño encontrado, siempre
              que sean diferentes.
             */
            if(i != lowerPos){
                swap(i, lowerPos);
            }
        }

    }

    /*{ QUIKSORT }*/
    /*
        La ordenación de un array con el argoritmo quiksort se realiza mendiante
        la realzación de particiones donde se selecciona un pivote(Aqui es donde reside
        el mayor problema del algoritmo) y se colocan todos los elementos menores a el,
        se moveran a las posiciones anteriores y los elementos mayores en las posiciones
        posteriores a el, logrando que el elemento pivote este ordenado. Tras ello se
        realizara lo mismo en las particione de la izquierda y derecha del pivote.
        El concepto de la partición es lo hace que el algoritmo quiksort sea el mas efectivo
        para la ordenación de conjuntos numericos, ya que cada vez se trabaja con partes mas
        pequeñas de el conjunto.
     */

    public void quickSort(){
        int[] limits = new int[array.length];//Guarda los limites de las particiones a realizar
        int i = 0;
        if(limits.length > 1){//Si es un array de 0,1 posiciones ya esta ordenado
            limits[i++] = 0;
            limits[i] = array.length -1;

            while(i >= 0){//Se ejecutra mientras queden particiones por ordenar.

                //Recuperación de los limites inferior y superior de la partición a ordenar
                int highLimit = limits[i--];
                int lowLimit  = limits[i--];

                //Realizamos la partición obteniendo, asi, la posición del elemento ya ordenado.
                int sortedElement = partition(lowLimit, highLimit);

                //Comprobamos en que lados del elemento ordenado se puede realizar una partición.
                if(sortedElement + 1 < highLimit){
                    limits[++i] = sortedElement + 1;
                    limits[++i] = highLimit;
                }

                if(sortedElement - 1  > lowLimit){
                    limits[++i] = lowLimit;
                    limits[++i] = sortedElement - 1;
                }

            }
        }
    }

   int partition(int initPos, int endPos) {
        //Posicionamos el pivote en el centro de la partición
        int pivotPos = initPos + (endPos - initPos) / 2;
        int pivot = array[pivotPos];
        while (initPos < endPos) {
            //Saltamos los elementos menores al elemento pivote
            while (lessThan(array[initPos], pivot)) {
                initPos++;
            }
            //Saltamos los elementos mayores al elemento pivote
            while (lessThan(pivot, array[endPos])) {
                endPos--;
            }
            //Comprobamos si los punteros han coincidido
            if (initPos >= endPos) {
                /*
                    Si los punteros han coincidido, comprobamos si que el
                    pivote no es menor que el elemento que al que apuntan los
                    punteros, si lo son ambos se intercambian.
                 */
                if (lessThan(pivot, array[endPos])) {
                    swap(endPos, pivotPos);
                }
                return endPos;
            }
            //Situamos los elementos en su lado correspondiente de la partición
            swap(initPos, endPos);
        }

        return pivotPos;
    }

    /*{ INSERTION SORT  }*/
   /*
    Este algoritmo ordena el array, compara todos los elementos de la
    posicionesde los elementos insertados con el elemento no insertado
    mas cercano a ellas, insertendo este en la posición anterior al
    elemento mayor que el. Asi hasta que se termine de ordenar todos
    los elementos del array.
    */
    public void insertionSort(){
         /*
        Se repetira N-1 veces ya el ultimo elemento estara
        ordenado si el resto ya lo estan
        */
        for(int s = 1; s < array.length; s++){
            int insert = s;
            for(int i = s - 1; i >= 0; i--){
                //Compara el elemento no insertado con los insertados
                if(!lessThanOrEqual(array[i], array[insert])){
                    swap(i, insert);
                    insert = i; //Cambia la posición del ultimo insertado.
                }else {
                    break;
                }
            }
        }
    }
}
