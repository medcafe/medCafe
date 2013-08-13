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
 *
 * JSRendererFast.java
 *
 * Created on 10. April 2007, 14:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/* Modified Jun 22, 2010  

Fix level and keep track of whether level is a foreach level
so that nested loops will work

*/

package at.riemers.velocity2js.jsrenderer;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 *
 * @author tobias
 */
public class JSRendererFast implements JSRenderer {
    
    private final static String REFERENCE = "Reference";
    private final static String VARIABLE = "Variable";
    private final static String IDENTIFIER = "Identifier";
    private final static String FUNCTION = "Function";
    private static Pattern getPattern = Pattern.compile("^.*([.]get[\\(]([A-Za-z][_A-Za-z0-9]*)[\\)])$");
    private int cnt=0;
    private Stack<String> contextStack = new Stack<String>();
    private StringBuffer buf = new StringBuffer();
    private StringBuffer buf2 = null;
    private final static String contextVar = "context";
    private Set<Variable> localVars = new HashSet<Variable>();
    private Set<Variable> keyVars = new HashSet<Variable>();
    private int level = 0;
    private boolean isGetFunction= false;
    private boolean isKeyReference = false;
    private ArrayList<Boolean> isForeach = new ArrayList<Boolean>();
    
    private boolean velocityCount = true;
    private boolean condenseWS = false;
    
    /** Creates a new instance of JSRendererFast */
    public JSRendererFast() {
    }
    
    private void newLine() {
        if (buf2 != null) {
            buf.append(buf2);
            buf2=null;
        }
        buf.append("\n");
    }
    
    private void append(String s) {
        if (buf2 != null) {
            buf.append(buf2);
            buf2=null;
        }
        buf.append(s);
    }
    
    private void newLineBuffered() {
        if (buf2 == null) {
            buf2 = new StringBuffer();
        }
        buf2.append("\n");
    }
    
    private void appendBuffered(String s) {
        if (buf2 == null) {
            buf2 = new StringBuffer();
        }
        buf2.append(s);
    }
    
    public void renderFunctionStart(String functionName) {
        addLocalVar("CONTEXT");
        contextStack.push(FUNCTION);
        append("function " + functionName + "(" + contextVar + ") { ");
        newLine();
        append("var t = new StringCat();");
        newLine();
        if (velocityCount) {
            appendBuffered("var velocityCount = 0;");
            newLineBuffered();
            appendBuffered("if (" +  contextVar + ".velocityCount) velocityCount=" +  contextVar + ".velocityCount;");
            newLineBuffered();
            addLocalVar("velocityCount");
        }
        
    }
    
    public void renderFunctionEnd() {
        append("return t.toString();");
        newLine();
        append("}");
        newLine();
        contextStack.pop();
    }
    
    public void renderFloatingPointLiteral(String string) {
        append(string);
    }
    
    public void renderIntegerLiteral(String string) {
        append(string);
    }
    
    public void renderStringLiteral(String string) {
        append(string);
    }
    
    public void renderIdentifier(String string) {
        
        if (contextStack.peek().equals(IDENTIFIER) || contextStack.peek().equals(REFERENCE)) {
            append(".");
        }
        append(string);
    }
    
    public void renderReferenceStart(String string) {
        
        if (!contextStack.peek().equals(VARIABLE)) {
            append("t.p( ");
        }
        
        if (contextStack.peek().equals(REFERENCE)) {
            append(".");
        } else {
            if (!localVars.contains(new Variable(string))) {
                append(contextVar + ".");
            }
	    if (keyVars.contains(new Variable(string)) && isGetFunction)
            {
                buf2 = null;
                isKeyReference = true;
                append("[");
		string = string+"]";
            }
        }
        if (string.equals("CONTEXT")) {
            append(contextVar);
        } else {
	
            append(string);

            
        }
        
        contextStack.push(REFERENCE);
    }
    
    public void renderReferenceEnd() {
        contextStack.pop();
        if (!contextStack.peek().equals(VARIABLE) && !contextStack.peek().equals(REFERENCE)) {
            append(");");
            newLine();
        }
    }
    
    public void renderMethodStart(String string) {
        if (string.equals("get"))
	{
           isGetFunction=true;
           appendBuffered(".");
           appendBuffered(string);
           appendBuffered("(");
         }
	else {
        append(".");
        append(string);
        append("(");
        }
        contextStack.push(VARIABLE);
    }
    
    public void renderMethodEnd() {
        contextStack.pop();
        if (!isGetFunction || !isKeyReference)
	{
    		append(")");
	}
	isGetFunction=false;
        isKeyReference=false;

        //newLine();
    }
    
    public void renderText(String string) {
        String text = string.replaceAll("\\r\\n", "").replaceAll("'", "\\\\'").replaceAll("\\n", "");
        if (condenseWS) {
            text = text.replaceAll(" ( )*", " ");
        }
        if (!text.equals("")) {
            append("t.p('" + text + "');");
            newLine();
        }
    }
    
    public void renderToken(String string) {
        append(string);
    }
    
    public void renderForStart(String var, String list) {
        increaseLevel();
        isForeach.add(true);       //levels are indexed starting at 1, isForeach index starts at 0.
        Matcher matcher = getPattern.matcher(list);
 
        if (matcher.matches()&& keyVars.contains(new Variable(matcher.group(2))))
        {
            list =list.replace(matcher.group(1),"["+ matcher.group(2)+"]");
        }
	if (list.endsWith(".keySet()"))
{
     String v = "i" + level;
     append(v + "=0;");
     newLine();
     addKey(var);
     append("for (" + var + " in " +list.replace(".keySet()","") + ")");
append(" {");
     newLine();
     append("velocityCount = " +v+";");
     newLine();
     append(v + "++;");
     newLine();
}
else
{
        String v = "i" + level;
        //append(" alert('for: " + v + "');");
        append("for (var " + v + "=0;  " + v + "<" + list + ".length; " + v + "++)");
        append(" {");
        newLine();
        addLocalVar(var);
        append("var " + var + " = " + list + "[" + v + "];");
        newLine();
        append("velocityCount = " + v + ";");
        newLine();
}
    }
    
    public void renderForEnd() {
        append("}");
        newLine();
        decreaseLevel();
        if (level == 0) {
            append("velocityCount = 0;");
        } else {
            isForeach.remove(level);
            int forLevel = level - 1;
            while (forLevel >= 0 && !isForeach.get(forLevel))
            {
                forLevel--;
            }
            if (forLevel >=0)
            {
                String v = "i" + (forLevel + 1);
                append("velocityCount = " + v + ";");
            }
        }
        newLine();
        
    }
    
    public String toString() {
        return buf.toString();
    }
    
    
    public Variable addLocalVar(String var) {
        Variable v = new Variable(var, level);
        localVars.add(v);
        return v;
    }
    
    public void removeLocalVar(String var) {
        Variable v = new Variable(var, level);
        localVars.remove(v);
        keyVars.remove(v);
    }
    
    public Variable addLocalVar(Variable var) {
        var.setLocal(true);
        localVars.add(var);
        return var;
    }
    public Variable addKey(String var) {
	Variable v = new Variable(var,level);
	keyVars.add(v);
        return addLocalVar(v);
        
        
    }

    
    public void renderSetVariableStart(String v) {
        Variable var = new Variable(v);
        if (!localVars.contains(var)) {
            if (!v.startsWith(contextVar + ".")) {
                append("var " + var.getName() + " = ");
                addLocalVar(v);
            } else {
                append(var.getName() + " = ");
            }
        } else {
            append(v + " = ");
        }
        contextStack.push(VARIABLE);
    }
    
    public void renderSetVariableEnd() {
        contextStack.pop();
        append(";");
        newLine();
    }
    
    public void createVariableStart() {
        contextStack.push(VARIABLE);
    }
    
    public void createVariableEnd() {
        contextStack.pop();
    }
    
    public void renderExpressionStart() {
        contextStack.push(VARIABLE);
    }
    
    public void renderExpressionEnd() {
        contextStack.pop();
    }
    
    
    /*private String renderVariable(Variable var) {
        if (!localVars.contains(var)) {
            return contextVar + "." + var.toString();
        }
        return var.toString();
    }*/
    
    public void renderIfStart() {
        increaseLevel();
        isForeach.add(false);
        append("if (");
    }
    
    public void renderIfBody() {
        append(") {");
        newLine();
    }
    
    public void renderIfEnd() {
        append("}");
        newLine();
        decreaseLevel();
        isForeach.remove(level);
    }
    
    public void renderElseStart() {
        append("else {");
        newLine();
    }
    
    public void renderElseEnd() {
        append("}");
        newLine();
    }
    
    public void renderIncludeStart() {
        addLocalVarsToContext();
        append("t.p( ");
    }
    
    public void renderIncludeEnd() {
        append(");");
        newLine();
    }
    
    public void renderFunctionCall(String functionName) {
        append(functionName);
        append("(context)");
    }
    
    public void renderDynaFunctionCallStart() {
        append("eval(");
    }
    
    public void renderDynaFunctionCallEnd() {
        append("+'(context)'");
        append(")");
    }
    
    private void addLocalVarsToContext() {
        for (Variable var : localVars) {
            if (!var.getName().equals("CONTEXT")) {
                append(contextVar + ".");
                append(var.getName());
                append("=");
                append(var.getName());
                append(";");
                newLine();
            }
        }
    }
    
    private void increaseLevel() {
        level ++;
    }
    
    private void decreaseLevel() {
        level --;
        Iterator<Variable> it = localVars.iterator();
        while (it.hasNext()) {
            Variable v = it.next();
            if (v.getLevel() > level) {
                it.remove();
            }
        }
    }
    
    
    public Set<Variable> getLocalVariables() {
        return localVars;
    }
    
    public void setLocalVariables(Set<Variable> localVars) {
        this.localVars.clear();
        this.localVars.addAll(localVars);
    }
    
    public void setV2JSDirective(String directive, String value) {
        if (directive.equals("CONDENSE_WS")) {
            condenseWS = Boolean.parseBoolean(value);
        }
        if (directive.equals("VELOCITYCOUNT")) {
            velocityCount = Boolean.parseBoolean(value);
            buf2=null;
            removeLocalVar("velocityCount");
        }
    }
    
}

