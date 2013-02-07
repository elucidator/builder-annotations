package nl.elucidator.patterns.builder.annotations.test;

import nl.elucidator.patterns.builder.annotations.test.CollectionsSampleImpl;
import org.junit.Test;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * test cases for immutable collections return values
 */
public class ImmutableCollections {

    CollectionsSample collectionsSample = new CollectionsSampleImpl.Builder().build();

    @Test
    public void list() {
        assertNotNull(collectionsSample.getListString());
        assertTrue(collectionsSample.getListString().isEmpty());
    }

    @Test
    public void map() {
        Map map = collectionsSample.getMapStringObject();
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    @Test
    public void sortedMap() {
        SortedMap sortedMap = collectionsSample.getSortedMapStringObject();
        assertNotNull(sortedMap);
        assertTrue(sortedMap.isEmpty());
    }

    @Test
    public void set() {
        Set set = collectionsSample.getSetString();
        assertNotNull(set);
        assertTrue(set.isEmpty());
    }

    @Test
    public void sortedSet() {
        SortedSet sorted = collectionsSample.getSortedSet();
        assertNotNull(sorted);
        assertTrue(sorted.isEmpty());
    }
}
