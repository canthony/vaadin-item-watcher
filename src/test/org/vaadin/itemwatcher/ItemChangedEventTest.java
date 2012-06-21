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

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import org.junit.Test;

import static junit.framework.Assert.assertSame;

public class ItemChangedEventTest {

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNullItem() {
    new ItemChangedEvent(null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNullProperty() {
    new ItemChangedEvent(new PropertysetItem(), null);
  }

  @Test
  public void testGetters() {
    Item i = new PropertysetItem();
    Property p = new ObjectProperty<String>("A");
    ItemChangedEvent event = new ItemChangedEvent(i, p);

    assertSame(i, event.getSource());
    assertSame(i, event.getItem());
    assertSame(p, event.getProperty());
  }
}
