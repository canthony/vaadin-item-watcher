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
import com.vaadin.data.util.BeanItem;


public class WatcherUsageExample {

  public static void main(String[] args) {
    BeanItem<ExampleBean> item1 = new BeanItem<ExampleBean>(new ExampleBean());
    BeanItem<ExampleBean> item2 = new BeanItem<ExampleBean>(new ExampleBean());

    ItemChangedListener beanSaver = new ItemChangedListener() {
      @Override
      public void itemChanged(ItemChangedEvent event) {
        Item item = event.getItem();
        if (item instanceof BeanItem) {
          System.out.println("Save bean : " + ((BeanItem) item).getBean());
        }
      }
    };
    Watcher watcher = new Watcher();
    watcher.addListener(beanSaver);
    watcher.watch(item1);
    watcher.watch(item2);

    item1.getItemProperty("firstName").setValue("Charles");
    item1.getItemProperty("lastName").setValue("Anthony");

    item2.getItemProperty("firstName").setValue("David");
    item2.getItemProperty("lastName").setValue("Cameron");

  }


  public static class ExampleBean {
    String firstName;
    String lastName;

    public String getFirstName() {
      return firstName;
    }

    public void setFirstName(String firstName) {
      this.firstName = firstName;
    }

    public String getLastName() {
      return lastName;
    }

    public void setLastName(String lastName) {
      this.lastName = lastName;
    }

    @Override
    public String toString() {
      return "ExampleBean@" + System.identityHashCode(this) + "{firstName=" + firstName + ",lastName=" + lastName + "}";
    }
  }
}
