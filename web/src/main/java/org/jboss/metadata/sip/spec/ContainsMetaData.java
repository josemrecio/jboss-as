/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.metadata.sip.spec;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author jean.deruelle@gmail.com
 *
 */
public class ContainsMetaData extends ConditionMetaData {
    private static final long serialVersionUID = 1;
    private String var;
    private String value;
    private boolean ignoreCase;

    /**
     * @param var the var to set
     */
    @XmlElement(name = "var")
    public void setVar(String var) {
        this.var = var;
    }

    /**
     * @return the var
     */
    public String getVar() {
        return var;
    }

    /**
     * @param value the value to set
     */
    @XmlElement(name = "value")
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param ignoreCase the ignoreCase to set
     */
    @XmlElement(name = "ignore-case")
    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    /**
     * @return the ignoreCase
     */
    public boolean isIgnoreCase() {
        return ignoreCase;
    }
}
