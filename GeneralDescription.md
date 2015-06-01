_As gwt-ui project is in very early state, subject if this document could be changed_

# Introduction #

The main purpose is to eliminate repetitive code if constructing UI in GWT. Its is much easer to describe UI in XML file and then let GWT to build your interface.

Moreover, changing states of UI often also takes much time - to remove/hide one widgets, and to show/add anothers. This `gwt-ui` also handles state changes.

All work is done duting compile-time using GWT's Generators, so it should works as fast, as written 'by hand'.

# Smallest Example #

Lets see basic and smallest example of how to use gwt-ui.

## 1. Layout XML ##

First of all lets define layout and states. To do that, we need to create file `ui.xml` and put in the folder, where module definition (**.gwt.xml) with such contents:**

```
<gwtui>
<layout>
   <container id='main' type='FlowPanel'/>     
</layout>
<states widgetpackage='client'>
  <state id='fiststate' default='true' >
     <content container='main'><widget type='MyWidget1' /></content>
  </state>  
  <state id='anotherstate'>
     <content container='main'><widget type='MyWidget2' /></content>
  </state>
</states>
</gwtui>
```

This xml file describes small one _container_ and two _states_, each state with one widget in that container.

Lets assume that we already have two widgets - `MyWidget1` and `MyWidget2` in `client` package.

## 2. Inheriting GWTUI ##

You need to include gwtui.jar file and add this line to your module definition file:
```
	<inherits name='org.olostan.gwtui.GWTUI'/>
```

## 3. Custom Interface ##

Now you should define some interface, that implements UIManager interface. Using this interface we can publish some containers, so you can add them to page.

Here is example of interface using XML above:
```
public interface MyUI extends UIManager {
    public Panel getMainContainer();
}
```

**Note** that signature of method should be: `public <Type> get<Id-Of-Container>Container();`
Where `<Type>` is some type, in which real container object can be casted.

## 3. Create UI implementation and use/add containers ##

To use generated UI you should create implementation using `GWT.create(...)` method. For example, there is implementation of sample `EntryPoint` that add constructed interface to `RootPanel`:

```
public class MyModule implements EntryPoint {
    MyUI ui;
    public void onModuleLoad() {
        ui = (MyUI) GWT.create(MyUI.class);
        RootPanel.get().add(ui.getMainContainer());
        ui.setState("fiststate");        
    }
}
```

Later you can change state just calling `ui.setState('anotherstate');`.

# Extended features #

Where is some extended features of gwt-ui:
  * State inheritance: any state can have parent state. So you can write some general state with basic UI, and then derive some state with only changes.
  * In-line visual style of containers (like "`align='right' width='100%' style='bar'`")
  * State-aware widgets: If widget implements `UIStateListener` interface and added to UI, it will be notified about state change.
  * _(Planned)_ Container can be any type extends `Panel` class or `gwtui.Containable` interface