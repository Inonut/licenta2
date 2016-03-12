package licenta.domain.logic;

public class BackPropagationSettings {

    private int epochs;
    private int[] hidden;
    private double error;
    private int dimension;

    public int getEpochs() {
        return epochs;
    }

    public void setEpochs(int epochs) {
        this.epochs = epochs;
    }

    public int[] getHidden() {
        return hidden;
    }

    public void setHidden(int[] hidden) {
        this.hidden = hidden;
    }

    public double getError() {
        return error;
    }

    public void setError(double error) {
        this.error = error;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }
}
