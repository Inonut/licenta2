package licenta.domain.logic;

public class Layer {

    protected double error;
    protected double output;
    protected double[] weights;

    public double getError() {
        return error;
    }

    public void setError(double error) {
        this.error = error;
    }

    public double getOutput() {
        return output;
    }

    public void setOutput(double output) {
        this.output = output;
    }

    public double[] getWeights() {
        return weights;
    }

    public void setWeights(double[] weights) {
        this.weights = weights;
    }

    def propertyMissing(name, value) {
        println "Worning: miss property ${name} with value ${value}"
    }
}
