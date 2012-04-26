/*
 * Copyright (C) 2010 Jan-Kees van Andel.
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

/**
 * Utility for JavaBean operations.
 *
 * $Author: jankeesvanandel $
 * $Revision: 23 $
 */
class BeanUtils {

    /**
     * A valid getter method must start with one of these prefixes.
     */
    private static final String[] GETTER_PREFIXES = new String[] { "get", "is", "has" };

    /**
     * Determine if the given methodName corresponds to a property and if it does,
     * return that property name.
     *
     * @param methodName The name of the method.
     * @return The property name, or null if this method is not an accessor.
     */
    static String determinePropertyName(final String methodName) {
        for (final String prefix : GETTER_PREFIXES) {
            if (methodName.startsWith(prefix)) {
                String propertyName = methodName.replaceFirst(prefix, "");
                propertyName = Character.toString(Character.toLowerCase(propertyName.charAt(0))) + propertyName.substring(1);
                return propertyName;
            }
        }
        return null;
    }

    /**
     * Create a setter name for the given property name.
     *
     * @param propertyName The property name.
     * @return The name of the corresponding setter.
     */
    static String determineSetter(final String propertyName) {
        return "set" + Character.toString(Character.toUpperCase(propertyName.charAt(0))) + propertyName.substring(1);
    }

    /**
     * Check to see if the given methodSignature is really a method we can process.
     *
     * @param methodSignature The method signature.
     * @return true if methodSignature could be a valid method, false otherwise.
     */
    static boolean couldBeAGetterMethod(final String methodSignature) {
        return methodSignature.contains("()");
    }

    /**
     * Determine the method name for the given method signature.
     *
     * @param methodSignature The method signature.
     * @return The method name, without complete signature (e.g. parameters).
     */
    static String determineMethodName(final String methodSignature) {
        return methodSignature.substring(0, methodSignature.indexOf('('));
    }

    /**
     * Determine if the given method name is a valid getter method.
     *
     * @param methodName The method name.
     * @return true if the method is a getter, false otherwise.
     */
    static boolean isGetter(final String methodName) {
        for (final String getterPrefix : GETTER_PREFIXES) {
            if (methodName.startsWith(getterPrefix)) {
                // A method called "get" is no getter
                int prefixLength = getterPrefix.length();
                if (methodName.length() > prefixLength) {
                    return true;
                }
            }
        }

        return false;
    }
}
