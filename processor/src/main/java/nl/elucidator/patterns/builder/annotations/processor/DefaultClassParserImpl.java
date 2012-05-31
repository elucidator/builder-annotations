/*
 * Copyright (C) 2010 Jan-Kees van Andel.
 * Copyright (C) 2012 Pieter van der Meer (pieter(at)elucidator.nl)
 *
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
 */

package nl.elucidator.patterns.builder.annotations.processor;

import net.jcip.annotations.Immutable;
import nl.elucidator.patterns.builder.annotations.Required;

import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVisitor;
import javax.lang.model.util.SimpleTypeVisitor6;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Default implementation of {ClassParser}.
 */
public class DefaultClassParserImpl implements ClassParser {
    /**
     * This Visitor determines the superclass of the current element.
     */
    private static final ElementVisitor<Object, SuperClassInfo> SUPERCLASS_VISITOR = new ElementVisitor<Object, SuperClassInfo>() {
        public Object visit(Element e, SuperClassInfo info) {
            return null;
        }

        public Object visit(Element e) {
            return null;
        }

        public Object visitPackage(PackageElement e, SuperClassInfo info) {
            return null;
        }

        public Object visitType(TypeElement e, SuperClassInfo info) {
            // Get the qualified name of the type.
            info.qualifiedName = e.getQualifiedName().toString();
            return null;
        }

        public Object visitVariable(VariableElement e, SuperClassInfo info) {
            return null;
        }

        public Object visitExecutable(ExecutableElement e, SuperClassInfo info) {
            return null;
        }

        public Object visitTypeParameter(TypeParameterElement e, SuperClassInfo info) {
            return null;
        }

        public Object visitUnknown(Element e, SuperClassInfo info) {
            return null;
        }
    };

    /**
     * Verifies if the class has  the {@link Immutable} annotation
     */
    private static final SimpleTypeVisitor6<Boolean, Object> IMMUTABLE_VISITOR = new SimpleTypeVisitor6<Boolean, Object>() {
        @Override
        public Boolean visitDeclared(final DeclaredType t, final Object o) {
            final Immutable immutable = t.asElement().getAnnotation(Immutable.class);
            return (immutable != null);
        }
    };

    /**
     * Visitor to determine the return type of the method.
     */
    private static final TypeVisitor<String, Object> GET_RETURNTYPE_VISITOR
            = new SimpleTypeVisitor6<String, Object>() {
        /** {@inheritDoc} */
        @Override
        public String visitExecutable(ExecutableType t, Object o) {
            return t.getReturnType().toString();
        }
    };

    @Override
    public ClassMetaData readMetaData(TypeElement element) {
        final SuperClassInfo superClassInfo = determineSuperClass(element);
        final ClassProperty[] properties = listOptionalProperties(element);
        final boolean isInterface = isInterface(element);

        return new ClassMetaData(superClassInfo, properties, isInterface);
    }

    private boolean isInterface(TypeElement element) {
        ObjectWrapper<Boolean> wrapper = new ObjectWrapper<Boolean>(false);

        element.asType().accept(new SimpleTypeVisitor6<Void, ObjectWrapper<Boolean>>() {
            @Override
            public Void visitDeclared(DeclaredType t, ObjectWrapper<Boolean> result) {
                result.set(t.asElement().getKind().equals(ElementKind.INTERFACE));
                return null;
            }
        }, wrapper);

        return wrapper.get();
    }

    /**
     * Determine the superclass for this element.
     *
     * @param element The current element.
     * @return The superclass metadata.
     */
    SuperClassInfo determineSuperClass(TypeElement element) {
        SuperClassInfo info = new SuperClassInfo();
        element.accept(SUPERCLASS_VISITOR, info);

        return info;
    }

    /**
     * Go through all properties in the type hierarchy.
     *
     * @param element The element for which the properties must be listed.
     * @return An array of {ClassProperty}s.
     */
    ClassProperty[] listOptionalProperties(final TypeElement element) {
        List<TypeElement> hierarchy = createTypeHierarchy(element);
        Set<ClassProperty> ret = processTypeHierarchy(hierarchy);
        return ret.toArray(new ClassProperty[ret.size()]);
    }

    /**
     * Determine all superclasses/interfaces for which properties must be listed.
     *
     * @param element The element for which a type hierarchy must be created.
     * @return A List of all {TypeElement}s that have properties that must be processed.
     */
    List<TypeElement> createTypeHierarchy(final TypeElement element) {
        final List<TypeElement> hierarchy = new ArrayList<TypeElement>();

        final List<? extends TypeMirror> interfaces = element.getInterfaces();
        for (final TypeMirror interfaze : interfaces) {
            final Boolean includeSuperInterface = interfaze.accept(IMMUTABLE_VISITOR, null);

            if (includeSuperInterface != null && includeSuperInterface) {
                final List<TypeElement> superHierarchy = createTypeHierarchy((TypeElement) ((DeclaredType) interfaze).asElement());
                hierarchy.addAll(superHierarchy);
            }
        }

        hierarchy.add(element);

        return hierarchy;
    }

    /**
     * Work through the given types and returning a Set with properties.
     *
     * @param hierarchy The type hierarchy, listed.
     * @return An ordered Set with properties.
     */
    Set<ClassProperty> processTypeHierarchy(final List<TypeElement> hierarchy) {
        final Set<ClassProperty> properties = new LinkedHashSet<ClassProperty>();

        for (final TypeElement typeElement : hierarchy) {
            final List<Element> methods = new ArrayList<Element>(typeElement.getEnclosedElements());
            for (final Element method : methods) {
                final String signature = method.toString();
                if (BeanUtils.couldBeAGetterMethod(signature)) {
                    final String methodName = BeanUtils.determineMethodName(signature);
                    if (BeanUtils.isGetter(methodName)) {
                        final String propertyName = BeanUtils.determinePropertyName(methodName);
                        final String setter = BeanUtils.determineSetter(propertyName);

                        final ClassProperty classProperty = new ClassProperty();
                        classProperty.name = propertyName;
                        classProperty.type = method.asType().accept(GET_RETURNTYPE_VISITOR, null);
                        classProperty.getter = methodName;
                        classProperty.setter = setter;
                        classProperty.required = (method.getAnnotation(Required.class) != null);
                        properties.add(classProperty);
                    }
                }
            }
        }

        return properties;
    }

    private class ObjectWrapper<T> {
        private T wrapped;

        private ObjectWrapper(T wrapped) {
            this.wrapped = wrapped;
        }

        private T get() {
            return this.wrapped;
        }

        private void set(T wrapped) {
            this.wrapped = wrapped;
        }
    }
}
