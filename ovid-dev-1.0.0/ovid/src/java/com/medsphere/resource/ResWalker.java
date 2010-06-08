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

public class ResWalker {
    protected ResNode readNode;
    protected boolean childrenRead = false;

    public ResWalker( Resource res ) {
        readNode = res.getRootNode();
        childrenRead = false;
    }

    public boolean nextProperty() {
        ResNode nextode = null;
        if (childrenRead) {
            // All the children have been read. Move to our next sibling.
            childrenRead = false;
            nextode = readNode.getNextSibling();
            if (nextode!=null) {
                readNode = nextode;
                return true;
            }
            // No siblings. Go to parent and return false.
            readNode = readNode.getParent();
            childrenRead = true;
            return false;
        }
        nextode = readNode.getChild(0);
        // Depth-first, first child
        if (nextode!=null) {
            readNode = nextode;
            return true;
        }
        // No children. Go after sibling
        nextode = readNode.getNextSibling();
        if (nextode!=null) {
            readNode = nextode;
            return true;
        }
        // No siblings. Go to parent and return false.
        readNode = readNode.getParent();
        childrenRead = true;
        return false;
    }

    public String getValue() {
        return readNode.getValue();
    }

    public String getProperty() {
        return readNode.getProperty();
    }

    public boolean isCompound() {
        return readNode.isCompound();
    }

    public void skip() {
        childrenRead = true; // mark the children as read so that we ignore them
    }
}
