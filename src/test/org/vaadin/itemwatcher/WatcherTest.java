/*
 * Copyright 2012 Charles Anthony <charles@backstage.org.uk>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.vaadin.itemwatcher;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class WatcherTest {

  protected Watcher watcher;

  @Before
  public void setUp() throws Exception {
    watcher = new Watcher();
  }

  @Test
  public void testAddListener() throws Exception {
    assertTrue(watcher.listeners.isEmpty());

    watcher.addListener(null);
    assertTrue(watcher.listeners.isEmpty());

    ItemChangedListener listener = mock(ItemChangedListener.class);
    watcher.addListener(listener);
    assertFalse(watcher.listeners.isEmpty());
    assertTrue(watcher.listeners.contains(listener));

    watcher.addListener(listener);
    assertEquals(1, watcher.listeners.size());
  }

  @Test
  public void testRemoveListener() throws Exception {

    ItemChangedListener listener1 = mock(ItemChangedListener.class);
    ItemChangedListener listener2 = mock(ItemChangedListener.class);
    watcher.addListener(listener1);

    assertEquals(1, watcher.listeners.size());
    watcher.removeListener(listener2);
    assertEquals(1, watcher.listeners.size());
    watcher.removeListener(null);
    assertEquals(1, watcher.listeners.size());

    watcher.removeListener(listener1);
    assertEquals(0, watcher.listeners.size());
  }

  @Test
  public void testNotifyItemChanged() {
    ItemChangedListener listener1 = mock(ItemChangedListener.class);
    ItemChangedListener listener2 = mock(ItemChangedListener.class);
    ItemChangedEvent event = mock(ItemChangedEvent.class);

    watcher.notifyItemChanged(event);
    verify(listener1, never()).itemChanged(event);
    verify(listener2, never()).itemChanged(event);

    watcher.addListener(listener1);
    watcher.addListener(listener2);
    watcher.notifyItemChanged(event);
    verify(listener1, times(1)).itemChanged(event);
    verify(listener2, times(1)).itemChanged(event);

    watcher.removeListener(listener1);
    watcher.notifyItemChanged(event);
    verify(listener1, times(1)).itemChanged(event);
    verify(listener2, times(2)).itemChanged(event);
  }

}
