/*
 * Copyright (C) 2007-2009  Medsphere Systems Corporation
 * All rights reserved.
 *
 * This source code contains the intellectual property
 * of its copyright holder(s), and is made available
 * under a license. If you do not know the terms of
 * the license, please stop and do not read further.
 *
 * Please read LICENSES for detailed information about
 * the license this source code file is available under.
 * Questions should be directed to legal@medsphere.com
 *
 */

package com.medsphere.resource;

import java.util.LinkedList;

public class ResNode {
    final protected String name;
    final protected String value;
    final protected ResNode parent;
    LinkedList<ResNode> children;

    protected ResNode( ResNode parent, String name, String value ) {
        this.parent = parent;
        this.name = name;
        this.value = value;
        children = null;
    }

    protected ResNode( ResNode parent, String name ) {
        this( parent, name, null );
    }

    protected ResNode getNextSibling() {
        if (parent==null) {
            return null;
        }
        return parent.getNextChild( this );
    }

    protected ResNode getNextChild( ResNode node ) {
        int index = children.indexOf( node );
        if (index >= children.size()-1) {
            return null;
        }
        return children.get( index + 1 );
    }

    protected ResNode getChild( int index ) {
        if (children!=null && index<children.size()) {
            return children.get( index );
        }
        return null;
    }

    public String getProperty() {
        return name;
    }

    public String getValue() {
        return value;
    }

    protected boolean addChild( ResNode child ) {
        if (value!=null) {
            return false;
        }
        if (children==null) {
            children = new LinkedList<ResNode>();
        }
        children.add( child );
        return true;
    }

    protected ResNode getParent() {
        return parent;
    }

    protected boolean isCompound() {
        return value==null;
    }

    public void addChild( String string, String value ) {
        addChild( new ResNode(this, string, value) );
    }

    public ResNode addChild(String string) {
        ResNode newChild = new ResNode(this, string);
        return addChild( newChild ) ? newChild : null;
    }
}
