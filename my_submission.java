import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println(solve(new ArrayList<>(List.of(2L, 5L, 1L)), 1, 5));
    }

    private static List<List<Long>> solve(List<Long> vals, Integer l, Integer r) {
        ArrayList<List<Long>> res = new ArrayList<>();
        for (Integer i = l; i <= r; i++) {
            res.add(new ArrayList<>());
            help(res.get(i-l), vals, 1L, i);
        }
        return res;
    }

    private static void help(List<Long> res, List<Long> vals, Long factor, Integer k) {
        if (k == 1) {
            for (int i = 0; i < vals.size(); i++) {
                res.add(i, factor * vals.get(i));
            }
        } else if (k <= vals.size() && !vals.isEmpty()) {
            for (int i = 0; i < vals.size() - 1 && k-1 <= vals.size() - (i + 1); i++) {
                List<Long> suffix = vals.subList(i+1, vals.size());
                help(res, suffix, factor * vals.get(i), k - 1);
            }
        }
    }


}

