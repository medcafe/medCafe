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
 * JSRenderer.java
 *
 * Created on 24. April 2007, 08:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package at.riemers.velocity2js.jsrenderer;

import java.util.Set;

/**
 *
 * @author tobias
 */
public interface JSRenderer {
 
    public void renderFunctionStart(String functionName);
    
    public void renderFunctionEnd();
    
    public void renderFloatingPointLiteral(String string);
    
    public void renderIntegerLiteral(String string);
    
    public void renderStringLiteral(String string);
    
    public void renderIdentifier(String string);
    
    public void renderReferenceStart(String string);
    
    public void renderReferenceEnd();
    
    public void renderMethodStart(String string);
    
    public void renderMethodEnd();
    
    public void renderText(String string);
    
    public void renderToken(String string);
    
    public void renderForStart(String var, String list);
    
    public void renderForEnd();        
    
    public Variable addLocalVar(String var);
    
    public void renderSetVariableStart(String v);
    
    public void renderSetVariableEnd();
    
    public void createVariableStart();
    
    public void createVariableEnd();
    
    public void renderExpressionStart();
    
    public void renderExpressionEnd();        
    
    public void renderIfStart();
    
    public void renderIfBody();
    
    public void renderIfEnd();
    
    public void renderElseStart();
    
    public void renderElseEnd();

    public void renderIncludeStart();
    
    public void renderIncludeEnd();

    public void renderFunctionCall(String functionName);

    public void renderDynaFunctionCallStart();
    public void renderDynaFunctionCallEnd();

    public Set<Variable> getLocalVariables();
    public void setLocalVariables(Set<Variable> localVars);

    void setV2JSDirective(String directive, String value);
    
}
