/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import stribog.Hash;

import java.util.*;
import static java.util.concurrent.TimeUnit.MILLISECONDS;


public class MyBenchmark {

    @State(Scope.Benchmark)
    public static class BenchTest {
        @Param({"0", "1000", "2000", "3000", "4000", "5000", "6000", "7000", "8000",
                "9000", "10000", "11000", "12000", "13000", "14000", "15000"})
        public int msgSize;
        public ArrayList<Integer> msg = new ArrayList<>();

        @Setup(Level.Trial)
        public void setUp() {
            for (var i = 0; i < msgSize; i++)
                msg.add(new Random().nextInt(256));
        }

        @Fork(value = 1, warmups = 1)
        @Warmup(iterations = 2, time = 500, timeUnit = MILLISECONDS)
        @org.openjdk.jmh.annotations.Benchmark
        @BenchmarkMode(Mode.AverageTime)
        @OutputTimeUnit(value = MILLISECONDS)
        @Measurement(iterations = 5, time = 200, timeUnit = MILLISECONDS)


        public void StribogBench(Blackhole blackhole, BenchTest state) {
            var hash = new Hash(512);
            var arr = new int[state.msg.size()];
            for (var i = 0; i < arr.length; i++)
                arr[i] = state.msg.get(i);
            blackhole.consume(hash.getHash(arr));
        }
    }
}