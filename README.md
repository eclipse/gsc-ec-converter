Eclipse Collections converter 
=============================

A converter application to replace [GS Collections](https://github.com/goldmansachs/gs-collections) dependencies with [Eclipse Collections](https://www.eclipse.org/collections/).

GS Collections 7.0 and Eclipse Collections 7.0 are functionally equivalent besides the name space changes below.

* Maven groupId: from ```com.goldmansachs``` to ```org.eclipse.collections```
* Maven artifactId: from ```gs-collections``` to ```eclipse-collections```
* Java package: from ```com.gs.collections``` to ```org.eclipse.collections```

To help users migrating from GS Collections to Eclipse Collections, 
we created a handy converter to replace the dependencies in build configurations and package imports in source code in your project.

File Scope
----------

Files with suffix below are in scope of the converter application.

 * .java 
 * .xml (Ant, Ivy, Maven) 
 * .gradle (Gradle)

1. Update GS Collections to 7.0
---------------------------------

If your project is depending on GS Collections 6.2 or earlier, 
update your project dependencies to use GS Collections 7.0 first. 
Make sure your project can be built with 7.0. 


2. Run converter against your project
-------------------------------------
NOTE: The converter binary hasn't been released yet. In the meantime you can run the converter from source code, following the steps below.

Clone this repo, or download [master zip](https://github.com/eclipse/gsc-ec-converter/archive/master.zip) file and extract it. 
Run the command below under the folder you cloned or extracted.

#### Windows
```
gradlew.bat run -Pargs="/path/to/your/project"
```

#### Mac/Linux/Unix
```
./gradlew run -Pargs="/path/to/your/project"
```

That's it. 

Enjoy happy Java development with Eclipse Collections!!
