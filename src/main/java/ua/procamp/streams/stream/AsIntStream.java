package ua.procamp.streams.stream;

import ua.procamp.streams.function.*;

import java.util.*;

public class AsIntStream implements IntStream {

    private final List<Integer> list;

    private AsIntStream() {
        list = Collections.EMPTY_LIST;
    }

    private AsIntStream(List<Integer> values) {
        this.list = values;
    }

    public static IntStream of(int... values) {
        List<Integer> list = new ArrayList<>();
        for (int value : values) {
            list.add(value);
        }
        return new AsIntStream(list);
    }

    @Override
    public Double average() {
        return Double.valueOf(sum() / list.size());

    }

    @Override
    public Integer max() {
        return Collections.max(list);
    }

    @Override
    public Integer min() {
        return Collections.min(list);
    }

    @Override
    public long count() {
        return list.size();
    }

    @Override
    public int sum() {
        int sum = 0;
        for (Integer i : list) {
            sum = sum + i;
        }
        return sum;
    }

    @Override
    public IntStream filter(IntPredicate predicate) {
        int[] out = new int[list.size()];
        int count  = 0;
        for (Integer i : list) {
            if(predicate.test(i)){
                out[count] = i;
                count = count + 1;
            }
        }
        return AsIntStream.of(out);
    }

    @Override
    public void forEach(IntConsumer action) {
        for (Integer i : list) {
            action.accept(i);
        }

    }

    @Override
    public IntStream map(IntUnaryOperator mapper) {
        List<Integer> out = new LinkedList<>();
        for (Integer i : list) {
            out.add(mapper.apply(i));
        }
        return new AsIntStream(out);
    }

    @Override
    public IntStream flatMap(IntToIntStreamFunction func) {
        List<Integer> out = new LinkedList<>();
        for (Integer i : list) {
            for (int cell : func.applyAsIntStream(i).toArray()) {
                out.add(cell);
            }
        }
        return new AsIntStream(out);
    }

    @Override
    public int reduce(int identity, IntBinaryOperator op) {
        int out = identity;
        for (Integer i : list) {
            out = op.apply(out, i);
        }
        return out;
    }

    @Override
    public int[] toArray() {
        int[] out = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            out[i] = list.get(i);
        }
        return out;
    }

}
