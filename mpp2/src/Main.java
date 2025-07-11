import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main {


    public static double[] vectorize(String text) {
        double[] freq = new double[26];
        text = text.toLowerCase().replaceAll("[^a-z]", "");
        for (char c : text.toCharArray()) {
            freq[c - 'a']++;
        }
        double total = Arrays.stream(freq).sum();
        if (total == 0) return freq;
        for (int i = 0; i < 26; i++) {
            freq[i] /= total;
        }
        return freq;
    }


    public static String loadCleanText(String path) throws IOException {
        String text = Files.readString(Paths.get(path));
        text = text.toLowerCase().replaceAll("[^\\p{ASCII}]", "");
        text = text.replaceAll("[^a-z\\s]", "");
        return text;
    }

    public static void main(String[] args) throws IOException {
        List<double[]> data = new ArrayList<>();
        List<int[]> labels = new ArrayList<>();

        Map<String, int[]> langLabels = Map.of(
                "data/en.txt", new int[]{1, 0, 0},
                "data/pl.txt", new int[]{0, 1, 0},
                "data/es.txt", new int[]{0, 0, 1}
        );

        for (Map.Entry<String, int[]> entry : langLabels.entrySet()) {
            String path = entry.getKey();
            int[] label = entry.getValue();
            String text = loadCleanText(path);
            double[] vec = vectorize(text);
            data.add(vec);
            labels.add(label);
        }

        Layer layer = new Layer(3, 26, 0.1);
        layer.train(data, labels, 10000);


        Scanner scanner = new Scanner(System.in);
        System.out.println("Wybierz sposób testowania:\n1 - Wczytaj tekst z pliku\n2 - Wpisz tekst ręcznie");
        String mode = scanner.nextLine();

        while (true) {
            String inputText = "";
            if (mode.equals("1")) {
                System.out.print("Podaj ścieżkę do pliku (lub wpisz 'exit' aby zakończyć): ");
                String filePath = scanner.nextLine();
                if (filePath.equalsIgnoreCase("exit")) break;
                try {
                    inputText = loadCleanText(filePath);
                } catch (IOException e) {
                    System.out.println("Błąd podczas wczytywania pliku: " + e.getMessage());
                    continue;
                }
            } else {
                System.out.println("Wklej tekst do klasyfikacji (wpisz 'done' aby zakończyć wprowadzanie tekstu):");
                StringBuilder sb = new StringBuilder();
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.equalsIgnoreCase("done")) {
                        if (sb.isEmpty()) return;
                        break;
                    }
                    sb.append(line).append(" ");
                }
                inputText = sb.toString().toLowerCase().replaceAll("[^a-z\\s]", "");
            }

            double[] input = vectorize(inputText);
            List<Double> output = layer.layerCompute(input);
            String[] langs = {"Angielski", "Polski", "Hiszpanski"};
            for (int i = 0; i < output.size(); i++) {
                System.out.printf("%s: %.4f\n", langs[i], output.get(i));
            }
            int maxIdx = 0;
            for (int i = 1; i < output.size(); i++) {
                if (output.get(i) > output.get(maxIdx)) maxIdx = i;
            }
            System.out.println("=> Przewidywany język: " + langs[maxIdx]);
        }
    }
}
