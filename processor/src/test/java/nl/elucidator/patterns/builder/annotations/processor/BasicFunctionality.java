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

package nl.elucidator.patterns.builder.annotations.processor;/*
 * Copyright  2010-2012 Pieter van der Meer (pieter@pieni.nl)
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

import org.junit.Test;

import javax.annotation.processing.Processor;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class BasicFunctionality extends AbstractAnnotationProcessorTest {
    @Override
    protected Collection<Processor> getProcessors() {
        return Arrays.<Processor>asList(new MakeBuilderProcessor());
    }

    //
//    @Test
//    @Ignore
//    public void xx() {
//
//    }
//
    @Test
    public void leafAnnotationOnNonCompositeMember() {
        List<Diagnostic<? extends JavaFileObject>> diagnostics = compileTestCase("nl/elucidator/patters/builder/annotations/processor/Address.java");
        int i = 0;
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
            System.out.println("diagnostic[" + i++ + "] = " + diagnostic + diagnostic.getLineNumber());
        }
        assertCompilationReturned(Diagnostic.Kind.NOTE, -1, diagnostics);
        assertCompilationSuccessful(diagnostics);
        System.out.println("diagnostics = " + diagnostics.get(0).getSource());
    }
}
