package ua.procamp.streams;


import org.junit.Before;
import org.junit.Test;
import ua.procamp.streams.stream.AsIntStream;
import ua.procamp.streams.stream.IntStream;

public class EdgeCasesTest {

    private IntStream emptyStream;
    private IntStream emptyAfterFlatMapStream;
    private IntStream emptyAfterFilter;

    @Before
    public void init() {
        int[] emptyArr = {};
        emptyStream = AsIntStream.of(emptyArr);
        int[] arr = new int[]{1, 2, 3};

        //those streams are created to to double check
        //there might be cases when flatMap of filter
        //won't return anything
        emptyAfterFlatMapStream = AsIntStream.of(arr)
                .map(x -> x * 100500)
                .flatMap(value -> AsIntStream.of());

        emptyAfterFilter = AsIntStream.of(arr).filter(x -> x < 0);

    }

    @Test(expected = IllegalArgumentException.class)
    public void maxEmptyStreamTest() {
        emptyStream.max();
        emptyAfterFlatMapStream.max();
        emptyAfterFilter.max();
    }

    @Test(expected = IllegalArgumentException.class)
    public void minEmptyStreamTest() {
        emptyStream.min();
        emptyAfterFlatMapStream.min();
        emptyAfterFilter.min();
    }

    @Test(expected = IllegalArgumentException.class)
    public void averageEmptyStreamTest() {
        emptyStream.average();
        emptyAfterFlatMapStream.average();
        emptyAfterFilter.average();
    }

    @Test(expected = IllegalArgumentException.class)
    public void sumEmptyStreamTest() {
        emptyStream.sum();
        emptyAfterFlatMapStream.sum();
        emptyAfterFilter.sum();
    }

    //This requirement wasn't present in original task
    //but this is how real StreamAPI works
    /*
      Nevermind. Current implementation creates new stream
      each time non-terminating method is called. Passing state
      between streams isn't an option.
    */
    /*
    @Test(expected = IllegalStateException.class)
    public void streamCannotBeUsedMoreThanOnceTest() {
        IntStream stream = AsIntStream.of(1, 2, 3);
        stream.map(x -> x * 25).sum();
        //should cause exception?
        stream.map(x -> x * 25);
    }*/
}
