/* ---------------------------------------------------------------
Práctica 2.
Código fuente: Statistics.java
Grau Informàtica
49259953W i Sergi Puigpinós Palau.
47694432E i Jordi Lazo Florensa.
--------------------------------------------------------------- */
package eps.udl.cat;

public class Statistics {

    private int numComb;
    private int numInvComb;
    private int numValidComb;
    private float avgCostValidComb;
    private float avgScoreValidComb;
    private JugadorsEquip bestCombination;
    private int bestScore;
    private JugadorsEquip worseCombination;
    private int worseScore;

    public void formatData() {
        this.numComb = 0;
        this.numInvComb = 0;
        this.numValidComb = 0;
        this.avgCostValidComb = 0;
        this.avgScoreValidComb = 0;
    }

    public void calculateStatistics(JugadorsEquip jugadors, int costEquip, int puntuacioEquip, int PresupostFitxatges) {
        avgCostValidComb = ((avgCostValidComb * numValidComb) + costEquip) / (numValidComb+1);
        avgScoreValidComb = ((avgScoreValidComb * numValidComb) + puntuacioEquip) / (numValidComb+1);
        numValidComb++;
        if (puntuacioEquip > bestScore && costEquip < PresupostFitxatges){    //Best combination
            bestScore = puntuacioEquip;
            bestCombination = jugadors;
        }else if (worseScore == 0 || puntuacioEquip < worseScore) {   //Worse combination
            worseScore = puntuacioEquip;
            worseCombination = jugadors;
        }
    }

    public void printStatistics(){
        threadMessenger.addMessageToQueue( Error.color_green + "*******THREAD " + Thread.currentThread().getId()+ " STATISTICS******"+
                "\nNúmero de Combinaciones evaluadas: " + numComb +
                "\nNúmero de combinaciones no válidas: " + numInvComb +
                "\nCoste promedio de las combinaciones válidas: " + avgCostValidComb +
                "\nPuntuación promedio de las combinaciones válidas: " + avgScoreValidComb +
                "\nMejor combinación (desde el punto de vista de la puntuación): " + (bestCombination == null ? " none" : (bestCombination.toStringEquipJugadors()) +
                "\n   Cost " + bestCombination.CostEquip() +", Points: " + bestCombination.PuntuacioEquip() + ".") +
                "\nPeor combinación (desde el punto de vista de la puntuación): " + (worseCombination == null ? " none" : worseCombination.toStringEquipJugadors() +
                "\n   Cost " + worseCombination.CostEquip() +", Points: " + worseCombination.PuntuacioEquip() + ".") +
                "\n********************************************************\n" + Error.end_color);
    }

    public void calculateGlobalStatistics(Statistics evaluatorStatistics){
        numComb += evaluatorStatistics.getNumComb();
        numInvComb += evaluatorStatistics.getNumInvComb();
        if (evaluatorStatistics.numValidComb != 0) {
            avgCostValidComb = ((avgCostValidComb * numValidComb) + (evaluatorStatistics.getAvgCostValidComb() * evaluatorStatistics.getNumValidComb())) / (numValidComb + evaluatorStatistics.getNumValidComb());
            avgScoreValidComb = ((avgScoreValidComb * numValidComb) + (evaluatorStatistics.getAvgScoreValidComb() * evaluatorStatistics.getNumValidComb())) / (numValidComb + evaluatorStatistics.getNumValidComb());
            numValidComb += evaluatorStatistics.getNumValidComb();
            if (bestScore == 0 || evaluatorStatistics.getBestScore() > bestScore) {    //Best combination regarding points
                bestScore = evaluatorStatistics.getBestScore();
                bestCombination = evaluatorStatistics.getBestCombination();
            }
            if (worseScore == 0 || (evaluatorStatistics.getWorseScore() < worseScore && evaluatorStatistics.getWorseScore() != 0)) {   //Worse combination regarding points
                worseScore = evaluatorStatistics.getWorseScore();
                worseCombination = evaluatorStatistics.getWorseCombination();
            }
        }
    }

    public void printGlobalStatistics(){
        threadMessenger.addMessageToQueue(Error.color_blue + "*******GLOBAL STATISTICS******"+
                "\nNúmero de Combinaciones evaluadas: " + numComb +
                "\nNúmero de combinaciones no válidas: " + numInvComb +
                "\nCoste promedio de las combinaciones válidas: " + avgCostValidComb +
                "\nPuntuación promedio de las combinaciones válidas: " + avgScoreValidComb +
                "\nMejor combinación (desde el punto de vista de la puntuación): " + (bestCombination == null ? "none" : bestCombination.toStringEquipJugadors() +
                "\n   Cost " + bestCombination.CostEquip() +", Points: " + bestCombination.PuntuacioEquip() + ".") +
                "\nPeor combinación (desde el punto de vista de la puntuación): " + (worseCombination == null ? "none" : worseCombination.toStringEquipJugadors() +
                "\n   Cost " + worseCombination.CostEquip() +", Points: " + worseCombination.PuntuacioEquip() + ".") +
                "\n********************************************************\n\n" + Error.end_color);
    }

    public int getNumComb() {
        return numComb;
    }

    public void setNumComb(int numComb) {
        this.numComb = numComb;
    }

    public int getNumInvComb() {
        return numInvComb;
    }

    public void setNumInvComb(int numInvComb) {
        this.numInvComb = numInvComb;
    }

    public int getNumValidComb() {
        return numValidComb;
    }

    public void setNumValidComb(int numValidComb) {
        this.numValidComb = numValidComb;
    }

    public float getAvgCostValidComb() {
        return avgCostValidComb;
    }

    public void setAvgCostValidComb(float avgCostValidComb) {
        this.avgCostValidComb = avgCostValidComb;
    }

    public float getAvgScoreValidComb() {
        return avgScoreValidComb;
    }

    public void setAvgScoreValidComb(float avgScoreValidComb) {
        this.avgScoreValidComb = avgScoreValidComb;
    }

    public JugadorsEquip getBestCombination() {
        return bestCombination;
    }

    public void setBestCombination(JugadorsEquip bestCombination) {
        this.bestCombination = bestCombination;
    }

    public int getBestScore() {
        return bestScore;
    }

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }

    public JugadorsEquip getWorseCombination() {
        return worseCombination;
    }

    public void setWorseCombination(JugadorsEquip worseCombination) {
        this.worseCombination = worseCombination;
    }

    public int getWorseScore() {
        return worseScore;
    }

    public void setWorseScore(int worseScore) {
        this.worseScore = worseScore;
    }

    public void incrementNumComb(){
        this.numComb++;
    }

    public void incrementNumInvComb(){
        this.numInvComb++;
    }

}
