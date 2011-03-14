// <editor-fold defaultstate="collapsed">
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
// </editor-fold>

package com.medsphere.fileman;

import java.util.ArrayList;

import com.medsphere.resource.ResWalker;
import com.medsphere.resource.Resource;

public class GlobalNode {
    private String subscript;
    private String value;
    private ArrayList<GlobalNode> children;

    public GlobalNode() {
    }

    public GlobalNode(ResWalker walker) {
        while( walker.nextProperty() ) {
            String prop = walker.getProperty();
            if (prop.equals("S")) {
                setSubscript( walker.getValue() );
            } else if (prop.equals("V")) {
                setValue( walker.getValue() );
            } else if (prop.equals("N")) {
                addChild( new GlobalNode(walker) );
            } else {
                walker.skip();
            }
        }
    }

    public void writeResource(Resource queryRes) {
        queryRes.addCompound("N");
        queryRes.addProperty("S", getSubscript());
        queryRes.addProperty("V", getValue());
        for (int i=getChildCount()-1; i>=0; --i) {
            getChild(i).writeResource(queryRes);
        }
        queryRes.endCompound(); // N
    }

    public String getSubscript() {
        return subscript;
    }

    public String getValue() {
        return value;
    }

    public GlobalNode getChild( int i ) {
        if (i<0 || i>=getChildCount()) {
            return null;
        }
        return children.get( i );
    }

    public int getChildCount() {
        if (children==null) {
            return 0;
        }
        return children.size();
    }

    public void addChild( GlobalNode child ) {
        if (children==null) {
            children = new ArrayList<GlobalNode>();
        }
        children.add( child );
    }

    public void setSubscript(String subscript) {
        this.subscript = subscript;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void copyChildren( GlobalNode other ) {
        int otherCount = 0;
        if (other!=null) {
            otherCount = other.getChildCount();
        }
        if (otherCount!=0) {
            children = new ArrayList<GlobalNode>();
            for (int i=0; i<otherCount; ++i){
                children.add(other.getChild(i));
            }
        } else {
            children = null;
        }
    }

    public String toString() {
        if (subscript!=null) {
            return getSubscript();
        }
        return "";
    }

}
