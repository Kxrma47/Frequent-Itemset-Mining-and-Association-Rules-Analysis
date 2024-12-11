
# **Frequent Itemset Mining and Association Rules Analysis**

## **Project Overview**

This repository contains the implementation of **Frequent Itemset Mining**, **Association Rules Mining**, and **Formal Concept Analysis** using Java (with the **SPMF library**) and Python for validation and analysis. The dataset comprises **2000 companies** and **3000 terms**, and the tasks explore various types of itemsets, rules, and recommendation systems based on frequent patterns.

---

## **Tasks and Implementations**

### **Task 1: Frequent Itemset Mining**

This task focuses on mining frequent itemsets, closed frequent itemsets, and maximal frequent itemsets with a support threshold of **35 transactions**.

1. **Find All Frequent Itemsets**
   - **Support Threshold**: `minsupp = 35 / 2000 = 0.0175`
   - **Algorithm**: FP-Growth  
   - **Output**:  
     ```
     Frequent itemsets count: 14,974,454
     ```

2. **Find Frequent Closed Itemsets**
   - **Algorithm**: FPClose  
   - **Output**:  
     ```
     Closed frequent itemset count: 498,116
     ```

3. **Find Maximal Frequent Itemsets**
   - **Algorithm**: FPMax  
   - **Output**:  
     ```
     Maximal frequent itemset count: 128,217
     ```

4. **Identify Itemsets with 10+ Terms**
   - **Extraction Function**:  
     ```python
     extract_large_itemsets(file_path, min_terms=10)
     ```
   - **Output**: 10 itemsets with 10 or more terms for frequent, closed, and maximal itemsets.

5. **Recommender Algorithm**
   - Selects two frequent terms and recommends a **Top-5 list** based on frequent itemsets.  
   - **Sample Output**:  
     ```
     Selected Terms: ['1430', '322']
     Top-5 Recommendations:  
     Item: 1823, Score: 359109  
     ```

---

### **Task 2: Association Rules Mining**

1. **Association Rules with `minsupp = 35` and `minconf = 1`**
   - **Algorithm**: AlgoAgrawalFaster94  
   - **Output**:  
     ```
     Number of association rules: 10940
     ```

2. **Closed Association Rules**
   - **Algorithm**: AlgoClosedRules  
   - **Output**:  
     ```
     Number of closed association rules: 10940
     ```

3. **Top-5 Frequent Rules with `minconf = 0.8`**
   - **Algorithm**: AlgoTopKRules  
   - **Output**: Top-5 rules saved to `top5_association_rules.txt`.

4. **Recommender Algorithm**
   - Generates recommendations based on frequent itemsets.

---

### **Task 3: Formal Concept Analysis**

1. **Dataset Reduction**
   - Reduced the dataset to approximately **100 formal concepts**.  
   - **Output**: 319 attributes, 4124 objects.

2. **Lattice Diagram Construction**
   - Generated lattice diagrams using **Concept Explorer**.

3. **Concept Extraction**
   - Extracted concepts as ⟨Extent Size, Intent⟩ pairs.  
   - **Sample Output**:  
     ```
     Extent Size: 4, Intent: [Attribute_43]
     ```

4. **Implication Analysis**
   - Extracted implications \( A \to B \) with support and confidence.  
   - **Sample Output**:  
     ```
     [Attribute_1] → [Attribute_4] (Support: 0.015, Confidence: 1.0)
     ```

---

## **Project Structure**

```
├── src/
│   ├── contextual_data.txt              # Input dataset
│   ├── frequent_itemsets.txt            # Frequent itemsets output
│   ├── association_rules.txt            # Association rules output
│   ├── closed_association_rules.txt     # Closed association rules output
│   ├── top5_association_rules.txt       # Top-5 frequent rules output
│   └── Main.java                        # Main Java implementation
├── reduced_context.csv                  # Reduced context for FCA
├── validate_transactions.py             # Python validation script
├── spmf.jar                             # SPMF library
└── README.md                            # This file
```

---

## **How to Run**

### **Java Implementation**

1. **Compile** the Java code with the SPMF library:
   ```bash
   javac -cp ".:spmf.jar" src/Main.java
   ```

2. **Run** the program:
   ```bash
   java -cp ".:spmf.jar" src.Main
   ```

### **Python Validation**

1. **Run** the validation script:
   ```bash
   python validate_transactions.py
   ```

---

## **Dependencies**

- **Java**: JDK 8 or above
- **SPMF Library**: `spmf.jar`
- **Python**: Version 3.7+

---

## **Results Summary**

- **Frequent Itemsets**: 14,974,454  
- **Closed Frequent Itemsets**: 498,116  
- **Maximal Frequent Itemsets**: 128,217  
- **Association Rules**: 10,940  
- **Top-5 Rules**: Generated with `minconf = 0.8`  
- **Formal Concepts**: ~100 concepts extracted and analyzed

---

## **Acknowledgments**

- **SPMF Library**: For frequent pattern mining and association rule generation.
