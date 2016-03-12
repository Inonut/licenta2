package licenta.algorithm.classification.impl

import licenta.algorithm.classification.Classification
import licenta.domain.logic.*
import licenta.utils.BussinesConstants
import licenta.utils.Util

public class BackPropagationMethod implements Classification {

    private List<ClassificationData> data;
    private BackPropagationSettings settings;
    private List<Layer[]> layers;

    @Override
    public void train(Settings settings) {
        this.settings = settings.getBackPropagationSettings();
        this.settings.setDimension((int) (BussinesConstants.HEIGHT * BussinesConstants.WIDTH));

        //---temporar----prepare hidden nodes --- ignore hidden with 0
        List<Integer> h = new ArrayList<>();
        for (int e : this.settings.getHidden()) {
            if (e > 0) {
                h.add(e);
            }
        }
        int[] hh = new int[h.size()];
        for (int i = 0; i < h.size(); i++) {
            hh[i] = h.get(i);
        }
        this.settings.setHidden(hh);
        //--------------------------

        this.initializeNetwork();

        double currentError = 9999;
        int currentIteration = 0;

        while (currentError > this.settings.getError() && currentIteration < this.settings.getEpochs()) {
            currentError = 0;
            for (ClassificationData c : data) {
                for (double[] pattern : c.getData()) {
//            		long t1;
//            		t1 = System.currentTimeMillis();
                    layers.stream()
                            .findFirst()
                            .ifPresent({ elems ->
                        int k = 0;
                        for (Layer layer : elems) {
                            layer.setOutput(pattern[k++]);
                        }
                    });

//            		System.out.println("1 "+(System.currentTimeMillis()-t1));
//					t1 = System.currentTimeMillis();
                    currentError += this.forwardPropagate(c.getName());
//            		System.out.println("2 "+(System.currentTimeMillis()-t1));
//            		t1 = System.currentTimeMillis();
                    this.updateError();
//            		System.out.println("3 "+(System.currentTimeMillis()-t1));
//            		t1 = System.currentTimeMillis();
                    this.updateWeights();
//            		System.out.println("4 "+(System.currentTimeMillis()-t1));
//            		t1 = System.currentTimeMillis();
                }
            }

            currentIteration++;

            // if (currentIteration % 5 == 0)

            System.out.println(currentError + " " + currentIteration);
        };
    }

    private void updateWeights() {

        for (int k = 1; k < layers.size(); k++) {
            Layer[] layer2 = layers.get(k);
            Layer[] layer1 = layers.get(k - 1);
            for (int i = 0; i < layer2.length; i++) {
                for (int j = 0; j < layer1.length; j++) {
                    layer1[j].getWeights()[i] += 0.2 * layer2[i].getError() * layer1[j].getOutput();
                }
            }
        }
    }

    private void updateError() {

        double total;

        for (int k = layers.size() - 1; k >= 2; k--) {
            Layer[] layer2 = layers.get(k);
            Layer[] layer1 = layers.get(k - 1);
            for (int i = 0; i < layer1.length; i++) {
                total = 0;
                for (int j = 0; j < layer2.length; j++) {
                    total += layer1[i].getWeights()[j] * layer2[j].getError();
                }
                layer1[i].setError(total);
            }
        }

    }

    private double forwardPropagate(String name) {

        double total;
        double totalError = 0;
        for (int k = 1; k < layers.size(); k++) {
            Layer[] layer2 = layers.get(k);
            Layer[] layer1 = layers.get(k - 1);
            for (int i = 0; i < layer2.length; i++) {
                total = 0;
                for (int j = 0; j < layer1.length; j++) {
                    total += layer1[j].getOutput() * layer1[j].getWeights()[i];
                }
                layer2[i].setOutput(f(total));
                if (layer2[i] instanceof Output) {
                    Output output = (Output) layer2[i];
                    output.setTarget(output.getName().compareTo(name) == 0 ? 1 : 0);
                    output.setError((output.getTarget() - output.getOutput()) * output.getOutput() * (1 - output.getOutput()));
                    totalError += Math.pow((output.getTarget() - output.getOutput()), 2) / 2;
                }
            }
        }

        return totalError;

    }

    private double f(double x) {
        return (1 / (1 + Math.exp(-x)));
    }

    private void initializeNetwork() {

        int[] layersDim = new int[settings.getHidden().length + 2];
        layersDim[0] = settings.getDimension();
        for (int i = 0; i < settings.getHidden().length; i++) {
            layersDim[i + 1] = settings.getHidden()[i];
        }
        layersDim[layersDim.length - 1] = data.size();


        Random r = new Random();
        layers = new ArrayList<Layer[]>();
        for (int i = 0; i < layersDim.length - 1; i++) {
            Hidden[] layer = new Hidden[layersDim[i]];
            for (int j = 0; j < layer.length; j++) {
                layer[j] = new Hidden();
                layer[j].setWeights(Util.generateArray(r, layersDim[i + 1]));
            }

            layers.add(layer);
        }


        Output[] output = new Output[data.size()];
        for (int i = 0; i < output.length; i++) {
            output[i] = new Output();
            output[i].setName(data.get(i).getName());
        }
        layers.add(output);
    }

    @Override
    public ClassificationData recognize(double[] input) {

        double total = 0.0;
        double max = -1;
        String val = null;

        layers.stream()
                .findFirst()
                .ifPresent({ elems ->
            int k = 0;
            for (Layer layer : elems) {
                layer.setOutput(input[k++]);
            }
        });

        for (int k = 1; k < layers.size(); k++) {
            Layer[] layer2 = layers.get(k);
            Layer[] layer1 = layers.get(k - 1);
            for (int i = 0; i < layer2.length; i++) {
                total = 0;
                for (int j = 0; j < layer1.length; j++) {
                    total += layer1[j].getOutput() * layer1[j].getWeights()[i];
                }
                layer2[i].setOutput(f(total));
                if (layer2[i] instanceof Output) {
                    Output output = (Output) layer2[i];
                    if (output.getOutput() > max) {
                        max = output.getOutput();
                        val = output.getName();
                    }
                }
            }
        }
        System.out.println(val);
        return new ClassificationData(val, null);
    }

    @Override
    public void settingData(List<ClassificationData> data) {
        this.data = data;
    }

    @Override
    def gettingData() {
        return ["trainData": this.data, "network": this.layers];
    }

}
