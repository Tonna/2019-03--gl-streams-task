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
        emptyStream = AsIntStream.of();

        //those streams are created to to double check
        //there might be cases when flatMap of filter
        //won't return anything
        emptyAfterFlatMapStream = AsIntStream.of(1, 2, 3)
                .map(x -> x * 100500)
                .flatMap(value -> AsIntStream.of());

        emptyAfterFilter = AsIntStream.of(1, 2, 3).filter(x -> x < 0);

    }

    @Test(expected = NullPointerException.class)
    public void checkNullInputCausesExceptionTest() {
        AsIntStream.of(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void maxEmptyStreamTest() {
        emptyStream.max();
    }

    @Test(expected = IllegalArgumentException.class)
    public void minEmptyStreamTest() {
        emptyStream.min();
    }

    @Test(expected = IllegalArgumentException.class)
    public void averageEmptyStreamTest() {
        emptyStream.average();
    }

    @Test(expected = IllegalArgumentException.class)
    public void sumEmptyStreamTest() {
        emptyStream.sum();
    }

    @Test(expected = IllegalArgumentException.class)
    public void maxEmptyAfterFlatMapStreamTest() {
        emptyAfterFlatMapStream.max();
    }

    @Test(expected = IllegalArgumentException.class)
    public void minEmptyAfterFlatMapStreamTest() {
        emptyAfterFlatMapStream.min();
    }

    @Test(expected = IllegalArgumentException.class)
    public void averageEmptyAfterFlatMapStreamTest() {
        emptyAfterFlatMapStream.average();
    }

    @Test(expected = IllegalArgumentException.class)
    public void sumEmptyAfterFlatMapStreamTest() {
        emptyAfterFlatMapStream.sum();
    }

    @Test(expected = IllegalArgumentException.class)
    public void maxEmptyAfterFilterStreamTest() {
        emptyAfterFilter.max();
    }

    @Test(expected = IllegalArgumentException.class)
    public void minEmptyAfterFilterStreamTest() {
        emptyAfterFilter.min();
    }

    @Test(expected = IllegalArgumentException.class)
    public void averageEmptyAfterFilterStreamTest() {
        emptyAfterFilter.average();
    }

    @Test(expected = IllegalArgumentException.class)
    public void sumEmptyAfterFilterStreamTest() {
        emptyAfterFilter.sum();
    }


    /*
       This requirement wasn't present in original task
       but this is how real StreamAPI works
       Current implementation creates new stream
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
    }

    @Test(expected = IllegalStateException.class)
    public void nonTerminatingOperationOnUsedStreamTest() {
        emptyAfterFilter.filter(x -> x > 0);
    }
    */
}
