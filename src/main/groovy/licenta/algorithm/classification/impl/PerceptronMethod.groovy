package licenta.algorithm.classification.impl

import licenta.algorithm.classification.Classification
import licenta.domain.logic.ClassificationData
import licenta.domain.logic.DataPerceptron
import licenta.domain.logic.PerceptronSettings
import licenta.domain.logic.Settings
import licenta.util.BussinesConstants
import licenta.util.Util

public class PerceptronMethod implements Classification {

    private List<DataPerceptron> data;
    private PerceptronSettings settings;
    private int nbOfData;

    @Override
    public void train(Settings settings) {
        this.settings = settings.getPerceptrinSettings();
        this.settings.setDimension((int) (BussinesConstants.HEIGHT * BussinesConstants.WIDTH));

        nbOfData = 0;
        data.stream()
                .forEach({ elem -> nbOfData += elem.getData().size() });

        data.stream()
                .forEach({ elem ->
            long t1 = System.currentTimeMillis();
            double[][] input = makeData(elem);
            System.out.println(System.currentTimeMillis() - t1);
            elem.setWeight(getBestWeight(input));
            System.out.println(System.currentTimeMillis() - t1);
        });
    }

    private double[] getBestWeight(double[][] input) {

        boolean ok = false;
        double[] weight = new double[settings.getDimension() + 1];

        Random random = new Random();
        /*for(int i=0;i<=settings.getDimension();i++){
            weight[i] = random.nextDouble()*20-10;
        }*/
        weight = Util.generateArray(random, settings.getDimension() + 1);

        for (int i = 0; i < settings.getEpochs() && !ok; i++) {
            ok = true;
            for (int j = 0; j < input.length; j++) {
                if (prod(input[j], weight) <= 0) {
                    weight = add(weight, input[j]);
                    ok = false;
                }
            }
        }

        return weight;

    }

    private double[] add(double[] weight, double[] input) {

        for (int i = 0; i < input.length; i++) {
            weight[i] += input[i];
        }

        return weight;
    }

    private double prod(double[] input, double[] weight) {

        int val = 0;

        for (int i = 0; i < input.length; i++) {
            val += input[i] * weight[i];
        }

        return val;
    }

    private int k = 0;

    private double[][] makeData(DataPerceptron item) {

        double[][] input = new double[nbOfData][settings.getDimension() + 1];

        k = 0;
        item.getData()
                .stream()
                .forEach({ elem -> input[k++] = normalize(elem, 1) });

        data.stream()
                .filter({ elem -> elem.getName().compareTo(item.getName()) != 0 })
                .forEach({ elem ->
            elem.getData()
                    .stream()
                    .forEach({ el -> input[k++] = normalize(el, -1) });
        });

        return input;
    }

    private double[] normalize(double[] elem, int theta) {
        double[] result = new double[elem.length + 1];
        result[0] = theta;

        for (int i = 0; i < elem.length; i++) {
            result[i + 1] = theta * elem[i];
        }

        return result;
    }


    @Override
    public ClassificationData recognize(double[] input) {

        double[] result = normalize(input, 1);
        double bestError;
        DataPerceptron dataError;

        bestError = -Integer.MAX_VALUE;

        data.stream()
                .forEach({ elem ->
            double val = prod(result, elem.getWeight());
            System.out.println(elem.getName() + " " + val);
            if (val > bestError) {
                bestError = val;
                dataError = elem;
            }
        });

        if (bestError <= 0) {
            System.out.println("Habar nu am ce e asta! Dar pare a fi " + dataError.getName());
        } else {
            System.out.println("Este " + dataError.getName());
        }

        return dataError;
    }

    @Override
    public void settingData(List<ClassificationData> data) {
        this.data = new ArrayList<DataPerceptron>();
        data.stream()
                .forEach({ elem ->
            DataPerceptron d = new DataPerceptron();
            d.setData(elem.getData());
            d.setName(elem.getName());
            this.data.add(d);
        });
    }

    @Override
    def gettingData() {
        return this.data;
    }
}
