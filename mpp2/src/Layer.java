import java.util.ArrayList;
import java.util.List;

public class Layer {
    private List<Perceptron> perceptrons;

    public Layer(int perceptronCount, int inputSize, double alpha) {
        perceptrons = new ArrayList<>();
        for (int i = 0; i < perceptronCount; i++) {
            perceptrons.add(new Perceptron(inputSize, alpha));
        }
    }

    public List<Double> layerCompute(double[] data) {
        List<Double> outputs = new ArrayList<>();
        for (Perceptron p : perceptrons) {
            outputs.add(p.compute(data));
        }
        return outputs;
    }

    public void layerUpdate(double[] data, int[] decision) {
        for (int i = 0; i < perceptrons.size(); i++) {
            perceptrons.get(i).update(data, decision[i]);
        }
    }
    public void train(List<double[]> dataSet, List<int[]> decisionSet, int iterations) {
        for (int iter = 0; iter < iterations; iter++) {
            for (int i = 0; i < dataSet.size(); i++) {
                layerUpdate(dataSet.get(i), decisionSet.get(i));
            }
        }
    }
}
