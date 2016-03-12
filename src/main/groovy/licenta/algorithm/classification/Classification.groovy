package licenta.algorithm.classification

import licenta.domain.logic.ClassificationData
import licenta.domain.logic.FileImage
import licenta.domain.logic.Settings

public trait Classification {

    public abstract void train(Settings settings);

    public abstract ClassificationData recognize(double[] input);

    public abstract void settingData(List<ClassificationData> data);

    public abstract gettingData();

    public static List<ClassificationData> prepareData(List<FileImage> data) {

        def list = new ArrayList<ClassificationData>();

        data.stream()
                .sorted({ FileImage a, FileImage b -> a.name.compareToIgnoreCase(b.name) })
                .forEach({ elem ->
            boolean notFound = true;
            for (ClassificationData e : list) {
                if (e.getName().compareTo(elem.getName()) == 0) {
                    e.getData().add(elem.getSemnificativeScaledImageTransformated());
                    notFound = false;
                    break;
                }
            }
            if (notFound) {
                List<double[]> d = new ArrayList<double[]>();
                d.add(elem.getSemnificativeScaledImageTransformated());
                list.add(new ClassificationData(elem.getName(), d));
            }
        });


        return list;
    }
}
