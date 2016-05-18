# ISBI Sample Service

In this example you will find an implementation of a very simple Service.
It only generates a random number and writes it to ProcessData of your running Business Process.
After working with this example you will be able to 
- understand the structure of the Adapter
- the code entry point where the control is handed over from BP to your code
- how the deployment descriptor looks like that is needed to deploy the Service into ISBI
- how you can automatically create an instance of the service when you install it
- how you do the l10n for the configuration menus

## Things to consider
This example is a really simple one to show the layout of a service. 
Send feedback if you like it. Send feedback if you do not like it :grin:
Avoid creating your own threads since they will not be monitored by ISBI and can introduce blocks.
Avoid add 3rd party libs that are already shipping with ISBI, you might create version 
conflicts for the rest of the product leaving ISBI unusable.

Before running ant to build the service, please make sure that you are 
setting your `PATH` and `JAVA_HOME` so that they are pointing to the ISBI
JDK that you will find under the ISBI install directory.

Technically there is no need to use `ant`, just implement the same sequence
of build operations and pay attention to using the correct `JDK` in
your `PATH` and `JAVA_HOME` 

The Ant script is assuming a default location in a subdirectory 
next to your ISBI `install` directory. When you change the locations in the
ant script you can also place it somewhere else. It will only need read only access
to the ISBI install directory so you can even use another user to build it.

## TODO List
- write your java code, see the example
- define the service in the deployment descriptor under servicedefs/
  - you can define more than 1 service in a file
  - the ```<SERVICE>``` element describes the service and points to the Java code
  - the ```label``` tags point to the language property file in files/properties/lang/en
  - please pay attention to the 3 different leves in the deployment descriptor to describe the parameters for the service
    - `global` defines parameters that you can set across all instances of the service
    - `instance` defines parameters that you set for a single instance of a service
    - `wfd` defines the parameters that you can provide during workflow execution
- if needed create language specific files in files/properties/lang/<language>
- if needed automagically create an instance with the servicesinstances.xml file
- if needed you can add more subdirectories like `bpml` to automatically add BPs


## How to install
Once you have compiled it you can install the Service with
bin/InstallService.sh
