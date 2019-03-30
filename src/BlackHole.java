/**
 * Black hole class
 */
public class BlackHole {

    /// The star in the black hole
    private Star star;

    /// The radius of the black hole
    private double radius;

    /**
     * Constructor
     *
     * @param star The star in the black hole
     */
    public BlackHole(Star star) {
        this.star = star;
    }

    /**
     * Set the star
     *
     * @param star The star in the black hole
     */
    public void setStar(Star star) {
        this.star = star;
    }

    /**
     * Get the star
     *
     * @return The star in the black hole
     */
    public Star getStar() {
        return this.star;
    }

    /**
     * Set the radius
     *
     * @param radius The radius of the black hole
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * Get the radius
     *
     * @return The radius of the black hole
     */
    public double getRadius() {
        return this.radius;
    }
}