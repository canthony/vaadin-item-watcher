Vaadin Item Watcher
===================

This library enables you monitor one or more Vaadin items for property value changes.
The main class is the ```org.vaadin.itemwatcher.Watcher``` class.

Usage Example
=============

```java
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
```

License
=======
Code released under the Apache 2 License.

Cheers,

Charles.