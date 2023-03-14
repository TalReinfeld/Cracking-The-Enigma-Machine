package logic;

import java.util.ArrayList;
import java.util.List;

public class BinomialCoefficient {

    private List<int[]> combinations = new ArrayList<>();

    public BinomialCoefficient(int n, int r){
        generate(n,r);
    }

    public void generate(int n, int r) {

        int[] combination = new int[r];
        // initialize with lowest lexicographic combination
        for (int i = 0; i < r; i++) {
            combination[i] = i;
        }

        while (combination[r - 1] < n) {
            combinations.add(combination.clone());
            // generate next combination in lexicographic order
            int t = r - 1;
            while (t != 0 && combination[t] == n - r + t) {
                t--;
            }

            combination[t]++;
            for (int i = t + 1; i < r; i++) {
                combination[i] = combination[i - 1] + 1;
            }
        }
    }

    public List<int[]> getCombinations(){
        return combinations;
    }
}
