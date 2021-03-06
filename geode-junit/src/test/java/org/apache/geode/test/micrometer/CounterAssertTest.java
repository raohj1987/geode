/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.geode.test.micrometer;

import static org.apache.geode.test.micrometer.MicrometerAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.micrometer.core.instrument.Counter;
import org.assertj.core.api.Condition;
import org.junit.Test;

public class CounterAssertTest {
  @SuppressWarnings("unchecked")
  private final Condition<Double> countCondition = mock(Condition.class, "count condition");
  private final Counter counter = mock(Counter.class);

  @Test
  public void hasCount_doesNotThrow_ifConditionAcceptsCount() {
    double acceptableCount = 92.0;

    when(counter.count()).thenReturn(acceptableCount);
    when(countCondition.matches(acceptableCount)).thenReturn(true);

    assertThatCode(() -> assertThat(counter).hasCount(countCondition))
        .doesNotThrowAnyException();
  }

  @Test
  public void hasCount_failsDescriptively_ifConditionRejectsCount() {
    double unacceptableCount = 92.0;

    when(counter.count()).thenReturn(unacceptableCount);
    when(countCondition.matches(unacceptableCount)).thenReturn(false);

    assertThatThrownBy(() -> assertThat(counter).hasCount(countCondition))
        .isInstanceOf(AssertionError.class)
        .hasMessageContaining(countCondition.toString())
        .hasMessageContaining(String.valueOf(unacceptableCount));
  }
}
