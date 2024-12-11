/**
//Task 1 a,b,c ( d and e you can find it in the collab file
import ca.pfv.spmf.algorithms.frequentpatterns.fpgrowth.AlgoFPGrowth;
import ca.pfv.spmf.algorithms.frequentpatterns.fpgrowth.AlgoFPClose;
import ca.pfv.spmf.algorithms.frequentpatterns.fpgrowth.AlgoFPMax;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            String input = "src/contextual_data.txt";
            String outputFrequent = "src/frequent_itemsets.txt";
            String outputClosed = "src/closed_itemsets.txt";
            String outputMaximal = "src/maximal_itemsets.txt";

            // Minimum support threshold (35 transactions out of 2000 firms)
            double minsupp = 20.0 / 2000.0;

            // a) FP-Growth for Frequent Itemsets
            System.out.println("Running FP-Growth for frequent itemsets...");
            AlgoFPGrowth fpGrowth = new AlgoFPGrowth();
            fpGrowth.runAlgorithm(input, outputFrequent, minsupp);
            fpGrowth.printStats();

            // b) FPClose for Frequent Closed Itemsets
            System.out.println("Running FPClose for frequent closed itemsets...");
            AlgoFPClose fpClose = new AlgoFPClose();
            fpClose.runAlgorithm(input, outputClosed, minsupp);
            fpClose.printStats();

            // c) FPMax for Maximal Frequent Itemsets
            System.out.println("Running FPMax for maximal frequent itemsets...");
            AlgoFPMax fpMax = new AlgoFPMax();
            fpMax.runAlgorithm(input, outputMaximal, minsupp);
            fpMax.printStats();

            System.out.println("All tasks completed");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error occurred", e);
        }
    }
}
**/

 /** Task 2
import ca.pfv.spmf.algorithms.frequentpatterns.fpgrowth.AlgoFPGrowth;
import ca.pfv.spmf.algorithms.associationrules.agrawal94_association_rules.AlgoAgrawalFaster94;
import ca.pfv.spmf.algorithms.associationrules.closedrules.AlgoClosedRules;
import ca.pfv.spmf.algorithms.associationrules.TopKRules_and_TNR.AlgoTopKRules;
import ca.pfv.spmf.algorithms.associationrules.TopKRules_and_TNR.Database;
import ca.pfv.spmf.algorithms.associationrules.TopKRules_and_TNR.Transaction;
import ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemsets;
import ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemset;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {

            String input = "src/contextual_data.txt";
            String outputFrequentItemsets = "src/frequent_itemsets.txt";
            String outputAssocRules = "src/association_rules.txt";
            String outputClosedRules = "src/closed_association_rules.txt";
            String outputTopKRules = "src/top5_association_rules.txt";

            // Minimum support and confidence thresholds
            double minsupp = 35.0 / 2000.0; // 1.75%
            double minconf = 1.0;           // for a) and b)

            // a) Generate Frequent Itemsets using FP-Growth
            System.out.println("Generating Frequent Itemsets...");
            AlgoFPGrowth fpGrowth = new AlgoFPGrowth();
            Itemsets frequentItemsets = fpGrowth.runAlgorithm(input, null, minsupp);
            fpGrowth.printStats();

            if (frequentItemsets == null || frequentItemsets.getItemsetsCount() == 0) {
                System.out.println("No frequent itemsets were generated. Please adjust the support threshold.");
                return;
            }

            // a) Generate Association Rules with minsupp=35, minconf=1.0
            System.out.println("Generating Association Rules...");
            AlgoAgrawalFaster94 algo = new AlgoAgrawalFaster94();
            // For association rules, we pass frequentItemsets, output file, minimum support (0 here means from itemsets), and minconf
            algo.runAlgorithm(frequentItemsets, outputAssocRules, 0, minconf);
            algo.printStats();

            // b)  Closed Association Rules with minsupp=35, minconf=1.0
            System.out.println("Generating Closed Association Rules...");
            AlgoClosedRules closedAlgo = new AlgoClosedRules();
            closedAlgo.runAlgorithm(frequentItemsets, outputClosedRules, 0, minconf);
            closedAlgo.printStats();

            // c)  Top-5 Rules with minconf=0.8 using the older AlgoTopKRules
            System.out.println("Generating Top-5 Frequent Rules...");
            int k = 5;
            double topMinConf = 0.8;

            // Load database for the TopKRules algorithm
            Database db = new Database();
            db.loadFile(input);

            AlgoTopKRules topKRules = new AlgoTopKRules();

            topKRules.runAlgorithm(k, topMinConf, db);
            // If there's a method to print stats, use it. If not, comment out the next line.
            // topKRules.printStats(); // Remove this if the method doesn't exist in this variant

            // d) Simple Recommender Algorithm
            System.out.println("Generating Recommendations Based on Random Terms...");

            // Extract distinct items from the database transactions
            // Extract distinct items from the database transactions
            Set<Integer> distinctItems = new HashSet<>();
            for (Transaction transaction : db.getTransactions()) {
                List<Integer> itemsList = transaction.getItems();
                for (int item : itemsList) {
                    distinctItems.add(item);
                }
            }

            List<Integer> terms = new ArrayList<>(distinctItems);
            if (terms.size() < 2) {
                System.out.println("Not enough terms in the dataset for recommendations.");
                return;
            }

            Random random = new Random();
            // Pick two random terms
            int term1 = terms.get(random.nextInt(terms.size()));
            int term2 = terms.get(random.nextInt(terms.size()));
            System.out.println("Selected Terms for Recommendations: " + term1 + ", " + term2);

            //  recommendations based on the frequent itemsets containing these terms
            generateRecommendations(frequentItemsets, term1, term2);

            System.out.println("All tasks completed successfully.");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error occurred", e);
        }
    }

    //  top-5 recommendations based on selected terms
    private static void generateRecommendations(Itemsets frequentItemsets, int term1, int term2) {
        final int maxRecommendations = 5;
        List<Itemset> recommendations = new ArrayList<>();

        for (List<Itemset> level : frequentItemsets.getLevels()) {
            for (Itemset itemset : level) {
                int[] items = itemset.getItems();
                boolean containsTerm1 = Arrays.stream(items).anyMatch(x -> x == term1);
                boolean containsTerm2 = Arrays.stream(items).anyMatch(x -> x == term2);

                if (containsTerm1 && containsTerm2) {
                    recommendations.add(itemset);
                    if (recommendations.size() == maxRecommendations) {
                        break;
                    }
                }
            }
            if (recommendations.size() == maxRecommendations) {
                break;
            }
        }

        if (recommendations.isEmpty()) {
            System.out.println("No recommendations found for the selected terms.");
        } else {
            for (Itemset itemset : recommendations) {
                System.out.println("Itemset: " + Arrays.toString(itemset.getItems())
                        + " (Support: " + itemset.getAbsoluteSupport() + ")");
            }
        }
    }

}

**/

/**
import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        // to the datasets
        String newsDataset = "src/hse_5282_20_sep_u.txt";
        String educationDataset = "src/hse_5289_20_sep_u.txt";
        String financeDataset = "src/hse_5516_20_sep_u.txt";

        // Output dataset after reduction
        String reducedDataset = "reduced_context.csv"; // Save as CSV for Python compatibility

        // Reduce the datasets to ensure ~100 formal concepts
        System.out.println("Reducing datasets...");
        reduceDataset(newsDataset, reducedDataset, 100);
        System.out.println("Dataset reduction complete. Output saved to: " + reducedDataset);
    }


     //Reduce the dataset by removing attributes/objects iteratively to ensure ~100 formal concepts.

    private static void reduceDataset(String inputPath, String outputPath, int targetConcepts) throws IOException {
        List<String[]> data = loadDataset(inputPath);

        // Iteratively remove rows to approach the target number of formal concepts
        while (true) {
            if (data.size() <= 1) {
                System.out.println("Warning: Dataset too small to continue reduction.");
                break;
            }

            // Remove a random row
            int removeIndex = new Random().nextInt(data.size());
            data.remove(removeIndex);

            // Calculate the number of formal concepts
            int conceptCount = countFormalConcepts(data);


            System.out.println("Current formal concepts: " + conceptCount);

            // Stop when target formal concepts are achieved
            if (conceptCount <= targetConcepts) {
                System.out.println("Target formal concepts achieved: " + conceptCount);
                break;
            }
        }

        // Save
        saveDatasetToCSV(data, outputPath);
    }


    private static int countFormalConcepts(List<String[]> data) {
        // Count unique intents (rows) as formal concepts
        Set<Set<String>> uniqueConcepts = new HashSet<>();
        for (String[] row : data) {
            Set<String> intent = new HashSet<>(Arrays.asList(row));
            uniqueConcepts.add(intent);
        }
        return uniqueConcepts.size();
    }


    private static List<String[]> loadDataset(String path) throws IOException {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.add(line.split("\\s+")); // Split by whitespace
            }
        }
        return data;
    }


    private static void saveDatasetToCSV(List<String[]> data, String path) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (String[] row : data) {
                bw.write(String.join(",", row)); // CSV format with commas
                bw.newLine();
            }
        }
    }
}

**/

/**
import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        // datasets
        String reducedDataset = "reduced_context.csv"; // From task (a)


        System.out.println("Extracting concepts...");
        extractConcepts(reducedDataset, 2);


        System.out.println("Extracting implications...");
        extractImplications(reducedDataset);
    }


     //Step c: Extract 3-5 examples of concepts with extent size > 2 and interpret them.

    private static void extractConcepts(String datasetPath, int minIntentSize) throws IOException {
        List<String[]> data = loadDatasetFromCSV(datasetPath);
        Map<Set<String>, Set<Integer>> formalConcepts = generateFormalConcepts(data);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("concepts_output.csv"))) {
            writer.write("Extent Size,Intent\n"); // Header
            System.out.println("Extracting concepts with extent size > 2...");
            int count = 0;

            for (Map.Entry<Set<String>, Set<Integer>> entry : formalConcepts.entrySet()) {
                if (entry.getValue().size() > 2) { // Extent size > 2
                    String intent = entry.getKey().toString();
                    int extentSize = entry.getValue().size();
                    writer.write(extentSize + "," + intent + "\n");
                    System.out.println("Concept: ⟨Extent Size: " + extentSize + ", Intent: " + intent + "⟩");
                    count++;
                    if (count >= 5) break; // Limit to 3-5 examples
                }
            }
        }
    }



     //Step d: Extract implications A → B with support and confidence.

    private static void extractImplications(String datasetPath) throws IOException {
        List<String[]> data = loadDatasetFromCSV(datasetPath);
        Map<Set<String>, Set<Integer>> formalConcepts = generateFormalConcepts(data);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("implications_output.csv"))) {
            writer.write("A,B,Support,Confidence\n"); // Header
            System.out.println("Extracting implications...");

            for (Map.Entry<Set<String>, Set<Integer>> entry : formalConcepts.entrySet()) {
                Set<String> intent = entry.getKey();
                for (String attribute : intent) {
                    // A = Intent - {attribute}
                    Set<String> A = new HashSet<>(intent);
                    A.remove(attribute);

                    // B = {attribute}
                    Set<String> B = new HashSet<>();
                    B.add(attribute);

                    // Support(A ∪ B)
                    double supportAB = calculateSupport(intent, data);

                    // Support(A)
                    double supportA = calculateSupport(A, data);

                    // Confidence
                    double confidence = (supportA > 0) ? (supportAB / supportA) : 0;

                    // to CSV
                    writer.write(A + "," + B + "," + supportAB + "," + confidence + "\n");
                    System.out.println("Implication: " + A + " → " + B +
                            " (Support: " + supportAB + ", Confidence: " + confidence + ")");
                }
            }
        }
    }


    //Helper: Calculate support for a given set of attributes (intent).

    private static double calculateSupport(Set<String> intent, List<String[]> data) {
        int matchingObjects = 0;

        for (String[] object : data) {
            boolean matches = true;

            for (String attribute : intent) {
                int attributeIndex;
                try {
                    attributeIndex = Integer.parseInt(attribute.replace("Attribute_", ""));
                } catch (NumberFormatException e) {
                    System.err.println("Invalid attribute format: " + attribute);
                    matches = false;
                    break;
                }

                // Check if the attribute index is valid within the object
                if (attributeIndex >= object.length || !"1".equals(object[attributeIndex].trim())) {
                    matches = false;
                    break;
                }
            }

            if (matches) {
                matchingObjects++;
            }
        }

        return (double) matchingObjects / data.size();
    }

    //Helper: Load a dataset from a CSV file.

    private static List<String[]> loadDatasetFromCSV(String path) throws IOException {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.add(line.split(",")); // CSV format
            }
        }
        return data;
    }


     //Helper: Generate formal concepts from the dataset.

    private static Map<Set<String>, Set<Integer>> generateFormalConcepts(List<String[]> data) {
        Map<Set<String>, Set<Integer>> concepts = new HashMap<>();

        for (int i = 0; i < data.size(); i++) {
            Set<String> intent = new HashSet<>();
            for (int j = 0; j < data.get(i).length; j++) {
                if ("1".equals(data.get(i)[j].trim())) {
                    intent.add("Attribute_" + j);
                }
            }
            concepts.computeIfAbsent(intent, k -> new HashSet<>()).add(i);
        }
        return concepts;
    }
}
 **/