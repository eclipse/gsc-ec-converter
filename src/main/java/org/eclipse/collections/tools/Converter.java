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
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Maps;

public class Converter
{
    private static final ImmutableList<String> FILE_SCOPE = Lists.immutable.of(".java", ".xml", "gradle", ".groovy", ".scala", ".kt", ".rb", ".clj");
    private static final ImmutableMap<String, String> CONVERSION_MAP = Maps.immutable.with(
            "com\\.gs\\.collections", "org\\.eclipse\\.collections",
            "com\\.goldmansachs", "org\\.eclipse\\.collections",
            "gs-collections", "eclipse-collections");
    public static final Charset DEFAULT_ENCODING = Charset.forName("UTF-8");
    private static Charset endoding = DEFAULT_ENCODING;

    public static void main(String... args)
    {
        if (args.length < 1)
        {
            System.out.println("Usage: gsc-ec-converter <PATH_TO_PROJECT> [File encoding]");
            System.exit(1);
        }
        String path = args[0];
        if (Files.exists(Paths.get(path)))
        {
            if (args.length == 2)
            {
                try
                {
                    endoding = Charset.forName(args[1]);
                }
                catch (IllegalCharsetNameException | UnsupportedCharsetException e)
                {
                    System.out.println("Invalid file encoding is passed: " + args[1]);
                    System.exit(1);
                }
            }
            System.out.println("Running Eclipse Collections Converter against " + path + ".");
            Converter.convert(path);
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
                    .filter(file -> FILE_SCOPE.anySatisfy(suffix -> file.getFileName().toString().endsWith(suffix)))
                    .forEach(file -> {
                        try
                        {
                            boolean[] fileChanged = {false};
                            List<String> replacedLines = Files.lines(file, endoding).map(line -> {
                                String newLine = Converter.replaceMatching(line);
                                fileChanged[0] = fileChanged[0] || !newLine.equals(line);
                                return newLine;
                            }).collect(Collectors.toList());

                            if (fileChanged[0])
                            {
                                Files.write(file, replacedLines);
                            }
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

    protected static String replaceMatching(String original)
    {
        String[] temp = {original};
        CONVERSION_MAP.forEachKeyValue((key, value) -> {
            Pattern pattern = Pattern.compile(key);
            Matcher matcher = pattern.matcher(temp[0]);
            temp[0] = matcher.replaceAll(value);
        });
        return temp[0];
    }
}
