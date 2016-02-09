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

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.factory.Maps;
import org.eclipse.collections.impl.list.mutable.ListAdapter;
import org.eclipse.collections.impl.list.primitive.IntInterval;

public class Converter
{
    private static ImmutableMap<String, String> CONVERSION_MAP;

    static
    {
        MutableMap<String, String> conversionMap = Maps.mutable.empty();
        conversionMap.put("com\\.gs", "org\\.eclipse");
        conversionMap.put("com\\.goldmansachs", "org\\.eclipse\\.collections");
        conversionMap.put("gs-collections", "eclipse-collections");
        CONVERSION_MAP = Maps.immutable.ofMap(conversionMap);
    }

    public static void main(String[] args)
    {
        if (args.length != 1)
        {
            System.out.println("Usage: gsc-ec-converter <PATH_TO_PROJECT>");
            System.exit(1);
        }
        String path = args[0];
        if (Files.exists(Paths.get(path)))
        {
            System.out.println("Running Eclipse Collections Converter against " + path + ".");
            convert(path);
            System.out.println("Done!");
        }
        else
        {
            System.out.println("Invalid path: " + path);
            System.exit(1);
        }
    }

    private static void convert(String path)
    {
        try
        {
            Files.walk(Paths.get(path))
                    .filter(Files::isRegularFile)
                    .filter(file -> IntInterval.fromTo(0, file.getNameCount() - 1).collect(file::getName).noneSatisfy(s -> s.toString().startsWith(".")))
                    .forEach(file -> {
                        try
                        {
                            MutableList<String> allLines = ListAdapter.adapt(Files.readAllLines(file, Charset.defaultCharset()));
                            MutableList<String> replacedLines = allLines.collect(Converter::replaceMatching);

                            Files.write(file, replacedLines);
                        }
                        catch (IOException e)
                        {
                            throw new RuntimeException("A problem occurred in file: " + file, e);
                        }
                    });
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    protected static String replaceMatching(final String original)
    {
        String[] temp = new String[]{original};
        CONVERSION_MAP.forEachKeyValue((key, value) -> {
            final Pattern pattern = Pattern.compile(key);
            Matcher matcher = pattern.matcher(temp[0]);
            temp[0] = matcher.replaceAll(value);
        });
        return temp[0];
    }
}