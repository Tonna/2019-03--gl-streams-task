package ua.procamp.streams;

import org.junit.Before;
import org.junit.Test;
import ua.procamp.streams.stream.AsIntStream;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class CompareWithActualStreamAPIAndPerformanceTest {

    private int[] array;

    @Before
    public void init() {
        int bigInt = 20000;
        int[] values = new int[bigInt];
        for (int i = 0; i < bigInt; i++) {
            values[i] = i;
        }
        array = values;
    }

    @Test
    public void testCompareSum() {
        assertEquals(java.util.stream.IntStream.of(array).sum(),
                AsIntStream.of(array).sum());
    }

    @Test
    public void testCompareCount() {
        assertEquals(java.util.stream.IntStream.of(array).count(),
                AsIntStream.of(array).count());
    }

    @Test
    public void testCompareMax() {
        Integer expected =
                Optional.of(IntStream.of(array).max().getAsInt()).orElse(0);
        assertEquals(expected, AsIntStream.of(array).max());
    }

    @Test
    public void testCompareMin() {
        Integer expected =
                Optional.of(IntStream.of(array).min().getAsInt()).orElse(0);
        assertEquals(expected, AsIntStream.of(array).min());
    }

    @Test
    public void testCompareAverage() {
        Double expected =
                java.util.stream.IntStream.of(array).average().getAsDouble();
        assertEquals(expected, AsIntStream.of(array).average());
    }

    @Test
    public void testCompareReduceAsSum() {
        assertEquals(
                java.util.stream.IntStream.of(array).reduce(0, (a, b) -> a + b),
                AsIntStream.of(array).reduce(0, (a, b) -> a + b));
        assertEquals(
                java.util.stream.IntStream.of(array).sum(),
                AsIntStream.of(array).reduce(0, (a, b) -> a + b));
    }

    @Test
    public void testCompareReduceSubtraction() {
        assertEquals(
                java.util.stream.IntStream.of(array)
                        .reduce(10, (a, b) -> a - b),
                AsIntStream.of(array).reduce(10, (a, b) -> a - b));
    }

    @Test
    public void testCompareMapAndSum() {
        assertEquals(
                java.util.stream.IntStream.of(array).map(x -> x * 2).sum(),
                AsIntStream.of(array).map(x -> x * 2).sum());
    }

    @Test
    public void testCompareFilterAndSum() {
        assertEquals(
                java.util.stream.IntStream.of(array).filter(x -> x % 2 == 0).sum(),
                AsIntStream.of(array).filter(x -> x % 2 == 0).sum());
    }
}
