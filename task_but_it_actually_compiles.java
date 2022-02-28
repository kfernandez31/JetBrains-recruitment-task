import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        System.out.println(solve(new ArrayList<>(List.of(Long.valueOf(2), Long.valueOf(5), Long.valueOf(1))), 1, 5));
    }

    private static List<List<Long>> solve(List<Long> vals, Integer l, Integer k) {
        List<List<Long>> res = new ArrayList<>();
        for (Integer i = l; i <= k; i++) {
            res.add(help(vals, i));
        }
        return res;
    }

    private static List<Long> help(List<Long> vals, Integer k) {
        List<Long> res = new ArrayList<>();
        for (int i = 0; i < vals.size(); i++) {
            if (k == 1)
                res.add(vals.get(i));
            else {
                int idx = i;
                List<Long> newVals = new ArrayList<>(vals);
                for (int j = 0; j < idx + 1; j++) {
                    if (!newVals.isEmpty()) {
                        newVals.remove(0);
                    }
                }
                res.addAll(help(newVals, k - 1).stream().map(it -> it * vals.get(idx)).collect(Collectors.toList()));
            }
        }
        return res;
    }
}
