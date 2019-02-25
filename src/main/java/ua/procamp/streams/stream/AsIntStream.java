package ua.procamp.streams.stream;

import ua.procamp.streams.function.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AsIntStream implements IntStream {

    private final int[] values;
    private boolean terminated = false;

    private AsIntStream() {
        values = new int[0];
    }

    private AsIntStream(int[] values) {
        if (values == null) {
            throw new NullPointerException("null array passed");
        }
        this.values = values;
    }

    public static IntStream of(int... values) {
        return new AsIntStream(values);
    }

    @Override
    public Double average() {
        checkIfTerminated();
        checkIsEmpty();
        terminateStream();
        return Double.valueOf(internalSum()) / values.length;
    }

    @Override
    public Integer max() {
        checkIfTerminated();
        checkIsEmpty();
        int max = values[0];
        for (int i = 1; i < values.length; i++) {
            if (values[i] > max) {
                max = values[i];
            }
        }
        terminateStream();
        return max;
    }

    @Override
    public Integer min() {
        checkIfTerminated();
        checkIsEmpty();
        int min = values[0];
        for (int i = 1; i < values.length; i++) {
            if (values[i] < min) {
                min = values[i];
            }
        }
        terminateStream();
        return min;
    }

    @Override
    public long count() {
        checkIfTerminated();
        terminateStream();
        //FIXME: If it would be real stream, it wouldn't be limited to arrays length
        return (long) values.length;
    }

    @Override
    public int sum() {
        checkIfTerminated();
        checkIsEmpty();
        terminateStream();
        return (int) internalSum();
    }

    public long internalSum() {
        long sum = 0;
        for (int i = 0; i < values.length; i++) {
            sum = sum + values[i];
        }
        return sum;
    }

    @Override
    public IntStream filter(IntPredicate predicate) {
        checkIfTerminated();
        int[] out = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            if (predicate.test(values[i])) {
                out[i] = values[i];
            }
        }
        return AsIntStream.of(out);
    }

    @Override
    public void forEach(IntConsumer action) {
        checkIfTerminated();
        for (int i = 0; i < values.length; i++) {
            action.accept(values[i]);
        }
        terminateStream();
    }

    @Override
    public IntStream map(IntUnaryOperator mapper) {
        checkIfTerminated();
        int[] out = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            out[i] = mapper.apply(values[i]);
        }
        return new AsIntStream(out);
    }

    @Override
    public IntStream flatMap(IntToIntStreamFunction func) {
        checkIfTerminated();
        List<Integer> out = new LinkedList<>();
        for (int i = 0; i < values.length; i++) {
            for (int cell : func.applyAsIntStream(values[i]).toArray()) {
                out.add(cell);
            }
        }
        out = new ArrayList<>(out);
        int[] out2 = new int[out.size()];
        for (int i = 0; i < out.size(); i++) {
            out2[i] = out.get(i);
        }

        return new AsIntStream(out2);
    }

    @Override
    public int reduce(int identity, IntBinaryOperator op) {
        checkIfTerminated();
        int out = identity;
        for (int i = 0; i < values.length; i++) {
            out = op.apply(out, values[i]);
        }
        terminateStream();
        return out;
    }

    @Override
    public int[] toArray() {
        checkIfTerminated();
        terminateStream();
        return values;
    }


    private void checkIsEmpty() {
        if (values.length == 0) {
            throw new IllegalArgumentException("stream is empty");
        }
    }

    private void checkIfTerminated(){
        if(terminated){
            throw new IllegalStateException("stream was already used");
        }
    }

    private void terminateStream(){
        this.terminated = true;
    }
}
