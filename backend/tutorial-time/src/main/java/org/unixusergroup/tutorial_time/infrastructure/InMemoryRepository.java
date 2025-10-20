/** 
 * @author Martin
 */
package org.unixusergroup.tutorial_time.infrastructure;

import java.util.Map;
import java.util.HashMap;

/** Basic in-memory repository class */
public abstract class InMemoryRepository<K, V> {

    public Map<K, V> data;

    public InMemoryRepository() {
        data = new HashMap<>();
    }

    public void insert(K key, V value) {
        data.put(key, value);
    }

    public V retrieve(K key) {
        return data.get(key);
    }

}
