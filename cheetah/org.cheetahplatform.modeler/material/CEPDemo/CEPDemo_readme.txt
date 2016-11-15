Cheetah Experimental Platform
=============================

System Requirements
-------------------
 - Windows
 - Java 6
 - Internet Access

Quick Start
-----------
Start Cheetah Modeler using cheetah_modeler.exe and use the following key to start the experiment:

Cheetah Modeler Key: 3249

Read CEPDemo_experiment.pdf and follow the instructions provided by the tool. After
you have conducted your modification task, Cheetah Analyzer can be used to replay
the previously created process model. Use Process - Load - Replay to load and
replay process models. Furthermore, export functionalities are available under Process - Export. 

Note that all process models are stored in a central database and can therefore be replayed
by everybody using this demo of CEP.


Cheetah Modeler
---------------

When executing the experimental workflow configuration CEP guides the user
through the experiment ensuring that the setup is followed. Furthermore, data
collected when executing the experimental workflow is stored on a central
database server, giving researchers the possibility to check whether all
activities were completed and to restore the experiment to a specific state
(e.g., in case of a crashed system). If the database server cannot be accessed a
local copy is created and the user is asked to send it to the experiment's
supervisor via email.

In order to enable the investigation of how process models are created, CEP
offers Cheetah Modeler, which is a rather simple modeling component providing only basic
modeling functionalities for simulating a pen and paper modeling session using a
subset of BPMN. The focus was put on developing a tool facilitating the 
investigation of how process models are created, rather than providing a full 
fledged modeling suite.

Besides monitoring the experiment's correct execution and gathering the results of surveys, 
the collection of data on how users create process models was one of the main objectives 
when implementing Cheetah Modeler. Consequently, every change to the process model (e.g.,
add/delete/move activity, add/delete/move edge) and the corresponding timestamp is automatically 
recorded and stored in a separate process log, offering the possibility for detailed 
investigations concerning the process of process modeling. 

Cheetah Analyzer
----------------

To be able to analyze data collected when executing the experimental
workflow an export system is in place. By providing the option to export
data as Comma-Separated Values (CSV) files, several tools for performing
statistical analysis can be addressed (e.g., SPSS, Excel).

One of the main advantages of using CEP is the
possibility of replaying process models created with Cheetah Modeler. Recording
all modeling steps enables researches to investigate how business
process models are really created. For this purpose Cheetah Analyzer was
implemented allowing for a step by step execution of modeling processes. Additionally, 
researches can export modeling processes using the Mining XML (MXML) format, 
allowing them to apply process mining techniques using ProM. 

