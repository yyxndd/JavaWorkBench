package com.moeee.streambenchmark;

import org.openjdk.jmh.annotations.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Title: StreamBenchmark2<br>
 * Description: <br>
 * Create DateTime: 2017年07月19日 上午10:46 <br>
 *
 * @author MoEee
 */
@State(Scope.Benchmark)
public class StreamBenchmark2 {
    int size = 10000000;
    List<Integer> integers = null;

    public static void main(String[] args) {
        StreamBenchmark2 benchmark = new StreamBenchmark2();
        benchmark.setup1();
        System.out.println("forTrans max is: " + benchmark.forTrans());
        System.out.println("forTransSimple max is: " + benchmark.forTransSimple());
        System.out.println("iteratorTrans max is: " + benchmark.iteratorTrans());
        System.out.println("forEachLambdaTrans max is: " + benchmark.forEachLambdaTrans());
        System.out.println("streamTrans max is: " + benchmark.streamTrans());
        System.out.println("parallelStreamTrans max is: " + benchmark.parallelStreamTrans());
    }

    @Setup
    public void setup1() {
        integers = new ArrayList<>(size);
        populate(integers);
    }

    public void populate(List<Integer> list) {
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            list.add(random.nextInt(100));
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(2)
    @Measurement(iterations = 5)
    @Warmup(iterations = 5)
    public int forTrans() {
        int max = 0;
        for (int i = 0; i < size; i++) {
            max = Math.addExact(max, integers.get(i));
        }
        return max;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(2)
    @Measurement(iterations = 5)
    @Warmup(iterations = 5)
    public int forTransSimple() {
        int max = 0;
        for (Integer n : integers) {
            max = Math.addExact(max, n);
        }
        return max;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(2)
    @Measurement(iterations = 5)
    @Warmup(iterations = 5)
    public int iteratorTrans() {
        int max = 0;
        for (Iterator<Integer> it = integers.iterator(); it.hasNext(); ) {
            max = Math.addExact(max, it.next());
        }
        return max;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(2)
    @Measurement(iterations = 5)
    @Warmup(iterations = 5)
    public int forEachLambdaTrans() {
        final Wrapper wrapper = new Wrapper();
        wrapper.inner = 0;
        integers.forEach(i -> helper(i, wrapper));
        return wrapper.inner.intValue();
    }

    public static class Wrapper {
        public Integer inner;
    }

    private int helper(int i, Wrapper wrapper) {
        wrapper.inner = Math.addExact(i, wrapper.inner);
        return wrapper.inner;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(2)
    @Measurement(iterations = 5)
    @Warmup(iterations = 5)
    public int streamTrans() {
        int max = integers.stream().mapToInt(i->i.intValue()).sum();
        return max;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(2)
    @Measurement(iterations = 5)
    @Warmup(iterations = 5)
    public int parallelStreamTrans() {
        int max = integers.parallelStream().mapToInt(i->i.intValue()).sum();
        return max;
    }
}
