package uoc.ds.pr.util;

import edu.uoc.ds.adt.helpers.KeyValue;
import edu.uoc.ds.adt.sequential.DictionaryArrayImpl;
import edu.uoc.ds.adt.sequential.FiniteContainer;

import java.util.Comparator;

public class DictionaryOrderedVector<K,V> extends DictionaryArrayImpl<K,V> implements FiniteContainer<V> {
	private static final long serialVersionUID = -3128510987753729875L;

	private static final int KEY_NOT_FOUND = -1;

    private Comparator<K> comparator;

    public DictionaryOrderedVector(int max, Comparator<K> comparator) {
        super(max);
        this.comparator = comparator;
    }

    public boolean isEmpty() {
        return (super.n == super.dictionary.length);
    }

    /**
     * An element is added in the last position and it is rearranged by placing itself in its location,
     * according to the order defined by the comparator
     */
    @Override
    public void put(K clave, V obj) {
        super.put(clave, obj);

        // add Key-Value
        int i = n - 1;



        KeyValue kv;
        KeyValue last = dictionary[n - 1];

        while (i >= 0 ) {
            kv = dictionary[i];

            if (comparator.compare((K) kv.getKey(), clave) > 0) {
                // swap
                dictionary[i] = last;
                dictionary[i+1] = kv;
                last = dictionary[i];
            }

            i--;
        }
    }


    /**
     * method that get an element over the ordered vector
     */
    @Override
    public V get(K clau) {
        int pos = binarySearch(clau, 0, n-1);
        return (pos != KEY_NOT_FOUND ? dictionary[pos].getValue() : null);
    }

    /**
     * private method that performs a binary search
     *
     * @param key the key
     * @param imin minimum position
     * @param imax maximum position
     * @return
     */
    private int binarySearch(K key, int imin, int imax)
    {
        // test if array is empty
        if (imax < imin) {
            // set is empty, so return value showing not found
            return KEY_NOT_FOUND;
        } else {
            // calculate midpoint to cut set in half
            int imid = midpoint(imin, imax);

            // three-way comparison
            if (comparator.compare(dictionary[imid].getKey(), key) > 0) {
                // key is in lower subset
                return binarySearch(key, imin, imid-1);
            } else if (comparator.compare(dictionary[imid].getKey(), key) < 0) {
                // key is in upper subset
                return binarySearch(key, imid+1, imax);
            } else {
                // key has been found
                return imid;
            }
        }
    }

    /**
     * private method that calculates the midpoint
     */
    private int midpoint(int imin, int imax) {
        return imin + ((imax - imin) / 2);
    }
}
