///*
// * Copyright (C) 2010 Jan-Kees van Andel.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package org.jkva.makebuilder.test.domain;
//
//import net.jcip.annotations.Immutable;
//
///**
// * Test for concrete class support.
// *
// * $Author: $
// * $Revision: $
// */
//@Immutable
//public class ConcreteClassExample {
//    public static final String SOME_CONSTANT = "yada yada";
//
//    private final String message;
//
//    public ConcreteClassExample(String message) {
//        this.message = message;
//    }
//
//    public String sayHello(String name) {
//        return message + " " + name;
//    }
//
//    public static void main(String[] args) {
//        final String message = new ConcreteClassExample("Hello").sayHello("Jan-Kees");
//        System.out.println("message = " + message);
//    }
//
//    public String getMessage() {
//        return message;
//    }
//}
