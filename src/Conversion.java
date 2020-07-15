public class Conversion {

    /**
     * Converts a string into 7-bit ascii binary representation
     */
    public static String strToBits(String str){
        String result = "";
        for(int x = 0; x < str.length(); x++){
            System.out.println("converting " + str.charAt(x) + " to " + Integer.toBinaryString((int)str.charAt(x)));
            String temp = Integer.toBinaryString((int)str.charAt(x)); // converts letter to 7-bit ascii
            for(int y = temp.length(); y < 8; y++){
                temp = "0" + temp;
            }
            result += temp.substring(1);
        }
        return result;
    }

    /**
     * Converts 7-bit ascii values into characters to build a string
     */
    public static String bitsToStr(String bits){
        String result = "";
        for(int x = 0; x < bits.length() - 7; x+=7){
            result += (char) Integer.parseInt("0" + bits.substring(x, x+7), 2);
        }
        return result;
    }
}