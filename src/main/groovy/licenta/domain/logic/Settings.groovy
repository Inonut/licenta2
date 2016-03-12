package licenta.domain.logic;

public class Settings {
    private PerceptronSettings perceptrinSettings;
    private BackPropagationSettings backPropagationSettings;

    public PerceptronSettings getPerceptrinSettings() {
        return perceptrinSettings;
    }

    public void setPerceptrinSettings(PerceptronSettings perceptrinSettings) {
        this.perceptrinSettings = perceptrinSettings;
    }

    public BackPropagationSettings getBackPropagationSettings() {
        return backPropagationSettings;
    }

    public void setBackPropagationSettings(BackPropagationSettings backPropagationSettings) {
        this.backPropagationSettings = backPropagationSettings;
    }
}
