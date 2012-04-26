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
package nl.elucidator.patterns.builder.annotations.test;

import net.jcip.annotations.Immutable;
import org.joda.time.DateTime;

/**
 * Example type that uses the JCiP annotation: "Immutable" to indicate immutability.
 * The process may generate a processor and implementation for this type.
 */
@Immutable
public interface JCiPExample {
    String getMessage();
    boolean isActive();
    DateTime getTime();
}
