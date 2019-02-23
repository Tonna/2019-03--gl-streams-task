package ua.procamp.streams;

import org.junit.Before;
import org.junit.Test;
import ua.procamp.streams.stream.AsIntStream;

import java.util.Date;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class CompareWithActualStreamAPI {


    private int[] array;

    @Before
    public void init() {
        int bigInt = 200000;
        int[] values = new int[bigInt];
        for (int i = 0; i < bigInt; i++) {
            values[i] = i;
        }
        array = values;
    }

    @Test
    public void testCompareSum() {
        assertEquals(java.util.stream.IntStream.of(array).sum(), AsIntStream.of(array).sum());
    }


    @Test
    public void testCompareCount() {
        assertEquals(java.util.stream.IntStream.of(array).count(), AsIntStream.of(array).count());
    }

    @Test
    public void testCompareMax() {
        Integer expected = Optional.of(IntStream.of(array).max().getAsInt()).orElse(0);
        assertEquals(expected, AsIntStream.of(array).max());
    }

    @Test
    public void testCompareMin() {
        Integer expected = Optional.of(IntStream.of(array).min().getAsInt()).orElse(0);
        assertEquals(expected, AsIntStream.of(array).min());
    }

    @Test
    public void testCompareAverage() {
        assertEquals(java.util.stream.IntStream.of(array).average(), AsIntStream.of(array).average());
    }

    @Test
    public void testCompareReduceAsSum() {
        assertEquals(java.util.stream.IntStream.of(array).reduce(0, (a, b) -> a + b), AsIntStream.of(array).reduce(0, (a, b) -> a + b));
        assertEquals(java.util.stream.IntStream.of(array).sum(), AsIntStream.of(array).reduce(0, (a, b) -> a + b));
    }

    @Test
    public void testCompareReduceSubtraction() {
        assertEquals(java.util.stream.IntStream.of(array).reduce(10, (a, b) -> a - b), AsIntStream.of(array).reduce(10, (a, b) -> a - b));
    }

    @Test
    public void testCompareMapAndSum() {
        assertEquals(java.util.stream.IntStream.of(array).map(x -> x * 2).sum(), AsIntStream.of(array).map(x -> x * 2).sum());
    }

    @Test
    public void testCompareFilterAndSum() {
        assertEquals(java.util.stream.IntStream.of(array).filter(x -> x % 2 == 0).sum(), AsIntStream.of(array).filter(x -> x % 2 == 0).sum());
    }


    @Test
    public void testStreamSum() {
        System.out.println("streamSum");
        //int bigInt = 20000000;
        int bigInt = 200000;
        int[] values = new int[bigInt];
        for (int i = 0; i < bigInt; i++) {
            values[i] = i;
        }
        long expResult = 2000000001000000000L;
        long start1 = new Date().getTime();
        long result = StreamApp.streamOperations(AsIntStream.of(values));
        long start2 = new Date().getTime();
        //assertEquals(expResult, result);

        long start3 = new Date().getTime();
        int c = java.util.stream.IntStream.of(values)
                .filter(x -> x > 0) // 1, 2, 3
                .map(x -> x * x) // 1, 4, 9
                .flatMap(x -> java.util.stream.IntStream.of(x - 1, x, x + 1)) // 0, 1, 2, 3, 4, 5, 8, 9, 10
                .reduce(0, (a, b) -> a + b); // 42
        long start4 = new Date().getTime();
        System.out.println(result + " " + (start2 - start1));
        System.out.println(c + " " + (start4 - start3));

    }


}
