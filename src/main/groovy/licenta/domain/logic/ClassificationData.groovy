package licenta.domain.logic

public class ClassificationData {

    private String name;
    private List<double[]> data;

    public ClassificationData() {
    }

    public ClassificationData(String name, List<double[]> data) {
        super();
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<double[]> getData() {
        return data;
    }

    public void setData(List<double[]> data) {
        this.data = data;
    }
}
