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

package org.apache.geode.internal.cache;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import org.apache.geode.cache.Region;

public class RegionFactoryImplTest {
  private InternalCache cache;

  @Before
  public void init() {
    cache = mock(InternalCache.class);
  }

  @Test
  public void createWithInternalRegionArgumentsCallsCreateVMRegion() throws Exception {
    RegionFactoryImpl<Object, Object> regionFactory = new RegionFactoryImpl<>(cache);
    InternalRegionArguments internalRegionArguments = mock(InternalRegionArguments.class);
    regionFactory.setInternalRegionArguments(internalRegionArguments);
    String regionName = "regionName";

    regionFactory.create(regionName);

    verify(cache).createVMRegion(same(regionName), any(), same(internalRegionArguments));
  }

  @Test
  public void createWithInternalRegionArgumentsReturnsRegion() throws Exception {
    RegionFactoryImpl<Object, Object> regionFactory = new RegionFactoryImpl<>(cache);
    InternalRegionArguments internalRegionArguments = mock(InternalRegionArguments.class);
    regionFactory.setInternalRegionArguments(internalRegionArguments);
    String regionName = "regionName";
    Region<Object, Object> expectedRegion = mock(Region.class);
    when(cache.createVMRegion(same(regionName), any(), same(internalRegionArguments)))
        .thenReturn(expectedRegion);

    Region<Object, Object> region = regionFactory.create(regionName);

    assertThat(region).isSameAs(expectedRegion);
  }

  @Test
  public void createWithoutInternalRegionArgumentsCallsCreateRegion() throws Exception {
    RegionFactoryImpl<Object, Object> regionFactory = new RegionFactoryImpl<>(cache);
    regionFactory.setInternalRegionArguments(null);
    String regionName = "regionName";

    regionFactory.create(regionName);

    verify(cache).createRegion(same(regionName), any());
  }
}
