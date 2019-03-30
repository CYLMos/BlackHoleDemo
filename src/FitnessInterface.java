/**
 * Fitness function interface
 */
public interface FitnessInterface<T> {

    /**
     * Get the fitness value
     *
     * @param params params
     * @return       fitness value
     */
    public T getFitnessValue(T...params);
}
