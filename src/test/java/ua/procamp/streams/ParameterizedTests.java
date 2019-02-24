package ua.procamp.streams;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ua.procamp.streams.stream.AsIntStream;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ParameterizedTests {

    private final int[] toArray;
    private final int sum;
    private int[] input;
    private Double average;
    private int max;
    private int min;
    private long count;
    private int[] flatMap1;
    private int[] flatMap2;
    private int reduce1;
    private int reduce2;

    public ParameterizedTests(int[] input,
                              double average,
                              int max,
                              int min,
                              long count,
                              int sum,
                              int[] toArray,
                              int[] flatMap1,
                              int[] flatMap2,
                              int reduce1,
                              int reduce2) {
        this.input = input;
        this.average = average;
        this.max = max;
        this.min = min;
        this.count = count;
        this.sum = sum;
        this.toArray = toArray;
        this.flatMap1 = flatMap1;
        this.flatMap2 = flatMap2;
        this.reduce1 = reduce1;
        this.reduce2 = reduce2;
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {
                        //initial input
                        new int[]{1, 2, 3},
                        //average
                        2,
                        //max
                        3,
                        //min
                        1,
                        //count
                        3,
                        //sum
                        6,
                        //toArray() output
                        new int[]{1, 2, 3},
                        //flatMap(x -> (x - 1, x + 2))
                        new int[]{0, 2, 1, 3, 2, 4},
                        //flatMap(x -> (x - 1, 22, x + 3))
                        new int[]{0, 22, 4, 1, 22, 5, 2, 22, 6},
                        //reduce(15, a - b)
                        21,
                        //reduce(0, a - b)
                        -6
                },
                //this case is overflow handling
                {new int[]{2000000035,2000000331},2000000183.0,2000000331,2000000035,2,-294966930,new int[]{2000000035,2000000331},new int[]{2000000034,2000000036,2000000330,2000000332}, new int[]{2000000034,22,2000000038,2000000330,22,2000000334},-294966915,294966930},


                //Generated test data
                {new int[]{115296, 62845, 115134, 170765, 77979, 108228, 173938, 58922, 70968, 116725, 194419, 79400, 177561, 40526, 194492}, 117146.53333333334, 194492, 40526, 15, 1757198, new int[]{115296, 62845, 115134, 170765, 77979, 108228, 173938, 58922, 70968, 116725, 194419, 79400, 177561, 40526, 194492}, new int[]{115295, 115297, 62844, 62846, 115133, 115135, 170764, 170766, 77978, 77980, 108227, 108229, 173937, 173939, 58921, 58923, 70967, 70969, 116724, 116726, 194418, 194420, 79399, 79401, 177560, 177562, 40525, 40527, 194491, 194493}, new int[]{115295, 22, 115299, 62844, 22, 62848, 115133, 22, 115137, 170764, 22, 170768, 77978, 22, 77982, 108227, 22, 108231, 173937, 22, 173941, 58921, 22, 58925, 70967, 22, 70971, 116724, 22, 116728, 194418, 22, 194422, 79399, 22, 79403, 177560, 22, 177564, 40525, 22, 40529, 194491, 22, 194495}, 1757213, -1757198},
                {new int[]{180394, 125046, 102030, 55901, 87791, 123470, 120645, 58227, 34712, 182896}, 107111.2, 182896, 34712, 10, 1071112, new int[]{180394, 125046, 102030, 55901, 87791, 123470, 120645, 58227, 34712, 182896}, new int[]{180393, 180395, 125045, 125047, 102029, 102031, 55900, 55902, 87790, 87792, 123469, 123471, 120644, 120646, 58226, 58228, 34711, 34713, 182895, 182897}, new int[]{180393, 22, 180397, 125045, 22, 125049, 102029, 22, 102033, 55900, 22, 55904, 87790, 22, 87794, 123469, 22, 123473, 120644, 22, 120648, 58226, 22, 58230, 34711, 22, 34715, 182895, 22, 182899}, 1071127, -1071112},
                {new int[]{129927, 5446}, 67686.5, 129927, 5446, 2, 135373, new int[]{129927, 5446}, new int[]{129926, 129928, 5445, 5447}, new int[]{129926, 22, 129930, 5445, 22, 5449}, 135388, -135373},
                {new int[]{136187, 189167, 13781, 49557, 186918, 137179, 116750, 174817, 32747, 166901, 80897, 14260}, 108263.41666666667, 189167, 13781, 12, 1299161, new int[]{136187, 189167, 13781, 49557, 186918, 137179, 116750, 174817, 32747, 166901, 80897, 14260}, new int[]{136186, 136188, 189166, 189168, 13780, 13782, 49556, 49558, 186917, 186919, 137178, 137180, 116749, 116751, 174816, 174818, 32746, 32748, 166900, 166902, 80896, 80898, 14259, 14261}, new int[]{136186, 22, 136190, 189166, 22, 189170, 13780, 22, 13784, 49556, 22, 49560, 186917, 22, 186921, 137178, 22, 137182, 116749, 22, 116753, 174816, 22, 174820, 32746, 22, 32750, 166900, 22, 166904, 80896, 22, 80900, 14259, 22, 14263}, 1299176, -1299161},
                {new int[]{144683, 51033}, 97858.0, 144683, 51033, 2, 195716, new int[]{144683, 51033}, new int[]{144682, 144684, 51032, 51034}, new int[]{144682, 22, 144686, 51032, 22, 51036}, 195731, -195716},
                {new int[]{9308, 177116, 90856, 157891, 103008, 188465, 173278, 119044, 23543, 28500}, 107100.9, 188465, 9308, 10, 1071009, new int[]{9308, 177116, 90856, 157891, 103008, 188465, 173278, 119044, 23543, 28500}, new int[]{9307, 9309, 177115, 177117, 90855, 90857, 157890, 157892, 103007, 103009, 188464, 188466, 173277, 173279, 119043, 119045, 23542, 23544, 28499, 28501}, new int[]{9307, 22, 9311, 177115, 22, 177119, 90855, 22, 90859, 157890, 22, 157894, 103007, 22, 103011, 188464, 22, 188468, 173277, 22, 173281, 119043, 22, 119047, 23542, 22, 23546, 28499, 22, 28503}, 1071024, -1071009},
                {new int[]{170331, 141973}, 156152.0, 170331, 141973, 2, 312304, new int[]{170331, 141973}, new int[]{170330, 170332, 141972, 141974}, new int[]{170330, 22, 170334, 141972, 22, 141976}, 312319, -312304},
                {new int[]{170061, 146999, 168429, 116397, 170857, 172956, 64458, 11350, 141626, 54075}, 121720.8, 172956, 11350, 10, 1217208, new int[]{170061, 146999, 168429, 116397, 170857, 172956, 64458, 11350, 141626, 54075}, new int[]{170060, 170062, 146998, 147000, 168428, 168430, 116396, 116398, 170856, 170858, 172955, 172957, 64457, 64459, 11349, 11351, 141625, 141627, 54074, 54076}, new int[]{170060, 22, 170064, 146998, 22, 147002, 168428, 22, 168432, 116396, 22, 116400, 170856, 22, 170860, 172955, 22, 172959, 64457, 22, 64461, 11349, 22, 11353, 141625, 22, 141629, 54074, 22, 54078}, 1217223, -1217208},
                {new int[]{133827, 29698, 174458, 116279, 62125, 47619, 153935}, 102563.0, 174458, 29698, 7, 717941, new int[]{133827, 29698, 174458, 116279, 62125, 47619, 153935}, new int[]{133826, 133828, 29697, 29699, 174457, 174459, 116278, 116280, 62124, 62126, 47618, 47620, 153934, 153936}, new int[]{133826, 22, 133830, 29697, 22, 29701, 174457, 22, 174461, 116278, 22, 116282, 62124, 22, 62128, 47618, 22, 47622, 153934, 22, 153938}, 717956, -717941}
        });
    }

    @Test
    public void testAverage() {
        assertEquals(average, AsIntStream.of(input).average());
    }

    @Test
    public void      testMax() {
        assertEquals((int) max, (int) AsIntStream.of(input).max());
    }

    @Test
    public void testMin() {
        assertEquals((int) min, (int) AsIntStream.of(input).min());
    }

    @Test
    public void testCount() {
        assertEquals((int) count, (int) AsIntStream.of(input).count());
    }

    @Test
    public void testSum() {
        assertEquals(sum, AsIntStream.of(input).sum());
    }

    @Test
    public void testToArray() {
        assertArrayEquals(toArray, AsIntStream.of(input).toArray());
    }

    @Test
    public void testFlatMap1() {
        assertArrayEquals(flatMap1, AsIntStream.of(input).flatMap(x -> AsIntStream.of(x - 1, x + 1)).toArray());
    }

    @Test
    public void testFlatMap2() {
        assertArrayEquals(flatMap2, AsIntStream.of(input).flatMap(x -> AsIntStream.of(x - 1, 22, x + 3)).toArray());
    }

    @Test
    public void testReduce1() {
        assertEquals(reduce1, AsIntStream.of(input).reduce(15, (a, b) -> a + b));
    }

    @Test
    public void testReduce2() {
        assertEquals(reduce2, AsIntStream.of(input).reduce(0, (a, b) -> a - b));
    }

}