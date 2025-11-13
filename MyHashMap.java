import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static int DEFAULT_INITIAL_CAPACITY = 4;
    private static int MAXIMUM_CAPACITY = 1 << 30;
    private static float DEFAULT_MAX_LOAD_FACTOR = 0.75f;

    private int capacity;
    private float loadFactorThreshold;
    private int size = 0;

    private LinkedList<MyMap.Entry<K, V>>[] table;

    public MyHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_MAX_LOAD_FACTOR);
    }

    public MyHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_MAX_LOAD_FACTOR);
    }

    public MyHashMap(int initialCapacity, float loadFactorThreshold) {
        if (initialCapacity > MAXIMUM_CAPACITY) {
            this.capacity = MAXIMUM_CAPACITY;
        } else {
            this.capacity = trimToPowerOf2(initialCapacity);
        }
        this.loadFactorThreshold = loadFactorThreshold;
        table = new LinkedList[capacity];
    }

    private int trimToPowerOf2(int initialCapacity) {
        int c = 1;
        while (c < initialCapacity) {
            c <<= 1;
        }
        return c;
    }

    @Override
    public void clear() {
        size = 0;
        removeEntries();
    }

    private void removeEntries() {
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null) {
                table[i].clear();
            }
        }
    }

    private static int supplementalHash(int h) {
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    private int hash(int hashCode) {
        return supplementalHash(hashCode) & (capacity - 1);
    }

    private int hash(K key) {
        return hash(key.hashCode());
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(V value) {
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null) {
                for (MyMap.Entry<K, V> entry : table[i]) {
                    if (entry.getValue().equals(value)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Set<MyMap.Entry<K, V>> entrySet() {
        Set<MyMap.Entry<K, V>> set = new HashSet<>();
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null) {
                for (MyMap.Entry<K, V> entry : table[i]) {
                    set.add(entry);
                }
            }
        }
        return set;
    }

    @Override
    public V get(K key) {
        int bucketIndex = hash(key);
        LinkedList<MyMap.Entry<K, V>> bucket = table[bucketIndex];

        if (bucket != null) {
            for (MyMap.Entry<K, V> entry : bucket) {
                if (entry.getKey().equals(key)) {
                    return entry.getValue();
                }
            }
        }

        return null;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<>();
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null) {
                for (MyMap.Entry<K, V> entry : table[i]) {
                    set.add(entry.getKey());
                }
            }
        }
        return set;
    }

    @Override
    public V put(K key, V value) {
        // إذا key موجود، عدّل قيمته
        if (get(key) != null) {
            int bucketIndex = hash(key);
            LinkedList<MyMap.Entry<K, V>> bucket = table[bucketIndex];
            for (MyMap.Entry<K, V> entry : bucket) {
                if (entry.getKey().equals(key)) {
                    V old = entry.getValue();
                    entry.setValue(value);
                    return old;
                }
            }
        }

        // إذا صار اللود عالي نعمل rehash
        if (size >= capacity * loadFactorThreshold) {
            if (capacity == MAXIMUM_CAPACITY) {
                throw new RuntimeException("Exceeding maximum capacity");
            }
            rehash();
        }

        int bucketIndex = hash(key);
        if (table[bucketIndex] == null) {
            table[bucketIndex] = new LinkedList<>();
        }

        table[bucketIndex].add(new MyMap.Entry<>(key, value));
        size++;
        return value;
    }

    private void rehash() {
        Set<MyMap.Entry<K, V>> set = entrySet();
        capacity <<= 1;               // double capacity
        table = new LinkedList[capacity];
        size = 0;

        for (MyMap.Entry<K, V> entry : set) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void remove(K key) {
        int bucketIndex = hash(key);
        LinkedList<MyMap.Entry<K, V>> bucket = table[bucketIndex];

        if (bucket != null) {
            Iterator<MyMap.Entry<K, V>> it = bucket.iterator();
            while (it.hasNext()) {
                MyMap.Entry<K, V> entry = it.next();
                if (entry.getKey().equals(key)) {
                    it.remove();
                    size--;
                    break;
                }
            }
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Set<V> values() {
        Set<V> set = new HashSet<>();
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null) {
                for (MyMap.Entry<K, V> entry : table[i]) {
                    set.add(entry.getValue());
                }
            }
        }
        return set;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (MyMap.Entry<K, V> entry : entrySet()) {
            if (!first) sb.append(", ");
            sb.append(entry.getKey()).append("=").append(entry.getValue());
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }
}
