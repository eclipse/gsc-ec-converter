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

import java.security.Permission;

public class ExitMock extends SecurityManager
{
    private static final ExitMock INSTANCE = new ExitMock();
    private static final SecurityManager SECURITY_MANAGER = System.getSecurityManager();
    private int status = 0;

    private ExitMock()
    {

    }

    public static ExitMock getInstance()
    {
        return INSTANCE;
    }

    @Override
    public void checkPermission(Permission perm)
    {
        //bypass
    }

    @Override
    public void checkExit(int status)
    {
        this.status = status;
        throw new RuntimeException();
    }

    public int getStatus()
    {
        return this.status;
    }

    public void setUp()
    {
        this.status = 0;
        System.setSecurityManager(INSTANCE);
    }

    public void tearDown()
    {
        this.status = 0;
        System.setSecurityManager(SECURITY_MANAGER);
    }
}
