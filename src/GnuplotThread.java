import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GnuplotThread extends Thread {

    /// gnuplot commands
    private List<String> gnuplotCommands;

    /**
     * Constructor
     */
    public GnuplotThread() {
        this.gnuplotCommands = new ArrayList<>();
    }

    /**
     * Run function
     */
    public void run() {
        try {
            Process process = Runtime.getRuntime().exec(
                    this.gnuplotCommands.toArray(new String[this.gnuplotCommands.size()])
            );
            InputStream stdin = process.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stdin);
            BufferedReader br = new BufferedReader(isr);

            String line;
            while((line = br.readLine()) != null) {
                System.err.println(line);
            }
            process.waitFor();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * Set gnuplot commands
     *
     * @param gnuplotCommands gnuplot commands
     */
    public void setGnuplotCommands(List<String> gnuplotCommands) {
        this.gnuplotCommands = gnuplotCommands;
    }

    /**
     * Get gnuplot commands
     *
     * @return gnuplot commands
     */
    public List<String> getGnuplotCommands() {
        return this.gnuplotCommands;
    }
}
