package ua.procamp.streams.stream;

import ua.procamp.streams.function.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class AsIntStream implements IntStream {

    //TODO investigate and make sure
    // 1. there are no extra objects created
    // 2. using same stream multiple times won't cause troubles
    private final int[] values;
    private List<Function> functions = new ArrayList<>();

    private AsIntStream(int[] values) {
        //original StreamAPI throws NPE in case if null passed
        if (values == null) {
            throw new NullPointerException("null value passed");
        }
        //Making copy of array just in case
        this.values = Arrays.copyOf(values, values.length);
    }

    private AsIntStream(int[] values, List<Function> functions) {
        this(values);
        //Making copy of list just in case
        this.functions = new ArrayList<>(functions);
    }

    public static IntStream of(int... values) {
        return new AsIntStream(values);
    }

    @Override
    public Double average() {
        int[] processed = applyAll();
        checkIsEmpty(processed);
        long sum = 0;
        for (int i = 0; i < processed.length; i++) {
            sum = sum + processed[i];
        }
        return Double.valueOf(sum) / processed.length;
    }

    @Override
    public Integer max() {

        int[] processed = applyAll();
        checkIsEmpty(processed);
        int max = processed[0];
        for (int i = 1; i < processed.length; i++) {
            if (processed[i] > max) {
                max = processed[i];
            }
        }
        return max;
    }

    @Override
    public Integer min() {
        int[] processed = applyAll();
        checkIsEmpty(processed);
        int min = processed[0];
        for (int i = 1; i < processed.length; i++) {
            if (processed[i] < min) {
                min = processed[i];
            }
        }
        return min;
    }

    @Override
    public long count() {
        //FIXME: If it would be real stream,
        // it wouldn't be limited to arrays length (max integer)?
        return (long) applyAll().length;
    }

    @Override
    public int sum() {
        int[] processed = applyAll();
        checkIsEmpty(processed);
        long sum = 0;
        for (int i = 0; i < processed.length; i++) {
            sum = sum + processed[i];
        }
        return (int) sum;
    }

    @Override
    public IntStream filter(IntPredicate predicate) {
        functions.add(values -> {
            //TODO replace arrays with LinkedList if it won't hurt performance?
            int[] intermediate = new int[values.length];
            //using the array we track position of values
            // that weren't filtered out, i.e. passed validation
            boolean[] isValidArr = new boolean[values.length];
            for (int i = 0; i < values.length; i++) {
                if (predicate.test(values[i])) {
                    intermediate[i] = values[i];
                    isValidArr[i] = true;
                }
            }
            return getValidValues(intermediate, isValidArr);

        });
        return new AsIntStream(values, functions);
    }

    private int[] getValidValues(int[] intermediate, boolean[] isValidArr) {
        //count how many values passed
        // and how big array for output should be
        int numOfValid = 0;
        for (boolean valid : isValidArr) {
            if (valid) {
                numOfValid = numOfValid + 1;
            }
        }
        if (numOfValid > 0) {
            int[] out = new int[numOfValid];
            //iterate over array containing both
            //valid and non-initialized zero values
            //put only valid to output array
            int insertCount = 0;
            for (int i = 0; i < intermediate.length; i++) {
                if (isValidArr[i]) {
                    out[insertCount] = intermediate[i];
                    insertCount = insertCount + 1;
                }
            }
            return out;
        } else {
            //all values were filtered out - return nothing
            return new int[]{};
        }
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
        functions.add(values -> {
            int[] out = new int[values.length];
            for (int i = 0; i < values.length; i++) {
                out[i] = mapper.apply(values[i]);
            }
            return out;
        });
        return new AsIntStream(values, functions);
    }

    private int[] applyAll() {
        //how many functions we have to apply in total
        int funSize = functions.size();

        //container for results of all function calls
        // get estimated total size of all returned array
        int[] out = new int[getMaxStreamSize()];
        int count = 0;
        //Iterating over each value of initial input
        for (int i = 0; i < values.length; i++) {
            int[] target = new int[]{values[i]};
            //Iterating over stored functions
            //and applying to each value
            for (int j = 0; j < funSize; j++) {
                target = functions.get(j).apply(target);
                if (target.length == 0) {
                    break;
                }
            }
            //storing every result from function
            //to array with all results
            for (int k = 0; k < target.length; k++) {
                out[k + count] = target[k];
            }
            count = count + target.length;
        }
        //returned only filled part of array
        return Arrays.copyOfRange(out, 0, count);
    }

    @Override
    public IntStream flatMap(IntToIntStreamFunction func) {
        functions.add(values -> {

            //TODO Optimize by making further calls functional also?
            List<Integer> out = new LinkedList<>();

            for (int i = 0; i < values.length; i++) {
                for (int cell : func.applyAsIntStream(values[i]).toArray()) {
                    out.add(cell);
                }
            }
            out = new ArrayList<>(out);
            return listToArray(out);
        });
        return new AsIntStream(values, functions);
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

    /*
      here we are estimating total length of arrays
      returned by all functions
      This is answer to the questions:
      - how many values will be filtered out?
      - how big array flatMap returns?

      We get answer to them by having dry run of stored functions.
      We cannot say minimal number of values coming from Stream
      but we can tell maximum number
    */
    private int getMaxStreamSize() {
        //default value
        int totalLength = 1;
        //dry run of functions chain
        for (Function f : functions) {
            int length = f.apply(new int[]{0}).length;
            //if not everything is filtered out
            if (length != 0) {
                //multiplication if used to handle multiple flatMaps
                totalLength = length * totalLength;
            }
        }
        //from single int chain of functions produces multiple ints
        //that is multiplied on input size
        return totalLength * values.length;
    }

    private int[] listToArray(List<Integer> list) {
        int[] out = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            out[i] = list.get(i);
        }
        return out;
    }

    private void checkIsEmpty(int[] array) {
        if (array.length == 0) {
            throw new IllegalArgumentException("stream is empty");
        }
    }

    private interface Function {
        int[] apply(int[] values);
    }

}
