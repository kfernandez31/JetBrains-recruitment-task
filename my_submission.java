import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println(solve(new ArrayList<>(List.of(2L, 5L, 1L)), 1, 5));
    }

    private static List<List<Long>> solve(List<Long> vals, Integer l, Integer k) {
        ArrayList<List<Long>> res = new ArrayList<>();
        for (Integer i = l; i <= k; i++) {
            res.add(help(vals, true, i));
        }
        return res;
    }

    private static List<Long> help(List<Long> vals, boolean copy, Integer k) {
        if (k == 1) {
            // omits checking the condition in every iteration of the loop below + more readable
            return copy? new ArrayList<>(vals) : vals;
        }
        if (k > vals.size() || vals.isEmpty()) {
            // omits unnecessary recursive calls
            return copy? new ArrayList<>() : vals.subList(0, 0); // empty sub-list
        }
        ArrayList<Long> res = new ArrayList<>();
        for (int i = 0; i < vals.size() - 1 && k-1 <= vals.size()-(i+1); i++) { // removed unnecessary iterations
            List<Long> newVals = new ArrayList<>(vals.subList(i+1, vals.size())); // saves O(n) time for removal of the non-suffix
            newVals = help(newVals, false, k-1);
            for (int j = 0; j < newVals.size(); j++)
                newVals.set(j, newVals.get(j) * vals.get(i));
            res.addAll(newVals);
        }
        return res;
    }
}
