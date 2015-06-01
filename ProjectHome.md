This project help building GWT application with complex UI based on descriptive XML file.

All work is done during compile-time using GWT `Generators`.

Some features that is currently implemented:

  1. Fully descriptive XML-driven user inteface building.
  1. Easy access to generated objects.
  1. Simple state changing (just one call of method)
  1. "Lazy" widget creation: widgets are created only when it becomes visible
  1. State inheritance: any state can have parent state. So you can write some general state with basic UI, and then derive some state with only changes.
  1. In-line visual attributes of containers (like "align='right' width='100%' style='bar'")
  1. State-aware widgets: If widget implements UIStateListener interface and added to UI, it will be notified about state change.

Here is example of XML file for gwt-ui:
```
<gwtui>
<layout>
   <container type='VerticalPanel' id='main' width='100%'>     
     <container id='top' type='HorizontalPanel' align='right' width='100%' style='bar' />
     <container id='middle' type='HorizontalPanel' width="100%" >
        <container id='leftContent' type='VerticalPanel' style='bar' cellwidth='250px' width='100%' />
        <container id='centerContent' type='VerticalPanel' style='bar' width='100%' />
        <container id='rightContent' type='VerticalPanel' style='bar' cellwidth='150px' />
     </container>
     <container id='bottom' type='HorizontalPanel' align='center' width='100%' style='bar' />
   </container>   
</layout>
<states widgetpackage='client.widgets'>
  <state id='base' >
     <content container='top'><widget type='LogoWidget' /></content>
     <content container='leftContent'><widget type='LeftMenu' /></content>
     <content container='rightContent'><widget type='HelpWidget' /> </content>
     <content container='bottom'><widget type='CopyrightWidget' /></content>
  </state>  

  <state id='general' default='true' parent='base'>
     <content container='centerContent'><widget type='HelloWidget' name='helloWidget' /></content>
  </state>  
  <state id='state1' parent='base'>
     <content container='centerContent'><widget type='State1Widget' /></content>     
  </state>
</states>
</gwtui>
```

## Whats new ##
|08 June 2007| Added Gwt\*Wigets() methods for retrieving generated widgets by name|
|:-----------|:--------------------------------------------------------------------|
|            | Changed DOM Level 3 to DOM Level 2                                  |
|            | Added 'SimplePanel' container support                               |

## Roadmap ##
  * Containers could be any type that extends Panel or implements ?Addatable? interface. May be supports of Table panes or other kind of panels, that can have widgets
  * Widget libraries with preset attributes
  * (?) Custom attributes (JavaBean style?)
  * ....