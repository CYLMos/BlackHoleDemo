/**
 * My Fitness function
 */
public class MyFitnessFunction implements FitnessInterface<Double> {

    /**
     * Get the fitness value
     *
     * @param params params
     * @return       fitness value
     */
    @Override
    public Double getFitnessValue(Double... params) {
        if(params.length != 4) {
            System.err.printf("params length error !");
        }

        Double powX = Math.pow(params[0] - params[2], 2);
        Double powY = Math.pow(params[1] - params[3], 2);

        return Math.sqrt(powX + powY);
    }
}
