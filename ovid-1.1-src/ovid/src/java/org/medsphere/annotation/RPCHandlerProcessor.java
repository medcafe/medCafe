// <editor-fold defaultstate="collapsed">
/*
 * Copyright (C) 2011  Medsphere Systems Corporation
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
package org.medsphere.annotation;

import java.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.AbstractElementVisitor6;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

/**
 * This is an annotation processor for RPCHandler annotations. It validates that
 * the annotation has been applied to method with the right signature and
 * access level, and verifies that it may be invoked either statically or by
 * creating a temporary "this" via the no-argument constructor.
 */
@SupportedAnnotationTypes(value = {"org.medsphere.annotation.RPCHandler"})
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class RPCHandlerProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        Messager vMessager = processingEnv.getMessager();
        MyVisitor myVisitor = new MyVisitor();

        for (Element e : roundEnv.getElementsAnnotatedWith(RPCHandler.class)) {
            String errorMsg = e.accept(myVisitor, null);
            if (errorMsg != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("RPCHandler error: ").append(errorMsg);
                sb.append(" at '").append(roundEnv.getRootElements().iterator().next().toString());
                sb.append(".").append(e.getSimpleName()).append("()'");
                vMessager.printMessage(Kind.ERROR, sb.toString());
            }
        }
        return false;
    }

    class MyVisitor extends AbstractElementVisitor6<String, Void> {

        @Override
        public String visitExecutable(ExecutableElement e, Void p) {
            List<? extends VariableElement> params = e.getParameters();
            if (params.size() != 1) {
                return "Method must have only one argument and it must be of type 'com.medsphere.vistarpc.VistaRPC'.";
            }
            VariableElement param = params.get(0);
            TypeElement vrpcType = processingEnv.getElementUtils().getTypeElement("com.medsphere.vistarpc.VistaRPC");
            if (!processingEnv.getTypeUtils().isSubtype(param.asType(), vrpcType.asType())) {
                return "Argument must be of type 'com.medsphere.vistarpc.VistaRPC'.";
            }
            TypeElement vRespType = processingEnv.getElementUtils().getTypeElement("com.medsphere.vistarpc.RPCResponse");
            if (!processingEnv.getTypeUtils().isSubtype(e.getReturnType(), vRespType.asType())) {
                return "Must return a value of type 'com.medsphere.vistarpc.RPCResponse'.";
            }
            if (!e.getModifiers().contains(Modifier.PUBLIC)) {
                return "Method must be public.";
            }
            if (!e.getEnclosingElement().getModifiers().contains(Modifier.PUBLIC)) {
                return "Enclosing class must be public.";
            }
            for (Element element = e.getEnclosingElement(); !element.getEnclosingElement().getKind().equals(ElementKind.PACKAGE); element=element.getEnclosingElement()) {
                if (!element.getModifiers().contains(Modifier.STATIC)) {
                    return "Inner class '"+e.getEnclosingElement().toString()+"' must be static.";
                }
            }
            RPCHandler annotation = e.getAnnotation(RPCHandler.class);
            if (annotation.rpc().isEmpty() && annotation.name().isEmpty()) {
                return "Annotations must specify either 'rpc' or 'name', or both.";
            }
            if (!e.getModifiers().contains(Modifier.STATIC)) {
                boolean foundConstructor = false;
                List<? extends Element> siblings = e.getEnclosingElement().getEnclosedElements();
                List<ExecutableElement> constructors = ElementFilter.constructorsIn(siblings);
                for (ExecutableElement constructor : constructors) {
                    if (constructor.getParameters().isEmpty()) {
                        if (constructor.getModifiers().contains(Modifier.PUBLIC)) {
                            foundConstructor = true;
                        }
                        break;
                    }
                }
                if (!foundConstructor) {
                    return "Methods must be static, or enclosing class must have a public no-argument constructor.";
                }
            }
            return null;
        }

        @Override
        public String visitPackage(PackageElement e, Void p) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String visitType(TypeElement e, Void p) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String visitVariable(VariableElement e, Void p) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String visitTypeParameter(TypeParameterElement e, Void p) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
