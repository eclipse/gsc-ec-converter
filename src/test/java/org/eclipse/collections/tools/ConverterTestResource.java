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

import org.eclipse.collections.impl.list.primitive.IntInterval;
import org.junit.rules.ExternalResource;

public class ConverterTestResource extends ExternalResource
{
    public static final String RESOURCE_DIR = "src/test/resources/source";
    public static final String TEST_DIR = System.getProperty("java.io.tmpdir") + "gsc_ec_converter_test";
    private final ExitMock exitMock = ExitMock.getInstance();

    @Override
    protected void before() throws Throwable
    {
        this.exitMock.setUp();
        this.copyFilesToTestDir();
    }

    @Override
    protected void after()
    {
        this.exitMock.tearDown();
        try
        {
            this.clean(Paths.get(TEST_DIR));
        }
        catch (IOException e)
        {
            throw new RuntimeException("Clean-up might have failed..", e);
        }
    }

    private void copyFilesToTestDir() throws IOException
    {
        Path src = new File(RESOURCE_DIR).toPath();
        Path dist = new File(TEST_DIR).toPath();

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

    private void clean(Path path) throws IOException
    {
        if (!Files.exists(path))
        {
            return;
        }
        if (Files.isRegularFile(path))
        {
            Files.delete(path);
        }
        else
        {
            Files.list(path).forEach(eachPath -> {
                try
                {
                    this.clean(eachPath);
                }
                catch (IOException e)
                {
                    throw new RuntimeException("Resources may not have been cleaned-up... " +
                            "Manually delete files under " + TEST_DIR + " and rerun", e);
                }
            });
            Files.delete(path);
        }
    }
}
