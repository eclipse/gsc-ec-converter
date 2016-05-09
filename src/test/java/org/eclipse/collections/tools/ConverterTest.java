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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;

import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.list.mutable.ListAdapter;
import org.eclipse.collections.impl.list.primitive.IntInterval;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ConverterTest
{
    public static final String RESOURCE_DIR = "src/test/resources/source";

    @Rule
    public NoSystemExitRule testResource = new NoSystemExitRule();

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    private File testDir;

    @Before
    public void setUp() throws IOException
    {
        this.testDir = this.temporaryFolder.newFolder();
        this.copyFilesToTestDir();
    }
    @Test
    public void main() throws Exception
    {
        Path noUpdateFile = Paths.get(this.testDir + "/pom_no_update.xml");
        FileTime lastModifiedTimeControl = Files.getLastModifiedTime(noUpdateFile);

        Converter.main(this.testDir.getAbsolutePath());

        MutableList<String> gradleFile = ListAdapter.adapt(Files.readAllLines(Paths.get(this.testDir + "/build.gradle")));
        Assert.assertEquals(0, gradleFile.countWith(String::contains, "com.goldmansachs"));
        Assert.assertEquals(0, gradleFile.countWith(String::contains, "gs-collections"));
        Assert.assertEquals(3, gradleFile.countWith(String::contains, "org.eclipse.collections"));
        Assert.assertEquals(3, gradleFile.countWith(String::contains, "eclipse-collections"));

        MutableList<String> pomFile = ListAdapter.adapt(Files.readAllLines(Paths.get(this.testDir + "/pom.xml")));
        Assert.assertEquals(0, pomFile.countWith(String::contains, "com.goldmansachs"));
        Assert.assertEquals(0, pomFile.countWith(String::contains, "gs-collections"));
        Assert.assertEquals(3, pomFile.countWith(String::contains, "org.eclipse.collections"));
        Assert.assertEquals(3, pomFile.countWith(String::contains, "eclipse-collections"));

        MutableList<String> javaFile = ListAdapter.adapt(Files.readAllLines(Paths.get(this.testDir + "/src/main/java/TestClass.java")));
        Assert.assertEquals(1, javaFile.countWith(String::contains, "com.gs"));
        Assert.assertEquals(2, javaFile.countWith(String::contains, "org.eclipse"));

        MutableList<String> ignoredFile = ListAdapter.adapt(Files.readAllLines(Paths.get(this.testDir + "/.ignored/should_not_be_read")));
        Assert.assertEquals(3, ignoredFile.countWith(String::contains, "com.goldmansachs"));
        Assert.assertEquals(3, ignoredFile.countWith(String::contains, "gs-collections"));
        Assert.assertEquals(0, ignoredFile.countWith(String::contains, "org.eclipse.collections"));
        Assert.assertEquals(0, ignoredFile.countWith(String::contains, "eclipse-collections"));

        FileTime lastModifiedTime = Files.getLastModifiedTime(noUpdateFile);
        Assert.assertEquals(lastModifiedTimeControl, lastModifiedTime);
    }

    @Test
    public void invalidArg1()
    {
        try
        {
            Converter.main();
            Assert.fail("Converter should fail for 0 argument");
        }
        catch (RuntimeException e)
        {
            Assert.assertEquals(1, ExitMock.getInstance().getStatus());
        }
    }

    @Test
    public void invalidArg2()
    {
        try
        {
            Converter.main(this.testDir.getAbsolutePath(), "nonExistingEncoding");
            Assert.fail("Converter should fail for non existing encoding");
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
            Converter.main("nonExistingPath");
            Assert.fail("Converter should fail for invalid path argument");
        }
        catch (RuntimeException e)
        {
            Assert.assertEquals(1, ExitMock.getInstance().getStatus());
        }
    }

    private void copyFilesToTestDir() throws IOException
    {
        Path src = new File(RESOURCE_DIR).toPath();
        Path dist = this.testDir.toPath();

        if (!Files.exists(dist))
        {
            Files.createDirectory(dist);
        }

        Files.walk(src).forEach(path -> {
            if (path.getNameCount() > src.getNameCount())
            {
                String relativePath = IntInterval.fromTo(src.getNameCount(), path.getNameCount() - 1)
                        .collect(count -> path.getName(count).toString())
                        .makeString("/");
                Path target = dist.resolve(Paths.get(relativePath));
                try
                {
                    Files.copy(path, target);
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
