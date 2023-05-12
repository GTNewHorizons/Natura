package mods.natura.util;

public class Util {

    public static <T> T getWithFallback(T[] array, int index) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Cannot fallback to array[0]");
        }
        if (index >= 0 && index < array.length) {
            return array[index];
        }
        return array[0];
    }
}
