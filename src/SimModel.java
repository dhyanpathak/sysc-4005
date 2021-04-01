import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import org.uncommons.maths.random.ExponentialGenerator;

public class SimModel {
    private static int Clock;
    private static Queue<Event> FEL;

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
        Random random = new Random();
        ExponentialGenerator gen = new ExponentialGenerator(mean, random);

        return gen;
    }

    public static void main(String[] args) throws FileNotFoundException {
        ExponentialGenerator ws1RN = setupRN("ws1.dat");
        ExponentialGenerator ws2RN = setupRN("ws2.dat");
        ExponentialGenerator ws3RN = setupRN("ws3.dat");
        ExponentialGenerator servinsp1 = setupRN("servinsp1.dat");
        ExponentialGenerator servinsp22 = setupRN("servinsp22.dat");
        ExponentialGenerator servinsp23 = setupRN("servinsp23.dat");

        System.out.println(ws1RN.nextValue() * 60);

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

        Thread inspector2 = new Inspector(2);
        ((Inspector) inspector2).addBuffer(firstC2);
        ((Inspector) inspector2).addBuffer(firstC3);

        workstation1.start();
        workstation2.start();
        workstation3.start();
        inspector1.start();
        inspector2.start();


        int rounds = 100;
        int i = 0;

        while(i < rounds) {

        }

    }
}
