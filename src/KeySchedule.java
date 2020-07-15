public class KeySchedule {

    public static String scheduleForEncryption(String key) {
        String c = key.substring(0, 28);
        String d = key.substring(28, 56);

        c = leftShift(c);
        d = leftShift(d);

        return c + d;
    }

    public static String scheduleForDecryption(String key) {
        String c = key.substring(0, 28);
        String d = key.substring(28, 56);

        c = rightShift(c);
        d = rightShift(d);

        return c + d;
    }

    private static String leftShift(String bits) {
        return bits.substring(1, 28) + bits.charAt(0);
    }

    private static String rightShift(String bits) {
        return bits.charAt(27) + bits.substring(0, 27);
    }

}
