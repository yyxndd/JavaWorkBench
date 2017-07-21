package com.moeee.streambenchmark;

import org.openjdk.jmh.annotations.*;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Title: StreamBenchmark3<br>
 * Description: <br>
 * Create DateTime: 2017年07月19日 上午11:07 <br>
 *
 * @author MoEee
 */
@State(Scope.Benchmark)
public class StreamBenchmark3 {
    int size = 10000000;
    List<PO> ints = null;

    public static void main(String[] args) {
        StreamBenchmark3 benchmark = new StreamBenchmark3();
        benchmark.setup1();
        System.out.println("forTrans is: " + benchmark.forTrans());
        System.out.println("forTransSimple is: " + benchmark.forTransSimple());
        System.out.println("iteratorTrans is: " + benchmark.iteratorTrans());
        System.out.println("forEachLambdaTrans is: " + benchmark.forEachLambdaTrans());
        System.out.println("streamTrans is: " + benchmark.streamTrans());
        System.out.println("parallelStreamTrans is: " + benchmark.parallelStreamTrans());
    }

    @Setup
    public void setup1() {
        ints = new ArrayList<>(size);
        populate(ints);
    }

    public void populate(List<PO> list) {
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            PO po = new PO();
            po.setInt1(random.nextInt(1000000));
            po.setInt2(random.nextInt(1000000));
            po.setInt3(random.nextInt(1000000));
            po.setInt4(random.nextInt(1000000));
            po.setInt5(random.nextInt(1000000));
            list.add(po);
        }
    }

    private VO translate(PO po) {
        VO result = new VO();
        result.setInt1(po.getInt1());
        result.setStr1(transStr(po.getInt1()));
        result.setInt2(po.getInt2());
        result.setStr2(transStr(po.getInt2()));
        result.setInt3(po.getInt3());
        result.setStr3(transStr(po.getInt3()));
        result.setInt4(po.getInt4());
        result.setStr4(transStr(po.getInt4()));
        result.setInt5(po.getInt5());
        result.setStr5(transStr(po.getInt5()));
        return result;
    }

    private String transStr(int i) {
        return i % 2 == 0 ? "偶数" : "奇数";
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(2)
    @Measurement(iterations = 5)
    @Warmup(iterations = 5)
    public List<VO> forTrans() {
        List<VO> vos = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            VO vo = translate(ints.get(i));
            vos.add(vo);
        }
        return vos;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(2)
    @Measurement(iterations = 5)
    @Warmup(iterations = 5)
    public List<VO> forTransSimple() {
        List<VO> vos = new ArrayList<>();
        for (PO n : ints) {
            VO vo = translate(n);
            vos.add(vo);
        }
        return vos;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(2)
    @Measurement(iterations = 5)
    @Warmup(iterations = 5)
    public List<VO> iteratorTrans() {
        List<VO> vos = new ArrayList<>();
        for (Iterator<PO> it = ints.iterator(); it.hasNext(); ) {
            VO vo = translate(it.next());
            vos.add(vo);
        }
        return vos;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(2)
    @Measurement(iterations = 5)
    @Warmup(iterations = 5)
    public List<VO> forEachLambdaTrans() {
        final Wrapper wrapper = new Wrapper();
        wrapper.inner = new ArrayList<>();
        ints.forEach(i -> helper(i, wrapper));
        return wrapper.inner;
    }

    public static class Wrapper {
        public List<VO> inner;
    }

    private List<VO> helper(PO i, Wrapper wrapper) {
        VO vo = translate(i);
        wrapper.inner.add(vo);
        return wrapper.inner;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(2)
    @Measurement(iterations = 5)
    @Warmup(iterations = 5)
    public List<VO> streamTrans() {
        List<VO> result = ints.stream().map(this::translate).collect(Collectors.toList());
        return result;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(2)
    @Measurement(iterations = 5)
    @Warmup(iterations = 5)
    public List<VO> parallelStreamTrans() {
        List<VO> result = ints.parallelStream().map(this::translate).collect(Collectors.toList());
        return result;
    }
}
