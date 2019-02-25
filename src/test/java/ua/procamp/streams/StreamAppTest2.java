package ua.procamp.streams;

import org.junit.Before;
import org.junit.Test;
import ua.procamp.streams.stream.AsIntStream;
import ua.procamp.streams.stream.IntStream;

import java.util.Date;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class StreamAppTest2 {

    private IntStream intStream;

    @Before
    public void init() {
        int[] intArr = {-1, 0, 1, 2, 3};
        intStream = AsIntStream.of(intArr);
    }

    @Test
    public void testStreamOperations() {
        System.out.println("streamOperations");
        int expResult = 42;
        int result = StreamApp.streamOperations(intStream);
        assertEquals(expResult, result);
    }

    @Test
    public void testStreamToArray() {
        System.out.println("streamToArray");
        int[] expResult = {-1, 0, 1, 2, 3};
        int[] result = StreamApp.streamToArray(intStream);
        assertArrayEquals(expResult, result);
    }

    @Test
    public void testStreamForEach() {
        System.out.println("streamForEach");
        String expResult = "-10123";
        String result = StreamApp.streamForEach(intStream);
        assertEquals(expResult, result);
    }

    @Test
    public void testStreamSum() {
        System.out.println("streamSum");
        //int bigInt = 20000000;
        int bigInt = 2000;
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
