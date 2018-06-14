package solvers;

public abstract class IterativeSolver extends Solver {

    private double eps = 1e-10;
    private int lastIterations = 0;

    public double getEps() {
        return eps;
    }

    public void setEps(double eps) {
        this.eps = eps;
    }

    public int getLastIterations() {
        return lastIterations;
    }

    public void reset() {
        lastIterations = 0;
    }

    public void step() {
        lastIterations++;
    }

}
