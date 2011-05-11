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


package com.medsphere.vistarpc;

public class RPCResponse {
    public enum ResponseType {SINGLE_VALUE, GLOBAL_INSTANCE, ARRAY, GLOBAL_ARRAY, WORD_PROCESSING}
    ResponseType type;
    Object value;
    private String errorMessage;

    public RPCResponse() {
        this(ResponseType.SINGLE_VALUE);
    }

    public RPCResponse( String value ) {
        this( ResponseType.SINGLE_VALUE );
        setValue( value );
    }

    public RPCResponse( String[] value ) {
        this( ResponseType.ARRAY );
        setValue( value );
    }

    public RPCResponse( ResponseType type ) {
        this.type = type;
    }

    public String[] getArray() {
        return (String[]) value;
    }

    public void setValue( Object value ) {
        this.value = value;
    }

    public String getString() {
        return (String) value;
    }

    public ResponseType getResponseType() {
        return type;
    }

    public void setError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getError() {
        return errorMessage;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (getError()!=null) {
            sb.append("ERROR: ").append(getError());
        } else if (value==null) {
            sb.append("No value or error");
        } else if (value instanceof String[]) {
            String[] array = (String[]) value;
            sb.append("Array[").append(array.length).append("]={");
            for (int i=0; i<array.length; i++) {
                if (i>0) {
                    sb.append(",");
                }
                sb.append("\"").append(array[i]).append("\"");
            }
            sb.append("}");
        }  else if (value instanceof String) {
            sb.append("String=\"").append(value.toString()).append("\"");
        } else {
            sb.append("Unknown value type"); // Shouldn't ever happen
        }
        return sb.toString();
    }
}
