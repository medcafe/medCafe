/*
 * Copyright 2007 Tobias Riemer
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * VelocityVisitor.java
 *
 * Created on 28. March 2007, 09:25
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */


/*  Modified Jun 22, 2010


 *  Still to do:  figure out why ASTAddNode, ASTMulNode, ASTDivNode and ASTSubtractNode
 * default to visit(SimpleNode, Object).  The code in visit(SimpleNode, Object) fixes
 * the problem, but leaves the possiblity that other types could later experience
 * the same problem.



 */
package at.riemers.velocity2js.velocity;

import at.riemers.velocity2js.jsrenderer.JSRenderer;
import at.riemers.velocity2js.jsrenderer.JSRendererFast;


import java.util.ResourceBundle;

import org.apache.velocity.runtime.parser.node.ParserVisitor;
import org.apache.velocity.runtime.parser.node.ASTAddNode;
import org.apache.velocity.runtime.parser.node.ASTAndNode;
import org.apache.velocity.runtime.parser.node.ASTAssignment;
import org.apache.velocity.runtime.parser.node.ASTBlock;
import org.apache.velocity.runtime.parser.node.ASTComment;
import org.apache.velocity.runtime.parser.node.ASTDirective;
import org.apache.velocity.runtime.parser.node.ASTDivNode;
import org.apache.velocity.runtime.parser.node.ASTEQNode;
import org.apache.velocity.runtime.parser.node.ASTElseIfStatement;
import org.apache.velocity.runtime.parser.node.ASTElseStatement;
import org.apache.velocity.runtime.parser.node.ASTEscape;
import org.apache.velocity.runtime.parser.node.ASTEscapedDirective;
import org.apache.velocity.runtime.parser.node.ASTExpression;
import org.apache.velocity.runtime.parser.node.ASTFalse;
import org.apache.velocity.runtime.parser.node.ASTFloatingPointLiteral;
import org.apache.velocity.runtime.parser.node.ASTGENode;
import org.apache.velocity.runtime.parser.node.ASTGTNode;
import org.apache.velocity.runtime.parser.node.ASTIdentifier;
import org.apache.velocity.runtime.parser.node.ASTIfStatement;
import org.apache.velocity.runtime.parser.node.ASTIntegerLiteral;
import org.apache.velocity.runtime.parser.node.ASTIntegerRange;
import org.apache.velocity.runtime.parser.node.ASTLENode;
import org.apache.velocity.runtime.parser.node.ASTLTNode;
import org.apache.velocity.runtime.parser.node.ASTMap;
import org.apache.velocity.runtime.parser.node.ASTMethod;
import org.apache.velocity.runtime.parser.node.ASTModNode;
import org.apache.velocity.runtime.parser.node.ASTMulNode;
import org.apache.velocity.runtime.parser.node.ASTNENode;
import org.apache.velocity.runtime.parser.node.ASTNotNode;
import org.apache.velocity.runtime.parser.node.ASTObjectArray;
import org.apache.velocity.runtime.parser.node.ASTOrNode;
import org.apache.velocity.runtime.parser.node.ASTReference;
import org.apache.velocity.runtime.parser.node.ASTSetDirective;
import org.apache.velocity.runtime.parser.node.ASTStop;
import org.apache.velocity.runtime.parser.node.ASTStringLiteral;
import org.apache.velocity.runtime.parser.node.ASTSubtractNode;
import org.apache.velocity.runtime.parser.node.ASTText;
import org.apache.velocity.runtime.parser.node.ASTTrue;
import org.apache.velocity.runtime.parser.node.ASTWord;
import org.apache.velocity.runtime.parser.node.ASTprocess;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.runtime.parser.node.NodeUtils;
import org.apache.velocity.runtime.parser.node.SimpleNode;

/**
 *
 * @author tobias
 */
public class VelocityVisitor implements ParserVisitor {
    
    private ResourceBundle bundle;
    /** Creates a new instance of MyVisitor */
    public VelocityVisitor() {
    }
    
    public VelocityVisitor(ResourceBundle bundle) {
        this.bundle = bundle;
    }
    
    public Object visit(SimpleNode node, Object object) {
      
        if (!node.getClass().equals(SimpleNode.class)&&(!node.getClass().equals(ASTprocess.class)))
        {
            if(node instanceof ASTAddNode)
                return this.visit((ASTAddNode) node, object);
            else if (node instanceof ASTSubtractNode)
                return this.visit((ASTSubtractNode) node, object);
            else if (node instanceof ASTMulNode)
                return this.visit((ASTMulNode) node, object);
            else if (node instanceof ASTDivNode)
                return this.visit((ASTDivNode) node, object);
            else
            {
                System.err.println("Class " + node.getClass() + " went to visit ");
                System.err.println("method for SimpleNode instead of appropriate method!");
                return object;
            }
        }
        else
        {
        MyContext context = new MyContext();

        context.getRenderer().renderFunctionStart(object.toString());
        context = (MyContext) node.childrenAccept(this, context);
        context.getRenderer().renderFunctionEnd();
        
        return context.getRenderer();
        }
    }
    
    public Object visit(ASTprocess node, Object object) {
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            throw new UnsupportedOperationException("ASTprocess");
            //object = node.childrenAccept(this, object);
        }
        return object;
    }
    
    public Object visit(ASTEscapedDirective node, Object object) {
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            c.getRenderer().renderText(NodeUtils.tokenLiteral(node.getFirstToken()).substring(1));
            //throw new UnsupportedOperationException("ASTEscapedDirective");
            //object = node.childrenAccept(this, object);
        }
        return object;
    }
    
    public Object visit(ASTEscape node, Object object) {
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            throw new UnsupportedOperationException("ASTEscape");
            //object = node.childrenAccept(this, object);
        }
        return object;
    }
    
    public Object visit(ASTComment node, Object object) {
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            //c.appendBuf("/* " + NodeUtils.tokenLiteral(node.getFirstToken()) + "*/");
            String text = NodeUtils.tokenLiteral(node.getFirstToken());
            int i = text.indexOf("V2JS:");
            
            if (i >= 0 && i<text.length()-5) {
                String[] directives = text.substring(i+5).split(",| |;");
                for(int j = 0; j < directives.length; j++) {
                    if (!directives[j].trim().equals("")) {
                        String[] directive = directives[j].split("=");
                        String value = "";
                        if (directive.length > 1) {
                            value = directive[1];
                        }
                        c.getRenderer().setV2JSDirective(directive[0], value);
                    }
                }
            }
        }
        object = node.childrenAccept(this, object);
        return object;
    }
    
    public Object visit(ASTFloatingPointLiteral node, Object object) {
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            c.getRenderer().renderFloatingPointLiteral(NodeUtils.tokenLiteral(node.getFirstToken()));
            object = node.childrenAccept(this, object);
        }
        return object;
    }
    
    public Object visit(ASTIntegerLiteral node, Object object) {
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            c.getRenderer().renderIntegerLiteral(NodeUtils.tokenLiteral(node.getFirstToken()));
            object = node.childrenAccept(this, object);
        }
        return object;
    }
    
    public Object visit(ASTStringLiteral node, Object object) {
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            c.getRenderer().renderStringLiteral(NodeUtils.tokenLiteral(node.getFirstToken()));
            object = node.childrenAccept(this, object);
        }
        return object;
    }
    
    public Object visit(ASTIdentifier node, Object object) {
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            
            c.getRenderer().renderIdentifier(NodeUtils.tokenLiteral(node.getFirstToken()));
            
            /*
            if (node.jjtGetParent() instanceof ASTReference || node.jjtGetParent() instanceof ASTIdentifier) {
                c.appendBuf(".");
            }
            c.appendBuf(NodeUtils.tokenLiteral(node.getFirstToken()));*/
        } else {
            //   c.appendBuf("ERROR: " + NodeUtils.tokenLiteral(node.getFirstToken()));
        }
        return node.childrenAccept(this, object);
    }
    
    public Object visit(ASTWord node, Object object) {
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            throw new UnsupportedOperationException("ASTWord: " + node.getFirstToken());
            //object = node.childrenAccept(this, object);
        }
        return object;
    }
    
    public Object visit(ASTDirective node, Object object) {
        
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            
            if (node.getDirectiveName().equals("foreach")) {
                
                String list = getVar((SimpleNode)node.jjtGetChild(2), false, c);
                
                String var = getVar((SimpleNode)node.jjtGetChild(0), true, c);
                
                c.getRenderer().renderForStart(var, list);
                if (node.jjtGetChild(3) instanceof ASTBlock) {
                    visit((ASTBlock)node.jjtGetChild(3), object);
                }
                c.getRenderer().renderForEnd();
               /* cnt ++;
                String v = "i" + cnt;
                c.appendBuf("for (var " + v + "=0;  " + v + "<" + list + ".length; " + v + "++)");
                if (node.jjtGetChild(3) instanceof ASTBlock) {
                    c.appendBuf(" {");
                    c.newLine();
                    //c.appendBuf("alert('" + v + "');");
                   // c.newLine();
                    c.appendBuf("var " + var + " = " + list + "[" + v + "];");
                    c.newLine();
                    visit((ASTBlock)node.jjtGetChild(3), object);
                    c.appendBuf("}");
                    c.newLine();
                    return object;
                }*/
                
            } else {
                if (node.getDirectiveName().equals("include")) {
                    
                    for(int i=0;i<node.jjtGetNumChildren();i++) {
                        if ( node.jjtGetChild(i) instanceof ASTStringLiteral ) {
                            c.getRenderer().renderIncludeStart();
                            c.getRenderer().renderFunctionCall(createFunctionName(NodeUtils.tokenLiteral(node.jjtGetChild(i).getFirstToken())));
                            c.getRenderer().renderIncludeEnd();
                        }
                        if ( node.jjtGetChild(i) instanceof ASTReference ) {
                            c.getRenderer().renderIncludeStart();
                            c.getRenderer().renderDynaFunctionCallStart();
                            c.getRenderer().renderToken(getVar((SimpleNode) node.jjtGetChild(i), false, c).toString());
                            //node.jjtGetChild(i).jjtAccept(this, object);
                            c.getRenderer().renderDynaFunctionCallEnd();
                            c.getRenderer().renderIncludeEnd();
                        }
                    }
                    
                } else {
                    throw new UnsupportedOperationException("ASTDirective: " + node.getDirectiveName());
                }
                //object = node.childrenAccept(this, object);
            }
        }
        return object;
    }
    
    public String getVar(SimpleNode node, boolean local, MyContext context) {
        MyContext c = new MyContext();
        c.setLocal(local);
        c.getRenderer().setLocalVariables(context.getRenderer().getLocalVariables());
        c.getRenderer().createVariableStart();
        c = (MyContext) node.jjtAccept(this, c);
        c.getRenderer().createVariableEnd();
        return c.getRenderer().toString();
    }
    
    public Object visit(ASTBlock node, Object object) {
        object = node.childrenAccept(this, object);
        return object;
    }
    
    public Object visit(ASTMap node, Object object) {
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            throw new UnsupportedOperationException("ASTMap");
            //object = node.childrenAccept(this, object);
        }
        return object;
    }
    
    public Object visit(ASTObjectArray node, Object object) {
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            throw new UnsupportedOperationException("ASTObjectArray");
            //object = node.childrenAccept(this, object);
        }
        return object;
    }
    
    public Object visit(ASTIntegerRange node, Object object) {
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            throw new UnsupportedOperationException("ASTIntegerRange");
            //object = node.childrenAccept(this, object);
        }
        return object;
    }
    
    public Object visit(ASTMethod node, Object object) {
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            c.getRenderer().renderMethodStart(node.getMethodName());
            for(int i=1;i<node.jjtGetNumChildren();i++) {
                if (i != 1) {
                    c.getRenderer().renderToken(", ");
                }
                object = node.jjtGetChild(i).jjtAccept(this, c);
            }
            c.getRenderer().renderMethodEnd();
        }
        return object;
    }
    
    public Object visit(ASTReference node, Object object) {
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            
            String v = node.getRootString();
            
            if (c.isLocal()) {
                c.getRenderer().addLocalVar(v);
            }
            
            if (v.equals("I18N")) {
                String resourceName = "";
                for(int i=0; i<node.jjtGetNumChildren(); i++) {
                    if (i>0) {
                        resourceName += ".";
                    }
                    Node child = node.jjtGetChild(i);
                    resourceName += NodeUtils.tokenLiteral(child.getFirstToken());
                }
                if (bundle != null) {
                    c.getRenderer().renderText(bundle.getString(resourceName));
                }
            } else {
                c.getRenderer().renderReferenceStart(v);
                object = node.childrenAccept(this, object);
                c.getRenderer().renderReferenceEnd();
            }
        }
        return object;
    }
    
    public Object visit(ASTTrue node, Object object) {
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            c.getRenderer().renderToken("true");
            object = node.childrenAccept(this, object);
        }
        return object;
    }
    
    public Object visit(ASTFalse node, Object object) {
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            c.getRenderer().renderToken("false");
            object = node.childrenAccept(this, object);
        }
        return object;
    }
    
    public Object visit(ASTText node, Object object) {
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            
            String text = NodeUtils.tokenLiteral(node.getFirstToken());
            int i = text.indexOf("V2JS:");
            
            if (i >= 0 && i<text.length()-5) {
                
                if (i>0) {
                    c.getRenderer().renderText(text.substring(0,i));
                }
                
                String text2 = text.substring(i+5);
                int e = text2.indexOf("\r");
                if (e<0) {
                    e = text2.indexOf("\n");
                    if (e<0) e = text.length();
                }
                String text3 = text2.substring(0,e);
                
                String[] directives = text3.split(",| |;");
                for(int j = 0; j < directives.length; j++) {
                    String[] directive = directives[j].split("=");
                    String value = "";
                    if (directive.length > 1) {
                        value = directive[1];
                    }
                    c.getRenderer().setV2JSDirective(directive[0], value);
                }
                
                if (e<text2.length()) {
                    c.getRenderer().renderText(text2.substring(e));
                }
                
            } else {
                c.getRenderer().renderText(NodeUtils.tokenLiteral( node.getFirstToken()));
                object = node.childrenAccept(this, object);
            }
        }
        return object;
    }
    
    public Object visit(ASTIfStatement node, Object object) {
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            c.getRenderer().renderIfStart();
            object = node.jjtGetChild(0).jjtAccept(this, object);
            c.getRenderer().renderIfBody();
            //c.newLine();
            object = node.jjtGetChild(1).jjtAccept(this, object);
            c.getRenderer().renderIfEnd();
            //c.newLine();
            for(int i = 2; i < node.jjtGetNumChildren(); i++) {
                c.getRenderer().renderElseStart();
                object = node.jjtGetChild(i).jjtAccept(this, object);
            }
            
            for(int i = 2; i < node.jjtGetNumChildren(); i++) {
                c.getRenderer().renderElseEnd();
            }
            
        }
        return object;
    }
    
    public Object visit(ASTElseStatement node, Object object) {
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            //c.getRenderer().renderElseStart();
            //c.newLine();
            object = node.jjtGetChild(0).jjtAccept(this, object);
            //c.getRenderer().renderElseEnd();
            //c.newLine();
        }
        return object;
    }
    
    public Object visit(ASTElseIfStatement node, Object object) {
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            //c.getRenderer().renderElseStart();
            c.getRenderer().renderIfStart();
            object = node.jjtGetChild(0).jjtAccept(this, object);
            c.getRenderer().renderIfBody();
            //c.newLine();
            object = node.jjtGetChild(1).jjtAccept(this, object);
            c.getRenderer().renderIfEnd();
            //c.newLine();
            if (node.jjtGetNumChildren() > 2) {
                object = node.jjtGetChild(2).jjtAccept(this, object);
            }
            //c.getRenderer().renderElseEnd();
        }
        return object;
    }
    
    public Object visit(ASTSetDirective node, Object object) {
        if (object instanceof MyContext) {

            MyContext c = (MyContext) object;
            Node varObject = node.jjtGetChild(0);
            
            //String v = createVar(NodeUtils.tokenLiteral( varObject.getFirstToken()));
            String v = getVar((SimpleNode)node.jjtGetChild(0), true, c);
            c.getRenderer().renderSetVariableStart(v);
            c = (MyContext) node.jjtGetChild(1).jjtAccept(this, c);
            c.getRenderer().renderSetVariableEnd();
            
        }
        
        return object;
    }

    public Object visit(ASTStop node, Object object) {
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            throw new UnsupportedOperationException("ASTStop");
            // object = node.childrenAccept(this, object);
        }
        return object;
    }
    
    public Object visit(ASTExpression node, Object object) {
        
        if (object instanceof MyContext) {

            MyContext c = (MyContext) object;
            //c.appendBuf(NodeUtils.tokenLiteral( node.getFirstToken()) );
            c.getRenderer().renderExpressionStart();
     
            object = node.childrenAccept(this, object);

            c.getRenderer().renderExpressionEnd();
        }
       
        return object;
    }
    
    public Object visit(ASTAssignment node, Object object) {
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            throw new UnsupportedOperationException("ASTAssignment");
            //object = node.childrenAccept(this, object);
        }
        return object;
    }
    
    public Object visit(ASTOrNode node, Object object) {
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            object = node.jjtGetChild(0).jjtAccept(this, object);
            c.getRenderer().renderToken(" || ");
            object = node.jjtGetChild(1).jjtAccept(this, object);
        }
        return object;
    }
    
    public Object visit(ASTAndNode node, Object object) {
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            object = node.jjtGetChild(0).jjtAccept(this, object);
            c.getRenderer().renderToken(" && ");
            object = node.jjtGetChild(1).jjtAccept(this, object);
        }
        return object;
    }
    
    public Object visit(ASTEQNode node, Object object) {
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            object = node.jjtGetChild(0).jjtAccept(this, object);
            c.getRenderer().renderToken(" == ");
            object = node.jjtGetChild(1).jjtAccept(this, object);
        }
        return object;
    }
    
    public Object visit(ASTNENode node, Object object) {
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            object = node.jjtGetChild(0).jjtAccept(this, object);
            c.getRenderer().renderToken(" != ");
            object = node.jjtGetChild(1).jjtAccept(this, object);
        }
        return object;
    }
    
    public Object visit(ASTLTNode node, Object object) {
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            object = node.jjtGetChild(0).jjtAccept(this, object);
            c.getRenderer().renderToken(" < ");
            object = node.jjtGetChild(1).jjtAccept(this, object);
        }
        return object;
    }
    
    public Object visit(ASTGTNode node, Object object) {
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            object = node.jjtGetChild(0).jjtAccept(this, object);
            c.getRenderer().renderToken(" > ");
            object = node.jjtGetChild(1).jjtAccept(this, object);
        }
        return object;
    }
    
    public Object visit(ASTLENode node, Object object) {
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            object = node.jjtGetChild(0).jjtAccept(this, object);
            c.getRenderer().renderToken(" <= ");
            object = node.jjtGetChild(1).jjtAccept(this, object);
        }
        return object;
    }
    
    public Object visit(ASTGENode node, Object object) {
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            object = node.jjtGetChild(0).jjtAccept(this, object);
            c.getRenderer().renderToken(" >= ");
            object = node.jjtGetChild(1).jjtAccept(this, object);
        }
        return object;
    }

       public Object visit(ASTAddNode node, Object object) {
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            c.getRenderer().renderToken("( ");
            c = (MyContext) node.jjtGetChild(0).jjtAccept(this, c);
            c.getRenderer().renderToken(" + ");
            c = (MyContext) node.jjtGetChild(1).jjtAccept(this, c);
            c.getRenderer().renderToken(" )");
        }

        return object;
    }

    
    public Object visit(ASTSubtractNode node, Object object) {
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            c.getRenderer().renderToken("( ");
            c = (MyContext) node.jjtGetChild(0).jjtAccept(this, c);
            c.getRenderer().renderToken(" - ");
            c = (MyContext) node.jjtGetChild(1).jjtAccept(this, c);
            c.getRenderer().renderToken(" )");
        }
        return object;
    }
    
    public Object visit(ASTMulNode node, Object object) {
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            c.getRenderer().renderToken("( ");
            c = (MyContext) node.jjtGetChild(0).jjtAccept(this, c);
            c.getRenderer().renderToken(" * ");
            c = (MyContext) node.jjtGetChild(1).jjtAccept(this, c);
            c.getRenderer().renderToken(" )");
        }
        return object;
    }
    
    public Object visit(ASTDivNode node, Object object) {
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            c.getRenderer().renderToken("( ");
            c = (MyContext) node.jjtGetChild(0).jjtAccept(this, c);
            c.getRenderer().renderToken(" / ");
            c = (MyContext) node.jjtGetChild(1).jjtAccept(this, c);
            c.getRenderer().renderToken(" )");
        }
        return object;
    }
    
    public Object visit(ASTModNode node, Object object) {
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            c.getRenderer().renderToken("( ");
            c = (MyContext) node.jjtGetChild(0).jjtAccept(this, c);
            c.getRenderer().renderToken(" % ");
            c = (MyContext) node.jjtGetChild(1).jjtAccept(this, c);
            c.getRenderer().renderToken(" )");
        }
        return object;
    }
    
    public Object visit(ASTNotNode node, Object object) {
        if (object instanceof MyContext) {
            MyContext c = (MyContext) object;
            c.getRenderer().renderToken("!( ");
            c = (MyContext) node.jjtGetChild(0).jjtAccept(this, c);
            c.getRenderer().renderToken(" )");
        }
        return object;
    }
    
    private String createVar(String string) {
        if (string.startsWith("$")) {
            return string.substring(1);
        }
        return string;
    }
    
    private String createFunctionName(String fileName) {
        int s = fileName.indexOf("\"");
        if (s<0) { s = 0; }
        return Velocity2Js.createFunctionName(fileName.substring(s+1));
    }
    
    
    public class MyContext {
        private JSRenderer renderer = new JSRendererFast();
        private boolean local;
        
        public JSRenderer getRenderer() {
            return renderer;
        }
        
        public boolean isLocal() {
            return local;
        }
        
        public void setLocal(boolean local) {
            this.local = local;
        }
        @Override
        public String toString()
        {
            return renderer.toString();
        }
        
        
        
    }
 
    
}
