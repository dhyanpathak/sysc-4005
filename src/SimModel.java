import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import org.uncommons.maths.random.ExponentialGenerator;

public class SimModel {
    private static final int REPLICATION_NUMBER = 100;
    private static final int REPLICATION_DURATION = 60;
    private static double ws1Mean;
    private static double ws2Mean;
    private static double ws3Mean;
    private static double servinsp1Mean;
    private static double servinsp22Mean;
    private static double servinsp23Mean;

    private static ArrayList<ArrayList<Double>> simulationVars = new ArrayList<ArrayList<Double>>();

    private static ExponentialGenerator setupRN(String filename) throws FileNotFoundException {
        double sum = 0;
        File file = new File("./src/data/" + filename);
        Scanner scanner = new Scanner(file);
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            if(line.length() < 2) {
                continue;
            }
            sum += Double.parseDouble(line);
        }
        double mean = sum / 300;

        if(filename == "servinsp1.dat") {
            servinsp1Mean = mean;
        }

        if(filename == "servinsp22.dat") {
            servinsp22Mean = mean;
        }

        if(filename == "servinsp23.dat") {
            servinsp23Mean = mean;
        }

        if(filename == "ws1.dat") {
            ws1Mean = mean;
        }

        if(filename == "ws2.dat") {
            ws2Mean = mean;
        }

        if(filename == "ws3.dat") {
            ws3Mean = mean;
        }

        double rate = 1/ mean;
        Random random = new Random();
        ExponentialGenerator gen = new ExponentialGenerator(rate, random);

        return gen;
    }

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        ExponentialGenerator servinsp1RN = setupRN("servinsp1.dat");
        ExponentialGenerator servinsp22RN = setupRN("servinsp22.dat");
        ExponentialGenerator servinsp23RN = setupRN("servinsp23.dat");
        ExponentialGenerator ws1RN = setupRN("ws1.dat");
        ExponentialGenerator ws2RN = setupRN("ws2.dat");
        ExponentialGenerator ws3RN = setupRN("ws3.dat");

        double ws1RandomMean = 0.0;
        double ws2RandomMean = 0.0;
        double ws3RandomMean = 0.0;
        double servinsp1RandomMean = 0.0;
        double servinsp22RandomMean = 0.0;
        double servinsp23RandomMean = 0.0;

        for(int i = 1; i < REPLICATION_NUMBER+1; i++) {
            Buffer firstC1 = new Buffer(1);
            Buffer secondC1 = new Buffer(1);
            Buffer firstC2 = new Buffer(2);
            Buffer thirdC1 = new Buffer(1);
            Buffer firstC3 = new Buffer(3);

            Thread workstation1 = new Workstation(1);
            ((Workstation) workstation1).addBuffer(firstC1);

            Thread workstation2 = new Workstation(2);
            ((Workstation) workstation2).addBuffer(secondC1);
            ((Workstation) workstation2).addBuffer(firstC2);

            Thread workstation3 = new Workstation(3);
            ((Workstation) workstation3).addBuffer(thirdC1);
            ((Workstation) workstation3).addBuffer(firstC3);

            Thread inspector1 = new Inspector(1);
            ((Inspector) inspector1).addBuffer(firstC1);
            ((Inspector) inspector1).addBuffer(secondC1);
            ((Inspector) inspector1).addBuffer(thirdC1);

            ArrayList<Workstation> workstations = new ArrayList<Workstation>();
            workstations.add((Workstation) workstation1);
            workstations.add((Workstation) workstation2);
            workstations.add((Workstation) workstation3);
            ((Inspector) inspector1).setTrackedWorkstations(workstations);

            Thread inspector2 = new Inspector(2);
            ((Inspector) inspector2).addBuffer(firstC2);
            ((Inspector) inspector2).addBuffer(firstC3);


            System.out.println("========== Iteration #" + i + " starting ==========");
            double ws1 = (ws1RN.nextValue());
            double ws2 = (ws2RN.nextValue());
            double ws3 = (ws3RN.nextValue());
            double servinsp1 = servinsp1RN.nextValue();
            double servinsp22 = servinsp22RN.nextValue();
            double servinsp23 = servinsp23RN.nextValue();

            ws1RandomMean += ws1;
            ws2RandomMean += ws2;
            ws3RandomMean += ws3;
            servinsp1RandomMean += servinsp1;
            servinsp22RandomMean += servinsp22;
            servinsp23RandomMean += servinsp23;

            ((Workstation) workstation1).setWaitTime(1, ws1);
            ((Workstation) workstation2).setWaitTime(2, ws2);
            ((Workstation) workstation3).setWaitTime(3, ws3);
            ((Inspector) inspector1).setWaitTime(1, servinsp1);
            ((Inspector) inspector2).setWaitTime(2, servinsp22);
            ((Inspector) inspector2).setWaitTime(3, servinsp23);

            workstation1.start();
            workstation2.start();
            workstation3.start();
            inspector1.start();
            inspector2.start();

            Thread.currentThread().sleep(REPLICATION_DURATION * 1000);

            ((Inspector) inspector1).setRunning(false);
            ((Inspector) inspector2).setRunning(false);
            ((Workstation) workstation1).setRunning(false);
            ((Workstation) workstation2).setRunning(false);
            ((Workstation) workstation3).setRunning(false);

            System.out.println("========== Iteration #" + i + " finished ==========");

        }
        System.out.println("=========== SIMULATION ENDED ===========");
        System.out.println("__________ GENERATING REPORT ___________");

        servinsp1RandomMean = servinsp1RandomMean/ ((double) REPLICATION_NUMBER);
        servinsp22RandomMean = servinsp22RandomMean/ ((double) REPLICATION_NUMBER);
        servinsp23RandomMean = servinsp23RandomMean/ ((double) REPLICATION_NUMBER);
        ws1RandomMean = ws1RandomMean/ ((double) REPLICATION_NUMBER);
        ws2RandomMean = ws2RandomMean/ ((double) REPLICATION_NUMBER);
        ws3RandomMean = ws3RandomMean/ ((double) REPLICATION_NUMBER);

        System.out.println(String.format("servinsp1.dat actual mean = %f", servinsp1Mean));
        System.out.println(String.format("servinsp1.dat random mean = %f", servinsp1RandomMean));
        System.out.println(String.format("difference = %f", getPercentageDifference(servinsp1Mean, servinsp1RandomMean)));

        System.out.println(String.format("\nservinsp22.dat actual mean = %f", servinsp22Mean));
        System.out.println(String.format("servinsp22.dat random mean = %f", servinsp22RandomMean));
        System.out.println(String.format("difference = %f", getPercentageDifference(servinsp22Mean, servinsp22RandomMean)));

        System.out.println(String.format("\nservinsp23.dat actual mean = %f", servinsp23Mean));
        System.out.println(String.format("servinsp23.dat random mean = %f", servinsp23RandomMean));
        System.out.println(String.format("difference = %f", getPercentageDifference(servinsp23Mean, servinsp23RandomMean)));

        System.out.println(String.format("\nws1.dat actual mean = %f", ws1Mean));
        System.out.println(String.format("ws1.dat random mean = %f", ws1RandomMean));
        System.out.println(String.format("difference = %f", getPercentageDifference(ws1Mean, ws1RandomMean)));

        System.out.println(String.format("\nws2.dat actual mean = %f", ws2Mean));
        System.out.println(String.format("ws2.dat random mean = %f", ws2RandomMean));
        System.out.println(String.format("difference = %f", getPercentageDifference(ws2Mean, ws2RandomMean)));

        System.out.println(String.format("\nws3.dat actual mean = %f", ws3Mean));
        System.out.println(String.format("ws3.dat random mean = %f", ws3RandomMean));
        System.out.println(String.format("difference = %f", getPercentageDifference(ws3Mean, ws3RandomMean)));


        Tracking.getInstance().generateStats(REPLICATION_NUMBER, REPLICATION_DURATION);
    }

    public static Double getPercentageDifference(Double og, Double newer) {
        return (Math.abs(newer - og) / og) * 100.0;
    }
}
