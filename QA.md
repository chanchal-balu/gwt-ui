Q: _For custom interface definition, does it matter if the_

&lt;Id-Of-Container&gt;

 is in lower-case, upper-case or mixed case in the ui.xml?

A: During generating implementation module do case insensitive lookup for valid condainer/widget. So you can write in any case, and implemented interface would return corrent widget/container.

Q: _Do all containers defined in ui.xml (within layout section) need to have a custom interface definition?_

A: No, you should define only those containers/widgets, that you want to retrieve from implementation. Functionality of module do not depends on methods, that are defined in custom interface.

Q: _What attributes are supported as part of the container definition in ui.xml? Could you please provide documentation on the wiki page?_

A: Currently there such attributes supported:
|type|Class type of container|
|:---|:----------------------|
|id  |Id of container, that would be used as reference|
|align|Horizontal aligment (valid for containers, that could have horizontal aligment)|
|width|Width of containers (used as parameter for SetWidth() call|
|cellwidth|Width of cell, where container is located. Parent container recieve SetCellWidth() call with current container as parameter|
|style|CSS style of container |

Q: _Do you have more attributes planned in the future?_

A: One of idea is to add JavaBean-like attributes support to containers/widgets. This idea is in panning state, so should be discussed much before implemented. If you have thoughts, please write email to olostan@gmail.com

Q: _What are "state aware widgets" - what does it mean?_

A: "State aware widgets" are widgets, that implements UIStateListener interface and added to gwt-ui layout. These widgets would receive notifications about state changes. So if widget remain visible on page, it can change its content using current state. Example of such widget is HelpWidget from 'gwt-ui-sample' project in svn ([HelpWidget.java source](http://gwt-ui.googlecode.com/svn/trunk/gwt-ui-sample/src/main/java/client/widgets/HelpWidget.java))

Q: _Is it possible to dynamically create states in run-time?_

A: As gwt-ui project uses Generators, UI implementation is build during compile-time. So it is no possibility to change/add layout/states after project is compiled by GWT (as UI implementation is already generated).