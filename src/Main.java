/**
 * Main class
 */
public class Main {

    public static void main(String[] argv) {
        int starCount = 20;
        double[] target = {50.0, 50.0};
        double[] bound = {100.0, 100.0};
        int loopTimes = 50;

        for(int i = 0; i < argv.length;) {
            if(argv[i].equals("-c")) {
                starCount = Integer.parseInt(argv[i + 1]);
                i += 2;
            }
            else if(argv[i].equals("-t")) {
                target = new double[]{Double.parseDouble(argv[i + 1]), Double.parseDouble(argv[i + 2])};
                i += 3;
            }
            else if(argv[i].equals("-b")) {
                bound = new double[]{Double.parseDouble(argv[i + 1]), Double.parseDouble(argv[i + 2])};
                i += 3;
            }
            else if(argv[i].equals("-l")) {
                loopTimes = Integer.parseInt(argv[i + 1]);
                i += 2;
            }
        }

        BlackHoleAlgorithm blackHoleAlgorithm = new BlackHoleAlgorithm(starCount, target, bound, loopTimes);
        blackHoleAlgorithm.start();

        System.out.printf("The best location (%f, %f)\n",
                blackHoleAlgorithm.getBestLocation()[0], blackHoleAlgorithm.getBestLocation()[1]);
    }

}
