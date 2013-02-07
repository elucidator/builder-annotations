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

package nl.elucidator.patterns.builder.annotations.test;

import nl.elucidator.patterns.builder.annotations.test.ImmutableInterfaceTestImpl;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * This runner is used as a simple concurrency test.
 */
public class RunnerTest {
    private static final AtomicReference<Person> P = new AtomicReference<Person>();

    private static final int noOfRunners = 1000;

    private static final CountDownLatch START = new CountDownLatch(1);

    private static final CountDownLatch STOP = new CountDownLatch(noOfRunners);

    public static void main(String[] args) throws InterruptedException {
        SetUpData setUpData = setUpData();

        Person person1 = setUpData.getPerson1();
        Person person2 = setUpData.getPerson2();
        Random random = setUpData.getRandom();
        Address addressOther = setUpData.getAddress();
        AtomicLong counter = setUpData.getCounter();

        ExecutorService threadPool = Executors.newFixedThreadPool(noOfRunners);

        for (int i = 0; i < noOfRunners; i++) {
            threadPool.execute(new MyRunnable(person1, person2, random, addressOther, counter));
        }
        START.countDown();
        STOP.await();

        System.out.println("counter.get() = " + counter.get());

        if (!threadPool.isTerminated()) {
            threadPool.shutdown();
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        }
    }

    private static class MyRunnable implements Runnable {
        private final Person person1;
        private final Person person2;
        private final Random random;
        private final Address address;
        private final AtomicLong counter;

        public MyRunnable(Person person1, Person person2, Random random, Address address, AtomicLong counter) {
            this.person1 = person1;
            this.person2 = person2;
            this.random = random;
            this.address = address;
            this.counter = counter;
        }

        public void run() {
            try {
                START.await();
                runBatch();
            } catch (InterruptedException e) {
                Thread.interrupted();
            } finally {
                STOP.countDown();
            }
        }

        private void runBatch() {
            for (int i = 0; i < 10000; i++) {
                runner();
            }
        }

        private void runner() {
            Person person = P.get();
            assertion(person);

            addRandomness();

            counter.incrementAndGet();
        }

        private void addRandomness() {
//            if (random.nextBoolean()) {
//                P.set(person1);
//            } else {
//                P.set(person2);
//            }
//
//            if (random.nextBoolean()) {
//                P.set(((PersonBuilderImpl)P.get().processor()).setId(-1L).setAddress(new AddressBuilderImpl().build()).build());
//            }
//
//            if (random.nextBoolean()) {
//                P.set(((PersonBuilderImpl)P.get().processor()).setId(-1L).setFirstName("" + random.nextInt()).build());
//            }
//
//            if (random.nextBoolean()) {
//                P.set(((PersonBuilderImpl)P.get().processor()).setId(-1L).setEmployer(new CompanyImpl(1L, "1", address, null, new Date(), null)).build());
//            }
//
//            if (random.nextInt(100) < 10) {
//                P.set(((PersonBuilderImpl)P.get().processor()).setId(1L).build());
//            } else if (random.nextInt(100) < 10) {
//                P.set(((PersonBuilderImpl) P.get().processor()).setId(2L).build());
//            }
        }

        private void assertion(Person person) {
            if (person.getId() == 1L) {
                assert person.equals(person1);
            } else if (person.getId() == 2L) {
                assert person.equals(person2);
            }
        }
    }

    private static class SetUpData {
        private Address address;
        private Person person1;
        private Person person2;
        private AtomicLong counter;
        private Random random;

        public Address getAddress() {
            return address;
        }

        public Person getPerson1() {
            return person1;
        }

        public Person getPerson2() {
            return person2;
        }

        public AtomicLong getCounter() {
            return counter;
        }

        public Random getRandom() {
            return random;
        }
    }

    private static SetUpData setUpData() {
//        SetUpData setUpData = new SetUpData();
//        Address addressMe = new AddressBuilderImpl("dummyFromSuperInterface", "Some Street", 2, null, "Some town", Country.NETHERLANDS).build();
//        assert addressMe.getStreet().equals("Some Street");
//
//        addressMe = ((AddressBuilderImpl) addressMe.processor()).setNumber(2).setSuffix("B").build();
//        assert addressMe.getStreet().equals("Some Street");
//        assert addressMe.getNumber() == 2;
//        assert addressMe.getSuffix().equals("B");
//        assert addressMe.getCity().equals("Some Town");
//        assert addressMe.getCountry().equals(Country.NETHERLANDS);
//
//        setUpData.address = new AddressImpl("dummyFromSuperInterface", "Other Street", 12, "A", "Utrecht", Country.NETHERLANDS);
//
//        final Address addressOrdina = new AddressImpl("dummyFromSuperInterface", "Ringwade", 1, null, "Nieuwegein", Country.NETHERLANDS);
//        Company ordina = new CompanyBuilderImpl(1L, "Ordina", addressOrdina, addressOrdina, new Date(), null).build();
//        assert ordina.getId() == 1L;
//        assert ordina.getName().equals("Ordina");
//
//        final Address addressOtherCompany = new AddressImpl("dummyFromSuperInterface", "Street", 1, null, "Utrecht", Country.NETHERLANDS);
//        Company otherCompany = ((CompanyBuilderImpl)ordina.processor()).setId(2L).setName("Yada yada").setAddress(addressOtherCompany).setPostalAddress(addressOtherCompany).build();
//        assert otherCompany.getId() == 2L;
//        assert otherCompany.getName().equals("Yada yada");
//
//        setUpData.person1 = new PersonBuilderImpl().setId(1L).setFirstName("Jan-Kees").setMiddleName("van").setLastName("Andel").setBirthDate(new Date()).setAddress(addressMe).setEmployer(ordina).build();
//        assert setUpData.person1.getFirstName().equals("Jan-Kees");
//
//        setUpData.person2 = new PersonBuilderImpl().setId(2L).setFirstName("FirstName").setLastName("LastName").setBirthDate(new Date()).setAddress(setUpData.address).setEmployer(otherCompany).build();
//        assert setUpData.person2.getFirstName().equals("FirstName");
//
//        P.set(setUpData.person1);
//        setUpData.counter = new AtomicLong(0);
//
//        setUpData.random = new Random();
//        return setUpData;
        return null;
    }


    @Test
    public void testRunner() {
        ImmutableInterfaceTest object = new ImmutableInterfaceTestImpl.Builder(4545L) //
                .name("asd")
                .build();
        assertEquals(4545L, object.getId());

        // Simple copy
        ImmutableInterfaceTest newObject = ((ImmutableInterfaceTestImpl) object).builder().build();
        assertNotSame(newObject, object);
        assertEquals(object.getId(), newObject.getId());
        assertEquals(object.getName(), newObject.getName());

        // Change a required property
        newObject = ((ImmutableInterfaceTestImpl) object).builder() //
                .id(1234L) //
                .build();
        assertNotSame(newObject, object);
        assertEquals(4545L, object.getId());
        assertEquals(1234L, newObject.getId());
        assertEquals(object.getName(), newObject.getName());

        // Change an optional property
        newObject = ((ImmutableInterfaceTestImpl) object).builder() //
                .name("newValue") //
                .build();
        assertNotSame(newObject, object);
        assertEquals(object.getId(), newObject.getId());
        assertEquals("newValue", newObject.getName());
        assertEquals("asd", object.getName());
    }
}
