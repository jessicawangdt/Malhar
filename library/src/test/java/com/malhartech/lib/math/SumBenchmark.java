/**
 * Copyright (c) 2012-2012 Malhar, Inc. All rights reserved.
 */
package com.malhartech.lib.math;

import com.malhartech.engine.TestSink;
import java.util.HashMap;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Performance tests for {@link com.malhartech.lib.math.MapSum}<p>
 *
 */
public class SumBenchmark
{
  private static Logger log = LoggerFactory.getLogger(SumBenchmark.class);

  /**
   * Test oper logic emits correct results
   */
  @Test
  @Category(com.malhartech.annotation.PerformanceTestCategory.class)
  public void testNodeSchemaProcessing() throws InterruptedException
  {
    MapSum<String, Double> oper = new MapSum<String, Double>();
    oper.setType(Double.class);
    TestSink sumSink = new TestSink();
    TestSink countSink = new TestSink();
    TestSink averageSink = new TestSink();
    oper.sum.setSink(sumSink);
    oper.count.setSink(countSink);
    oper.average.setSink(averageSink);

    int numTuples = 100000000;
    oper.beginWindow(0);
    HashMap<String, Double> input = new HashMap<String, Double>();

    for (int i = 0; i < numTuples; i++) {
      input.put("a", 2.0);
      input.put("b", 20.0);
      input.put("c", 10.0);
      oper.data.process(input);
    }
    oper.endWindow();

    HashMap<String, Double> dhash = (HashMap<String, Double>)sumSink.collectedTuples.get(0);
    HashMap<String, Double> ahash = (HashMap<String, Double>)averageSink.collectedTuples.get(0);
    HashMap<String, Integer> chash = (HashMap<String, Integer>)countSink.collectedTuples.get(0);
    log.debug(String.format("\nBenchmark sums for %d key/val pairs", numTuples * 3));

    log.debug(String.format("\nFor sum expected(%d,%d,%d), got(%.1f,%.1f,%.1f);",
                            2 * numTuples, 20 * numTuples, 10 * numTuples,
                            dhash.get("a"), dhash.get("b"), dhash.get("c")));

    log.debug(String.format("\nFor average expected(2,20,10), got(%d,%d,%d);",
                            ahash.get("a").intValue(), ahash.get("b").intValue(), ahash.get("c").intValue()));

    log.debug(String.format("\nFor count expected(%d,%d,%d), got(%d,%d,%d);",
                            numTuples, numTuples, numTuples,
                            chash.get("a"), chash.get("b"), chash.get("c")));
  }
}
