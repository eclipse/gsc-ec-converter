/*
 * Copyright (c) 2016 Goldman Sachs.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.tools;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.List;

import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.list.mutable.ListAdapter;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class ConverterTest
{
    @Rule
    public ConverterTestResource testResource = new ConverterTestResource();

    @Test
    public void main() throws Exception
    {
        Path no_updateFile = Paths.get(ConverterTestResource.TEST_DIR + "/pom_no_update.xml");
        FileTime lastModifiedTimeControl = Files.getLastModifiedTime(no_updateFile);

        Converter.main(new String[]{ConverterTestResource.TEST_DIR});

        MutableList<String> gradleFile = ListAdapter.adapt(Files.readAllLines(Paths.get(ConverterTestResource.TEST_DIR + "/build.gradle")));
        Assert.assertEquals(0, gradleFile.countWith(String::contains, "com.goldmansachs"));
        Assert.assertEquals(0, gradleFile.countWith(String::contains, "gs-collections"));
        Assert.assertEquals(3, gradleFile.countWith(String::contains, "org.eclipse.collections"));
        Assert.assertEquals(3, gradleFile.countWith(String::contains, "eclipse-collections"));

        MutableList<String> pomFile = ListAdapter.adapt(Files.readAllLines(Paths.get(ConverterTestResource.TEST_DIR + "/pom.xml")));
        Assert.assertEquals(0, pomFile.countWith(String::contains, "com.goldmansachs"));
        Assert.assertEquals(0, pomFile.countWith(String::contains, "gs-collections"));
        Assert.assertEquals(3, pomFile.countWith(String::contains, "org.eclipse.collections"));
        Assert.assertEquals(3, pomFile.countWith(String::contains, "eclipse-collections"));

        MutableList<String> javaFile = ListAdapter.adapt(Files.readAllLines(Paths.get(ConverterTestResource.TEST_DIR + "/src/main/java/TestClass.java")));
        Assert.assertEquals(0, javaFile.countWith(String::contains, "com.gs"));
        Assert.assertEquals(2, javaFile.countWith(String::contains, "org.eclipse"));

        MutableList<String> ignoredFile = ListAdapter.adapt(Files.readAllLines(Paths.get(ConverterTestResource.TEST_DIR + "/.ignored/should_not_be_read")));
        Assert.assertEquals(3, ignoredFile.countWith(String::contains, "com.goldmansachs"));
        Assert.assertEquals(3, ignoredFile.countWith(String::contains, "gs-collections"));
        Assert.assertEquals(0, ignoredFile.countWith(String::contains, "org.eclipse.collections"));
        Assert.assertEquals(0, ignoredFile.countWith(String::contains, "eclipse-collections"));

        FileTime lastModifiedTime = Files.getLastModifiedTime(no_updateFile);
        Assert.assertEquals(lastModifiedTimeControl, lastModifiedTime);
    }

    @Test
    public void invalidArg()
    {
        try
        {
            Converter.main(new String[]{});
            Assert.fail("Converter should fail for 0 argument");
        }
        catch (RuntimeException e)
        {
            Assert.assertEquals(1, ExitMock.getInstance().getStatus());
        }
    }

    @Test
    public void invalidPath()
    {
        try
        {
            Converter.main(new String[]{"nonExistingPath"});
            Assert.fail("Converter should fail for invalid path argument");
        }
        catch (RuntimeException e)
        {
            Assert.assertEquals(1, ExitMock.getInstance().getStatus());
        }
    }
}
