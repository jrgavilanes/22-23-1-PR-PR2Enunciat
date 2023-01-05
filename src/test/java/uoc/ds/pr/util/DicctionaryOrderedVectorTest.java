package uoc.ds.pr.util;

import edu.uoc.ds.traversal.Iterator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;

public class DicctionaryOrderedVectorTest {
    private static final int LEN = 10;

    Comparator<String> CMP = (arg0, arg1) -> arg0.compareTo(arg1);

    DictionaryOrderedVector<String, Integer> v ;

    @Before
    public void setUp() {
        v = new DictionaryOrderedVector<String, Integer>(LEN, CMP);
        v.put("09", 9);
        v.put("07", 7);
        v.put("02", 2);
        v.put("03", 3);
        v.put("04", 4);
        v.put("05", 5);
        v.put("06", 6);
        v.put("01", 1);
        Assert.assertEquals(8, v.size());
    }

    @Test
    public void test() {
        Assert.assertFalse(v.isEmpty());

        Iterator<Integer> it = v.values();
        Assert.assertEquals(1, it.next().intValue());
        Assert.assertEquals(2, it.next().intValue());
        Assert.assertEquals(3, it.next().intValue());
        Assert.assertEquals(4, it.next().intValue());
        Assert.assertEquals(5, it.next().intValue());
        Assert.assertEquals(6, it.next().intValue());
        Assert.assertEquals(7, it.next().intValue());
        Assert.assertEquals(9, it.next().intValue());
        Assert.assertFalse(it.hasNext());

        v.put("09", 9);
        v.put("10", 10);
        v.put("11", 11);

        Assert.assertTrue(v.isFull());

        it = v.values();
        Assert.assertEquals(1, it.next().intValue());
        Assert.assertEquals(2, it.next().intValue());
        Assert.assertEquals(3, it.next().intValue());
        Assert.assertEquals(4, it.next().intValue());
        Assert.assertEquals(5, it.next().intValue());
        Assert.assertEquals(6, it.next().intValue());
        Assert.assertEquals(7, it.next().intValue());
        Assert.assertEquals(9, it.next().intValue());
        Assert.assertEquals(10, it.next().intValue());
        Assert.assertEquals(11, it.next().intValue());
        Assert.assertFalse(it.hasNext());

        Assert.assertEquals(1, v.get("01").intValue());
        Assert.assertEquals(5, v.get("05").intValue());
        Assert.assertEquals(11, v.get("11").intValue());

        // NOT FOUND
        Assert.assertNull( v.get("1"));
        Assert.assertNull(v.get("5"));
    }

}
