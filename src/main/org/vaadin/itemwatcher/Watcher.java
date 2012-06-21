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

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

/**
 * Watches one or more {@link Item} objects, and notifies all registered {@link ItemChangedListener} of
 * changes in value.
 */
public class Watcher implements Serializable {

  protected Collection<ItemChangedListener> listeners = new LinkedList<ItemChangedListener>();

  protected Map<Item, ItemWatcher> itemWatchers = new HashMap<Item, ItemWatcher>();

  /**
   * Registers a new ItemChangeListener
   *
   * @param listener  the listener to be added.
   */
  public void addListener(ItemChangedListener listener) {
    if (listener != null && !listeners.contains(listener)) {
      listeners.add(listener);
    }
  }

  /**
   * Removes a previously registered ItemChangeListener
   *
   * @param listener  the listener to be removed.
   */
  public void removeListener(ItemChangedListener listener) {
    if (listener != null && listeners.contains(listener)) {
      listeners.remove(listener);
    }
  }

  /**
   * Notifies all ItemChangeListeners of an ItemChangedEvent
   *
   * @param event The event to notify listeners of
   */
  protected void notifyItemChanged(ItemChangedEvent event) {
    for (ItemChangedListener listener : listeners) {
      listener.itemChanged(event);
    }
  }

  /**
   * Watches an item for all property changes, and notifies all registered
   * ItemChangeListeners of any value change.
   *
   * @param item The item to watch
   */
  public void watch(Item item) {
    ItemWatcher watcher = new ItemWatcher(item);
    watcher.watch();
    itemWatchers.put(item, watcher);
  }

  /**
   * Stops watching the given item, removing any listeners
   *
   * @param item The item to stop watching
   */
  protected void unwatch(Item item) {
    ItemWatcher itemWatcher = itemWatchers.remove(item);
    if (itemWatcher != null) {
      itemWatcher.unwatch();
    }
  }

  /**
   * Watches a single item, and
   */
  protected class ItemWatcher implements Property.ValueChangeListener {
    private static final long serialVersionUID = -5521598324121258216L;
    @SuppressWarnings("NonSerializableFieldInSerializableClass")
    protected Collection<Object> watchedPropertyIds = new HashSet<Object>();
    protected Item item;

    public ItemWatcher(Item item) {
      this.item = item;
    }

    /**
     * Watch all of the properties on the item
     */
    public void watch() {
      watch(item.getItemPropertyIds());
    }

    /**
     * Watches the given properties on the item
     *
     * @param propertyIds the ids of the properties to watch
     */
    public void watch(Iterable<?> propertyIds) {
      for (Object propertyId : propertyIds) {
        watch(propertyId);
      }
    }

    /**
     * Attempts to watch the given property on the item
     * @param propertyId the id pof the property to watch
     */
    public void watch(Object propertyId) {
      Property property = item.getItemProperty(propertyId);
      if (property instanceof Property.ValueChangeNotifier) {
        ((Property.ValueChangeNotifier) property).addListener(this);
        watchedPropertyIds.add(propertyId);
      }
    }

    /**
     * Stops watching all of the properties on the item
     */
    public void unwatch() {
      unwatch(new HashSet<Object>(watchedPropertyIds));
    }

    /**
     * Stops watching the given properties on the item
     *
     * @param propertyIds The ids of the properties to stop watching
     */
    public void unwatch(Iterable<?> propertyIds) {
      for (Object propertyId : propertyIds) {
        unwatch(propertyId);
      }
    }

    /**
     * Stops watching the given property
     *
     * @param propertyId the id of the property to stop watching
     */
    public void unwatch(Object propertyId) {
      if (watchedPropertyIds.contains(propertyId)) {
        Property property = item.getItemProperty(propertyId);
        if (property instanceof Property.ValueChangeNotifier) {
          ((Property.ValueChangeNotifier) property).removeListener(this);
          watchedPropertyIds.remove(propertyId);
        }
      }
    }


    @Override
    public void valueChange(Property.ValueChangeEvent event) {
      notifyItemChanged(new ItemChangedEvent(item, event.getProperty()));
    }
  }
}
