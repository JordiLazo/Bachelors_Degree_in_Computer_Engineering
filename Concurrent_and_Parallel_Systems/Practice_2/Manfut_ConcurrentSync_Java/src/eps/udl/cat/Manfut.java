/* ---------------------------------------------------------------
Práctica 2.
Código fuente: Manfut.java
Grau Informàtica
49259953W i Sergi Puigpinós Palau.
47694432E i Jordi Lazo Florensa.
--------------------------------------------------------------- */
package eps.udl.cat;

public class Manfut {

    public static void main(String[] args)
    {
        long startTime = System.nanoTime();
        Market          PlayersMarket;
        int             PresupostFitxatges, threads;
        int             M = 25000;
        JugadorsEquip   MillorEquip;
        Error           err;

        // Procesar argumentos.
        if (args.length<3)
            throw new IllegalArgumentException("Error in arguments: ManFut <presupost> <fitxer_jugadors> <threads>");

        PresupostFitxatges = Integer.parseInt(args[0]);

        PlayersMarket = new Market();
        err = PlayersMarket.LlegirFitxerJugadors(args[1]);
        if (err!=Error.COk)
            Error.showError("[Manfut] ERROR Reading players file.");

        threads = Integer.parseInt(args[2]);
        if (threads <= 0)
            throw new IllegalArgumentException("Error in arguments: Invalid number of threads");

        if (args.length > 3){
            M = Integer.parseInt(args[3]);
            if (M <= 0)
                throw new IllegalArgumentException("Error in arguments: Invalid M");
        }

        // Calculate the best team.
        MillorEquip=PlayersMarket.CalcularEquipOptim(PresupostFitxatges, threads, M);
        System.out.print(Error.color_blue);
        System.out.println("-- Best Team -------------------------------------------------------------------------------------");
        MillorEquip.PrintEquipJugadors();
        System.out.println("   Cost " + MillorEquip.CostEquip() +", Points: " + MillorEquip.PuntuacioEquip() + ".");
        System.out.println("--------------------------------------------------------------------------------------------------");
        System.out.print(Error.end_color);


        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.print(duration / 1000000);
        System.exit(0);
    }


    // Methods Definition
    static int log(int x, int base)
    {
        return (int) Math.ceil((Math.log(x) / Math.log(base)));
    }

    static long log(long x, int base)
    {
        return (long) Math.ceil((Math.log(x) / Math.log(base)));
    }

    static int Log2(int x)
    {
        return (int) (log(x,2) );
    }

    static long Log2(long x)
    {
        return (long) (log(x,2) );
    }
}
