/**
 * Star class
 */
public class Star {

    /// The location of the star
    private double[] location;

    /**
     * Constructor
     *
     * @param location The location of the star
     */
    public Star(double[] location) {
        this.location = location;
    }

    /**
     * Set the location
     *
     * @param location The location of the star
     */
    public void setLocation(double[] location) {
        this.location = location;
    }

    /**
     * Get the location
     *
     * @return The location of the star
     */
    public double[] getLocation() {
        return this.location;
    }
}
