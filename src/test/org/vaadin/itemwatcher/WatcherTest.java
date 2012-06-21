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

import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static junit.framework.Assert.*;
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

  @Test
  public void testWatch() {
    ObjectProperty<String> p1 = new ObjectProperty<String>("A1");
    PropertysetItem item = new PropertysetItem();
    item.addItemProperty("p1", p1);
    ItemChangedListener listener1 = mock(ItemChangedListener.class);
    ItemChangedListener listener2 = mock(ItemChangedListener.class);
    watcher.addListener(listener1);
    watcher.addListener(listener2);

    assertTrue(watcher.itemWatchers.isEmpty());
    assertTrue(p1.getListeners(Property.ValueChangeEvent.class).isEmpty());
    watcher.watch(item);
    assertEquals(1, watcher.itemWatchers.size());
    assertEquals(1, p1.getListeners(Property.ValueChangeEvent.class).size());

    p1.setValue("A2");
    ArgumentCaptor<ItemChangedEvent> captor = ArgumentCaptor.forClass(ItemChangedEvent.class);
    verify(listener1, times(1)).itemChanged(captor.capture());
    assertSame(item, captor.getValue().getItem());
    assertSame(p1, captor.getValue().getProperty());
    assertEquals("A2", captor.getValue().getProperty().getValue());

    verify(listener2, times(1)).itemChanged(captor.capture());
    assertSame(item, captor.getValue().getItem());
    assertSame(p1, captor.getValue().getProperty());
    assertEquals("A2", captor.getValue().getProperty().getValue());

    Watcher.ItemWatcher iw = watcher.itemWatchers.get(item);
    iw.unwatch("p1");
    p1.setValue("A3");
    verify(listener1, times(1)).itemChanged(captor.capture());
  }

  @Test
  public void testUnwatch() {
    ObjectProperty<String> p1 = new ObjectProperty<String>("A1");

    PropertysetItem item = new PropertysetItem();
    item.addItemProperty("p1", p1);

    assertTrue(watcher.itemWatchers.isEmpty());
    watcher.watch(item);
    assertEquals(1, watcher.itemWatchers.size());

    watcher.unwatch(item);
    assertEquals(0, watcher.itemWatchers.size());
    assertEquals(0, p1.getListeners(Property.ValueChangeEvent.class).size());

  }

}
