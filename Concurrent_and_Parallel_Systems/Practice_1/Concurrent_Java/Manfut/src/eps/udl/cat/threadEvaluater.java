/* ---------------------------------------------------------------
Práctica 1.
Código fuente: threadEvaluater.java
Grau Informàtica
49259953W i Sergi Puigpinós Palau.
47694432E i Jordi Lazo Florensa.
--------------------------------------------------------------- */
package eps.udl.cat;

public class threadEvaluater extends Thread{

    private int first;
    private int end;
    private int PresupostFitxatges;
    private boolean isNull = false;

    private Market market;
    private JugadorsEquip MillorEquip = null;

    threadEvaluater(int first, int end, int PresupostFitxatges, Market market){
        this.first = first;
        this.end = end;
        this.market = market;
        this.PresupostFitxatges = PresupostFitxatges;
    }

    @Override
    public void run(){
        int equip;
        int MaxPuntuacio=-1;

        for (equip=first;equip<=end;equip++)
        {
            JugadorsEquip jugadors;

            // Get playes from team number. Returns false if the team is not valid.
            if ((jugadors=market.ObtenirJugadorsEquip(new IdEquip(equip)))==null)
                continue;

            //System.out.print("Team " + equip + "->");

            // Reject teams with repeated players.
            if (jugadors.JugadorsRepetits())
            {
                //System.out.println(Error.color_red +" Invalid." + Error.end_color);
                continue;	// Equip no valid.
            }

            // Chech if the team points is bigger than current optimal team, then evaluate if the cost is lower than the available budget
            if (jugadors.PuntuacioEquip()>MaxPuntuacio && jugadors.CostEquip()<PresupostFitxatges)
            {
                System.out.print("Thread: " + Thread.currentThread().getId() + " Team " + equip + "->");
                // We have a new partial optimal team.
                MaxPuntuacio=jugadors.PuntuacioEquip();
                MillorEquip = jugadors;
                System.out.println(Error.color_green + " Cost: " + jugadors.CostEquip() + " Points: " + jugadors.PuntuacioEquip() + ". "+ Error.end_color);
            }
            else
            {
                //System.out.println(" Cost: " + jugadors.CostEquip() + " Points: " + jugadors.PuntuacioEquip() + ".\r");
            }
        }
        if (this.MillorEquip == null)
            isNull=true;
    }

    public JugadorsEquip getMillorEquip(){
        return this.MillorEquip;
    }
    public int getEnd() { return end; }
    public boolean isNull() { return isNull; }
}
