Eclipse Collections converter 
=============================

A converter application to replace [GS Collections](https://github.com/goldmansachs/gs-collections) usages with [Eclipse Collections](https://www.eclipse.org/collections/) in code.

GS Collections 7.0 and Eclipse Collections 7.0 are functionally equivalent besides the name changes.

* Maven groupId: from `com.goldmansachs` to `org.eclipse.collections`
* Maven artifactId: from `gs-collections` to `eclipse-collections`
* Java package: from `com.gs.collections` to `org.eclipse.collections`

To help users of GS Collections migrate to Eclipse Collections, we created this handy converter to replace dependencies in build configurations and package imports in source code in your projects.

File Scope
----------

The converter application runs on a directory and recursively scans for the following file types alters them in place.

 * .java
 * .xml (Ant, Ivy, Maven)
 * .gradle (Gradle)

1. Update GS Collections to 7.0
-------------------------------

If your project depends on GS Collections 6.2 or earlier, update your project to use GS Collections 7.0 first. Make sure your project builds successfully with 7.0.


2. Run the converter against your project
-----------------------------------------

Download [gsc-ec-converter-7.1.0.zip](https://www.eclipse.org/downloads/download.php?file=/collections/gsc-ec-converter-7.1.0.zip) and extract it.

Run the command below under the directory where you extracted the zip.

#### Windows
```bash
bin/gsc-ec-converter.bat /path/to/your/project
```

#### Mac/Linux/Unix
```bash
bin/gsc-ec-converter /path/to/your/project
```

That's it.

Enjoy happy Java development with Eclipse Collections!
