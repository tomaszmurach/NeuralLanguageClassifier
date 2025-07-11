import java.util.Random;

public class Perceptron {
    private double[] weights;
    private double alpha; // stała uczenia
    private double theta; // próg aktywacji

    public Perceptron(int size, double alpha) {
        this.alpha = alpha;
        this.theta = 0.0;
        this.weights = new double[size];
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            this.weights[i] = -1 + 2 * rand.nextDouble(); // zakres [-1, 1]
        }
    }

    public double compute(double[] data) {
        double net = 0.0;
        for (int i = 0; i < data.length; i++) {
            net += data[i] * weights[i];
        }
        net -= theta;
        return sigmoid(net);
    }

    public void update(double[] data, double expected) {
        double net = 0.0;
        for (int i = 0; i < data.length; i++) {
            net += data[i] * weights[i];
        }
        net -= theta;
        double output = sigmoid(net);
        double delta = (expected - output) * output * (1 - output);

        for (int i = 0; i < weights.length; i++) {
            weights[i] += alpha * delta * data[i];
        }

        theta -= alpha * delta;
    }

    public void info() {}

    private double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }
}
