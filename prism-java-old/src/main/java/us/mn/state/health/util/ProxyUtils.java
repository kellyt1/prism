package us.mn.state.health.util;

public class ProxyUtils {

    public static boolean validateClassesForEquals(Class proxiedClass, Object currentInstance, Object otherInstance) {
        boolean b = proxiedClass.isAssignableFrom(currentInstance.getClass());
        boolean b1 = proxiedClass.isAssignableFrom(otherInstance.getClass());
        if (otherInstance == null || !b || !b1) return false;
        return true;
    }
}
