/* ---------------------------------------------------------------
Práctica 2.
Código fuente: Market.java
Grau Informàtica
49259953W i Sergi Puigpinós Palau.
47694432E i Jordi Lazo Florensa.
--------------------------------------------------------------- */
package eps.udl.cat;

import java.util.ArrayList;
import java.io.*;


public class Market {
    ArrayList<Jugador> Jugadors;

    int     NJugadors;
    int     NPorters;
    int     NDefensors;
    int     NMitjos;
    int     NDelanters;



    Market() {
        Jugadors = new ArrayList<Jugador>();
    }

    Jugador GetJugador(int j) { return Jugadors.get(j); }
    Jugador GetPorter(int j) { return Jugadors.get(j); }
    Jugador GetDefensor(int j) { return Jugadors.get(NPorters+j); }
    Jugador GetMitg(int j) { return Jugadors.get(NPorters+NDefensors+j); }
    Jugador GetDelanter(int j) { return Jugadors.get(NPorters+NDefensors+NMitjos+j); }

    // Read file with the market players list (each line containts a plater: "Id.;Name;Position;Cost;Team;Points")
    Error LlegirFitxerJugadors(String pathJugadors)
    {
        long Offset=0;
        FileInputStream fis;

        try {
            fis = new FileInputStream(pathJugadors);
        } catch (FileNotFoundException e) {
            System.err.println("[Market:LlegirFitxerJugadors] File "+pathJugadors+" not found.");
            e.printStackTrace();
            return(Error.CErrorOpenInputFile);
        }

        try {
            //Construct BufferedReader from InputStreamReader
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            br.readLine(); // skip csv header

            // Read players.
            NJugadors=NPorters=NDefensors=NMitjos=NDelanters=0;
            String line = null;
            while ((line = br.readLine())!=null)
            {
                String fields[] = line.split(";");
                Jugador player = new Jugador();

                // Player's identificator
                player.setId(Integer.parseInt(fields[0]));

                // Player's name
                player.setNom(fields[1]) ;

                // Player's position
                switch(fields[2]) {
                    case "Portero":
                        player.setTipus(TipusJug.JPorter);
                        NPorters++;
                        break;

                    case "Defensa":
                        player.setTipus(TipusJug.JDefensor);
                        NDefensors++;
                        break;

                    case "Medio":
                        player.setTipus(TipusJug.JMitg);
                        NMitjos++;
                        break;

                    case "Delantero":
                        player.setTipus(TipusJug.JDelanter);
                        NDelanters++;
                        break;

                    default:
                        System.err.println("[Market:LlegirFitxerJugadors] Error player type.");
                        return(Error.CErrorPlayerType);
                }

                // Player's cost
                player.setCost(Integer.parseInt(fields[3]));

                // Player's team
                player.setEquip(fields[4]); ;

                // Player's points
                player.setPunts(Integer.parseInt(fields[5]));

                NJugadors++;
                Jugadors.add(player);
            }
            br.close();

        } catch (IOException e) {
            System.err.println("[Market:LlegirFitxerJugadors] Reading file "+pathJugadors+".");
            e.printStackTrace();
            return(Error.CErrorReadingFile);
        }

        return(Error.COk);
    }


    JugadorsEquip  CalcularEquipOptim(int PresupostFitxatges, int numOfThreads, int M)
    {
        long maxbits;
        int primerEquip, ultimEquip, first, end;

        JugadorsEquip MillorEquip = null;

        //Thread array
        threadEvaluator[] threads = new threadEvaluator[numOfThreads];

        //PrintJugadors();

        // Calculated number of bits required for all teams codification.
        maxbits=Manfut.Log2(NPorters)*JugadorsEquip.DPosPorters+Manfut.Log2(NDefensors)*JugadorsEquip.DPosDefensors+Manfut.Log2(NMitjos)*JugadorsEquip.DPosMitjos+Manfut.Log2(NDelanters)*JugadorsEquip.DPosDelanters;
        if (maxbits>Manfut.Log2(Long.MAX_VALUE))
            Error.showError("[Market:CalcularEquipOptim] The number of player overflow the maximum width supported.");

        // Calculate first and end team that have to be evaluated.
        first=primerEquip=GetEquipInicial();
        end=ultimEquip=(int)Math.pow(2,maxbits);

        int remaining = ultimEquip-primerEquip;

        // Evaluating different teams/combinations.
        System.out.println("Evaluating form " + String.format("%x",first) + "H to " + String.format("%x",end) + "H (Maxbits: "+ maxbits + "). Evaluating "+ (end-first)+"  teams...");

        //Initialize messenger thread
        threadMessenger messenger = new threadMessenger();
        messenger.start();

        //Initialize threads
        for (int i=0; i<numOfThreads; ++i){
            int pivot = remaining / (numOfThreads-i);
            int prevThreadEnd = i==0 ? first : threads[i-1].getEnd()+1;
            int threadEnd = i==numOfThreads-1 ? end : prevThreadEnd+pivot;
            threads[i] = new threadEvaluator(prevThreadEnd, threadEnd , PresupostFitxatges, this, M, numOfThreads);
            threads[i].start();
            remaining -= pivot;
        }

        //Wait for the ending signal
        try{
            threadEvaluator.evaluatorLock.lock();
            threadEvaluator.evaluatorsEnded.await();
        }catch (java.lang.InterruptedException exception) {
            System.out.println("Program Interrupted");
        }finally {
            threadEvaluator.evaluatorLock.unlock();
        }

        //Wait for messenger to print all and exit
        try{
            threadMessenger.messengerLock.lock();
            threadMessenger.killMessenger();
            threadMessenger.messengerEnded.await();
        }catch (java.lang.InterruptedException exception) {
            System.out.println("Program Interrupted");
        }finally {
            threadMessenger.messengerLock.unlock();
        }

        return(threadEvaluator.MillorEquip);
    }


    // Calculate the initial team combination.
    int GetEquipInicial()
    {
        int p, equip=0;
        int bitsPorters, bitsDefensors, bitsMitjos, bitsDelanters;

        bitsPorters = Manfut.Log2(NPorters);
        bitsDefensors = Manfut.Log2(NDefensors);
        bitsMitjos = Manfut.Log2(NMitjos);
        bitsDelanters = Manfut.Log2(NDelanters);

        for (p=JugadorsEquip.DPosDelanters-1;p>=0;p--)
        {
            equip+=p;
            equip = equip << bitsDelanters;
        }

        for (p=JugadorsEquip.DPosMitjos-1;p>=0;p--)
        {
            equip+=p;
            equip = equip << bitsMitjos;
        }

        for (p=JugadorsEquip.DPosDefensors-1;p>=0;p--)
        {
            equip+=p;
            equip = equip << bitsDefensors;
        }

        for (p=JugadorsEquip.DPosPorters-1;p>0;p--)
        {
            equip+=p;
            equip = equip << bitsPorters;
        }

        return (equip);
    }


    // Convert team combination to an struct with all the player by position.
    // Returns false if the team is not valid.
    JugadorsEquip ObtenirJugadorsEquip (IdEquip equip)
    {
        int p;
        int bitsPorters, bitsDefensors, bitsMitjos, bitsDelanters;
        JugadorsEquip jugadors=new JugadorsEquip(this);

        bitsPorters = Manfut.Log2(NPorters);
        bitsDefensors = Manfut.Log2(NDefensors);
        bitsMitjos = Manfut.Log2(NMitjos);
        bitsDelanters = Manfut.Log2(NDelanters);

        for (p=0;p<JugadorsEquip.DPosPorters;p++)
        {
            jugadors.setPorter((equip.getIdEquip()>>(bitsPorters*p)) & ((int)Math.pow(2,bitsPorters)-1));
            if (jugadors.getPorter()>=NPorters)
                return null;
        }

        for (p=0;p<JugadorsEquip.DPosDefensors;p++)
        {
            jugadors.setDefensor(p, (equip.getIdEquip()>>((bitsPorters*JugadorsEquip.DPosPorters)+(bitsDefensors*p))) & ((int)Math.pow(2,bitsDefensors)-1));
            if (jugadors.getDefensor(p)>=NDefensors)
                return null;
        }

        for (p=0;p<JugadorsEquip.DPosMitjos;p++)
        {
            jugadors.setMitg(p, (equip.getIdEquip()>>((bitsPorters*JugadorsEquip.DPosPorters)+(bitsDefensors*JugadorsEquip.DPosDefensors)+(bitsMitjos*p))) & ((int)Math.pow(2,bitsMitjos)-1));
            if (jugadors.getMitg(p)>=NMitjos)
                return null;
        }

        for (p=0;p<JugadorsEquip.DPosDelanters;p++)
        {
            jugadors.setDelanter(p,(equip.getIdEquip()>>((bitsPorters*JugadorsEquip.DPosPorters)+(bitsDefensors*JugadorsEquip.DPosDefensors)+(bitsMitjos*JugadorsEquip.DPosMitjos)+(bitsDelanters*p))) & ((int)Math.pow(2,bitsDelanters)-1));
            if (jugadors.getDelanter(p)>=NDelanters)
                return null;
        }

        return jugadors;
    }


    // Prints all market players information,
    void PrintJugadors()
    {
        int j;

        for(j=0;j<NJugadors;j++)
        {
            GetJugador(j).print();
        }
    }

}
