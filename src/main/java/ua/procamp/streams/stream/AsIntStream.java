package ua.procamp.streams.stream;

import ua.procamp.streams.function.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AsIntStream implements IntStream {

    private final int[] values;
    private List<Function> functions = new ArrayList<>();

    private AsIntStream() {
        values = new int[0];
        functions = new ArrayList<>();
    }

    private AsIntStream(int[] values) {
        if (values == null) {
            throw new NullPointerException("null value passed");
        }
        this.values = values;
    }

    private AsIntStream(int[] values, List<Function> functions) {
        this(values);
        this.functions = functions;
    }

    public static IntStream of(int... values) {
        return new AsIntStream(values);
    }

    @Override
    public Double average() {
        checkIsEmpty();
        return Double.valueOf(internalSum()) / values.length;
    }

    @Override
    public Integer max() {
        checkIsEmpty();
        int[] values = applyAll();
        int max = values[0];
        for (int i = 1; i < values.length; i++) {
            if (values[i] > max) {
                max = values[i];
            }
        }
        return max;
    }

    @Override
    public Integer min() {
        checkIsEmpty();
        int[] values = applyAll();
        int min = values[0];
        for (int i = 1; i < values.length; i++) {
            if (values[i] < min) {
                min = values[i];
            }
        }
        return min;
    }

    @Override
    public long count() {
        //FIXME: If it would be real stream, it wouldn't be limited to arrays length (max integer)?
        return (long) applyAll().length;
    }

    @Override
    public int sum() {
        checkIsEmpty();
        return (int) internalSum();
    }

    public long internalSum() {
        long sum = 0;
        int[] processed = applyAll();
        for (int i = 0; i < processed.length; i++) {
            sum = sum + processed[i];
        }
        return sum;
    }

    @Override
    public IntStream filter(IntPredicate predicate) {
        Function function = new Function() {
            @Override
            public int[] apply(int[] values2) {
                int[] intermediate2 = new int[values2.length];
                boolean[] isValidArr2 = new boolean[values2.length];
                for (int i = 0; i < values2.length; i++) {
                    if (predicate.test(values2[i])) {
                        intermediate2[i] = values2[i];
                        isValidArr2[i] = true;
                    }
                }
                int numOfValid2 = 0;
                for (boolean valid : isValidArr2) {
                    if (valid) {
                        numOfValid2++;
                    }
                }
                if (numOfValid2 > 0) {
                    int[] out2 = new int[numOfValid2];
                    int insertCount2 = 0;
                    for (int i = 0; i < values2.length; i++) {
                        if (isValidArr2[i]) {
                            out2[insertCount2] = intermediate2[i];
                            insertCount2 = insertCount2 + 1;
                        }
                    }
                    return out2;
                }
                return new int[]{};

            }
        };
        functions.add(function);
        return new AsIntStream(values, functions);
    }

    @Override
    public void forEach(IntConsumer action) {
        int[] values = applyAll();
        for (int i = 0; i < values.length; i++) {
            action.accept(values[i]);
        }
    }

    @Override
    public IntStream map(IntUnaryOperator mapper) {
        Function function = new Function() {
            @Override
            public int[] apply(int[] values2) {
                int[] out2 = new int[values2.length];
                for (int i = 0; i < values2.length; i++) {
                    out2[i] = mapper.apply(values2[i]);
                }
                return out2;
            }
        };
        functions.add(function);
        return new AsIntStream(values, functions);
    }

    private int[] applyAll() {
        int funSize = functions.size();
        List<Integer> out = new LinkedList<>();
        for (int i = 0; i < values.length; i++) {
            int[] target = new int[]{values[i]};
            for (int j = 0; j < funSize; j++) {
                target = functions.get(j).apply(target);
            }
            for (int k = 0; k < target.length; k++) {
                out.add(target[k]);
            }
        }
        return listToArray(out);
    }

    @Override
    public IntStream flatMap(IntToIntStreamFunction func) {

        Function function = new Function() {
            @Override
            public int[] apply(int[] values3) {
                List<Integer> out4 = new LinkedList<>();

                for (int i = 0; i < values3.length; i++) {
                    for (int cell : func.applyAsIntStream(values3[i]).toArray()) {
                        out4.add(cell);
                    }
                }
                out4 = new ArrayList<>(out4);
                int[] out5 = listToArray(out4);
                return out5;
            }
        };
        functions.add(function);
        return new AsIntStream(values, functions);
    }

    private int[] listToArray(List<Integer> list) {
        int[] out = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            out[i] = list.get(i);
        }
        return out;
    }

    @Override
    public int reduce(int identity, IntBinaryOperator op) {
        int out = identity;
        int[] values = applyAll();
        for (int i = 0; i < values.length; i++) {
            out = op.apply(out, values[i]);
        }
        return out;
    }

    @Override
    public int[] toArray() {
        return applyAll();
    }

    private void checkIsEmpty() {
        //FIXME redundant call. Cache or do something.
        if (applyAll().length == 0) {
            throw new IllegalArgumentException("stream is empty");
        }
    }


    private interface Function {
        int[] apply(int[] values);
    }

}
