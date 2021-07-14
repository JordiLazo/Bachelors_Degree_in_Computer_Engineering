/* ---------------------------------------------------------------
Práctica 2.
Código fuente: threadEvaluater.java
Grau Informàtica
49259953W i Sergi Puigpinós Palau.
47694432E i Jordi Lazo Florensa.
--------------------------------------------------------------- */
package eps.udl.cat;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class threadEvaluator extends Thread{

    private final int first;
    private final int end;
    private final int PresupostFitxatges;
    private final Market market;
    private final int M;
    private final int numOfThreads;

    //Shared variables
    public static JugadorsEquip MillorEquip = null;
    public static int MaxPuntuacio = -1;
    public static int threadsWaitingSummary = 0;
    public static int threadsFinished = 0;
    public static boolean finalPrint = false;

    //Statistics
    private final Statistics statistics = new Statistics();
    public static Statistics globalStatistics = new Statistics();

    //Locks
    public static final ReentrantLock evaluatorLock = new ReentrantLock();
    public static final Condition evaluatorFinished = evaluatorLock.newCondition();
    public static final Condition evaluatorsEnded = evaluatorLock.newCondition();
    public static final Condition evaluatorFinishedPrinting = evaluatorLock.newCondition();


    threadEvaluator(int first, int end, int PresupostFitxatges, Market market, int M, int numOfThreads){
        this.first = first;
        this.end = end;
        this.market = market;
        this.PresupostFitxatges = PresupostFitxatges;
        this.M = M;
        this.numOfThreads = numOfThreads;
    }

    @Override
    public void run(){
        int equip;
        for (equip=first;equip<=end;equip++)
        {
            JugadorsEquip jugadors;

            statistics.incrementNumComb();

            if (statistics.getNumComb()%M == 0)
                statisticsSummary();

            // Get playes from team number. Returns false if the team is not valid.
            if ((jugadors=market.ObtenirJugadorsEquip(new IdEquip(equip)))==null) {
                statistics.incrementNumInvComb();
                continue;
            }

            // Reject teams with repeated players.
            if (jugadors.JugadorsRepetits())
            {
                statistics.incrementNumInvComb();
                //threadMessenger.addMessageToQueue(Error.color_red +" Invalid." + Error.end_color);
                continue;	// Equip no valid.
            }
            int costEquip = jugadors.CostEquip();
            int puntuacioEquip = jugadors.PuntuacioEquip();

            // Check if the team points is bigger than current optimal team, then evaluate if the cost is lower than the available budget
            checkTeam(equip, jugadors, this.PresupostFitxatges, costEquip, puntuacioEquip);

            //Calculate statistics
            statistics.calculateStatistics(jugadors, costEquip, puntuacioEquip, PresupostFitxatges);
        }

        //Wait for threads; print global statistics; send signal to parent
        threadsFinished++;
        while (threadsFinished <= numOfThreads){
            if (threadsFinished == numOfThreads)
                finalPrint=true;
            statisticsSummary();
        }
    }

    public void statisticsSummary() {
        try{
            evaluatorLock.lock();
            threadsWaitingSummary++;
            //Wait for the others
            while (threadsWaitingSummary < numOfThreads)
                evaluatorFinished.await();
            statistics.printStatistics();
            threadEvaluator.globalStatistics.calculateGlobalStatistics(statistics);
            //Last thread prints global and sends signal to parent
            if (threadsWaitingSummary == numOfThreads*2 - 1){
                threadEvaluator.globalStatistics.printGlobalStatistics();
                threadsWaitingSummary = 0;
                threadEvaluator.globalStatistics.formatData();
                evaluatorFinishedPrinting.signalAll();
                if (finalPrint) {
                    threadsFinished++;
                    evaluatorsEnded.signalAll();
                }
            } else {
                threadsWaitingSummary++;
                evaluatorFinished.signal();
                evaluatorFinishedPrinting.await();
            }
        } catch (InterruptedException exception) {
            System.out.println("Program Interrupted");
        } finally {
            evaluatorLock.unlock();
        }
    }

    //Synchronized so it doesn't change value between checking the value and changing
    public static synchronized void checkTeam(int equip, JugadorsEquip jugadors, int PresupostFitxatges, int costEquip, int puntuacioEquip){
        if (puntuacioEquip > threadEvaluator.MaxPuntuacio && costEquip < PresupostFitxatges)
        {
            threadMessenger.addMessageToQueue("Thread: " + Thread.currentThread().getId() + " Team " + equip + " -> " + Error.color_green + " Cost: " + costEquip + " Points: " + puntuacioEquip + ". \n"+ Error.end_color);
            // We have a new partial optimal team.
            threadEvaluator.MaxPuntuacio=puntuacioEquip;
            threadEvaluator.MillorEquip = jugadors;
        }
        else
        {
            threadMessenger.addMessageToQueue("Thread: " + Thread.currentThread().getId() + " Team " + equip + " -> " + "Cost: " + costEquip + " Points: " + puntuacioEquip + ".\r");
        }
    }

    public int getEnd() { return end; }
}
