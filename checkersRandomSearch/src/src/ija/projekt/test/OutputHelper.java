/**
 */
package ija.projekt.test;

/**
 * 
 * @author David Molnar
 */
public class OutputHelper {

    /**
     * 
     * @param description
     * @param object
     */
    public static void assertNull(String description, Object object) {
        try {
            org.junit.Assert.assertNull(description, object);
            printMessage(description, true);
        } catch (AssertionError e) {
            printMessage(description, false);
            throw e;
        }
    }

    /**
     * 
     * @param description
     * @param b
     */
    public static void assertTrue(String description, boolean b) {
        try {
            org.junit.Assert.assertTrue(description, b);
            printMessage(description, true);
        } catch (AssertionError e) {
            printMessage(description, false);
            throw e;
        }
    }

    /**
     * 
     * @param description
     * @param obj1
     * @param obj2
     */
    public static void assertNotSame(String description, Object obj1,
            Object obj2) {
        try {
            org.junit.Assert.assertNotSame(description, obj1, obj2);
            printMessage(description, true);
        } catch (AssertionError e) {
            printMessage(description, false);
            throw e;
        }
    }

    /**
     * 
     * @param description
     * @param obj1
     * @param obj2
     */
    public static void assertEquals(String description, Object obj1, Object obj2) {
        try {
            org.junit.Assert.assertEquals(description, obj1, obj2);
            printMessage(description, true);
        } catch (AssertionError e) {
            printMessage(description, false);
            throw e;
        }
    }
    
    /**
     * 
     * @param description
     * @param b
     */
    protected static void printMessage(String description, boolean b) {
        System.out.println("[" + (b ? "SUCCESS" : "FAILURE") + "] " + description);
    }

}
