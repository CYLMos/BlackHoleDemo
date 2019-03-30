import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Black hole algorithm class
 */
public class BlackHoleAlgorithm {

    /// The number of stars
    private int starCount;

    /// The target location
    private double[] target;

    /// The space bound
    private double[] bound;

    /// Loop times
    private int loopTimes;

    /// The user-defined fitness function
    private MyFitnessFunction myFitnessFunction;

    /// The best location
    private double[] bestLocation = null;

    /// The thread drawing the graph
    private GnuplotThread gnuplotThread;

    /**
     * Constructor
     *
     * @param starCount the number of stars
     * @param target    the target point
     */
    public BlackHoleAlgorithm(int starCount, double[] target, double[] bound, int loopTimes) {
        this.starCount = starCount;
        this.target = target;
        this.bound = bound;
        this.loopTimes = loopTimes;
        this.myFitnessFunction = new MyFitnessFunction();
        this.gnuplotThread = new GnuplotThread();

        String s[] = {
                "/usr/bin/gnuplot",
                "-p",
                "-e",
                "set term wxt; " +
                        "set title \"BH Demo\" font \",20\"; " +
                        "set xrange[0:" + this.bound[0] + "]; " +
                        "set yrange[0:" + this.bound[1] + "]; " +
                        "set object 1 circle at first " + this.target[0] + "," + this.target[1] +
                        " radius char 1.0 fillcolor rgb '#aa1100' fillstyle solid lw 2; " +
                        "plot 0;"
        };
        this.gnuplotThread.setGnuplotCommands(new ArrayList<String>(Arrays.asList(s)));
    }

    /**
     * Start BH algorithm
     */
    public void start() {

        /// Initial stars
        List<Star> starList = this.initial();

        /// Start loop
        this.startLoop(starList);
    }

    /**
     *
     *
     * @return The list of stars
     */
    private List<Star> initial() {
        List<Star> starList = new LinkedList<>();

        for(int i = 0; i < this.starCount; i++) {
            double x = (Math.random() * (this.bound[0] + 1));
            double y = (Math.random() * (this.bound[1] + 1));

            double[] location = {x, y};
            Star star = new Star(location);

            starList.add(star);
        }

        return starList;
    }

    /**
     * Start the loop to find the best location
     */
    private void startLoop(List<Star> starList) {
        BlackHole blackHole = null;

        for(int i = 0; i < this.loopTimes; i++) {
            /**
             * Move stars
             */
            if(blackHole != null) {
                for (Star star : starList) {
                    /// The black hole should not move
                    if (star == blackHole.getStar()) {
                        continue;
                    }

                    for (int j = 0; j < star.getLocation().length; j++) {
                        star.getLocation()[j] += Math.random() *
                                (blackHole.getStar().getLocation()[j] - star.getLocation()[j]);
                    }
                }
            }

            /**
             * The black hole absorbs the too close star
             */
            if(blackHole != null) {
                for (Star star : starList) {

                    double distance = this.myFitnessFunction.getFitnessValue(
                            star.getLocation()[0], star.getLocation()[1],
                            blackHole.getStar().getLocation()[0], blackHole.getStar().getLocation()[1]
                    );

                    /// Replace the star
                    if(distance <= blackHole.getRadius() && star != blackHole.getStar()) {
                        double x = (Math.random() * (this.bound[0] + 1));
                        double y = (Math.random() * (this.bound[1] + 1));

                        double[] location = {x, y};
                        star.setLocation(location);
                    }
                }

            }

            /**
             * Choose the black hole
             */
            Star nextBestStar = blackHole != null ? blackHole.getStar() : null;
            double totalFitnessValues = 0;
            for(Star star : starList) {
                double[] location = star.getLocation();
                double fitnessValue = this.myFitnessFunction.getFitnessValue(
                        location[0], location[1],
                        this.target[0], this.target[1]
                );

                totalFitnessValues += fitnessValue;

                if(blackHole != null) {
                    double bestFitnessValue = this.myFitnessFunction.getFitnessValue(
                            blackHole.getStar().getLocation()[0], blackHole.getStar().getLocation()[1],
                            this.target[0], this.target[1]
                    );

                    if(fitnessValue < bestFitnessValue) {
                        nextBestStar = star;
                    }
                }
                else {
                    if(nextBestStar == null) {
                        nextBestStar = star;
                    }
                    else {
                        double bestFitnessValue = this.myFitnessFunction.getFitnessValue(
                                nextBestStar.getLocation()[0], nextBestStar.getLocation()[1],
                                this.target[0], this.target[1]
                        );

                        if(fitnessValue < bestFitnessValue) {
                            nextBestStar = star;
                        }
                    }
                }
            }

            if(blackHole == null) {
                blackHole = new BlackHole(null);
            }

            /// Renew the black hole
            blackHole.setStar(nextBestStar);
            double blackHoleFitnessValue = this.myFitnessFunction.getFitnessValue(
                    blackHole.getStar().getLocation()[0], blackHole.getStar().getLocation()[1],
                    this.target[0], this.target[1]
            );
            blackHole.setRadius(blackHoleFitnessValue / totalFitnessValues);

            /// Update best location
            if(this.bestLocation != null) {
                double bestFitnessValue = this.myFitnessFunction.getFitnessValue(
                        this.bestLocation[0], this.bestLocation[1],
                        this.target[0], this.target[1]
                );

                /// Update best location
                this.bestLocation = bestFitnessValue > blackHoleFitnessValue ?
                        blackHole.getStar().getLocation() :
                        this.bestLocation;
            }
            else {
                this.bestLocation = blackHole.getStar().getLocation();
            }

            for(int j = 0; j < starList.size(); j++) {
                Star star = starList.get(j);

                this.gnuplotThread.getGnuplotCommands().add("-e");
                String newCommand = "";
                if(blackHole.getStar() == star) {
                    newCommand = "set object " + (j + 2) + " circle at first " +
                            star.getLocation()[0] + "," + star.getLocation()[1] +
                            " radius char 1.0 fillcolor rgb 'black' fillstyle solid lw 2;";
                }
                else {
                    newCommand = "set object " + (j + 2) + " circle at first " +
                            star.getLocation()[0] + "," + star.getLocation()[1] +
                            " radius char 0.8 fillcolor rgb 'green' fillstyle solid lw 2;";
                }

                List<String> commands = this.gnuplotThread.getGnuplotCommands();
                commands.add(newCommand);

            }

            this.gnuplotThread.getGnuplotCommands().set(
                    this.gnuplotThread.getGnuplotCommands().size() - 1,
                    this.gnuplotThread.getGnuplotCommands().get(this.gnuplotThread.getGnuplotCommands().size() - 1) +
                            "plot 0;"
                    );
        }

        this.gnuplotThread.start();

        try {
            this.gnuplotThread.join();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * Get the best location
     *
     * @return The best location
     */
    public double[] getBestLocation() {
        return this.bestLocation;
    }

}
