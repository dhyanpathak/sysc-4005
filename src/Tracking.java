import java.util.ArrayList;
import java.util.HashMap;

public class Tracking {
    private static Tracking instance = null;
    private static HashMap<String, ArrayList<Double>> serviceTimes = new HashMap<String, ArrayList<Double>>();
    private static HashMap<String, ArrayList<Double>> occupiedTimes = new HashMap<String, ArrayList<Double>>();
    private static HashMap<Integer, Integer> throughput = new HashMap<Integer, Integer>();

    public synchronized static Tracking getInstance() {
        if(instance == null) {
            instance = new Tracking();
        }
        return instance;
    }


    public synchronized void addServiceTime(String key, Double serviceTime) {
        if(serviceTimes.containsKey(key)) {
            ArrayList<Double> times = serviceTimes.get(key);
            times.add(serviceTime);
            serviceTimes.put(key, times);
        } else {
            ArrayList<Double> times = new ArrayList<Double>();
            times.add(serviceTime);
            serviceTimes.put(key, times);
        }
    }

    public synchronized void addOccupiedTime(String key, Double occupiedTime) {
        if(occupiedTimes.containsKey(key)) {
            ArrayList<Double> times = occupiedTimes.get(key);
            times.add(occupiedTime);
            occupiedTimes.put(key, times);
        } else {
            ArrayList<Double> times = new ArrayList<Double>();
            times.add(occupiedTime);
            occupiedTimes.put(key, times);
        }
    }

    public synchronized void addThroughput(Integer key) {
        if(throughput.containsKey(key)) {
            Integer inventory = throughput.get(key) + 1;
            throughput.put(key, inventory);
        } else {
            throughput.put(key, 1);
        }
    }

    public synchronized void generateStats(int replNum, int replDur) {
        for(Integer key: throughput.keySet()) {
            Double inventory = Double.valueOf(throughput.get(key));
            Double time = Double.valueOf(replNum) * (Double.valueOf(replDur)/60.0);
            Double rate = inventory / time;
            System.out.println("Throughput for product " + key + " = " + rate + " per minute");
        }

        for(String key: serviceTimes.keySet()) {
            Double sum = 0.0;
            Double n = Double.valueOf(serviceTimes.get(key).size());

            for(Double entry : serviceTimes.get(key)) {
                sum += (entry / 1000);
            }

            Double mean = sum/n;
            System.out.println("Average Service Time for " + key + " = " + mean);
        }

        for(String key: occupiedTimes.keySet()) {
            Double sum = 0.0;
            Double n = Double.valueOf(occupiedTimes.get(key).size());
            Double z = 0.95;
            Double stdDev = 0.0;

            for(Double entry : occupiedTimes.get(key)) {
                sum += entry;
            }

            Double mean = sum/n;

            System.out.print("Confidence Interval of Occupied Time for " + key + " = " + mean);

            for(Double entry : occupiedTimes.get(key)) {
                stdDev += Math.pow(entry - mean, 2);
            }
            stdDev = Math.sqrt(stdDev/ (n-1));
            double plusMinus = z* (stdDev/ Math.sqrt(n));
            System.out.println(" + or - " + plusMinus);
        }
    }
}
