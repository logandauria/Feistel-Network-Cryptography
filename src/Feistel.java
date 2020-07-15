import java.io.File;
import java.io.FileNotFoundException;
import java.util.BitSet;
import java.util.Scanner;

public class Feistel {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("required configuration: plaintext-filename key-filename");
            return;
        }

        String plaintextFileName = args[0];
        String keyFileName = args[1];
        // initialization vector: FFFF0000FFFF0000
        String iv = "1111111111111111000000000000000011111111111111110000000000000000";
        String key = "11000011100001110000111000011100001110000111000011100001";    // key 56 bits: aaaaaa


        try {

            Scanner input0 = new Scanner(new File(plaintextFileName));



            String recoveredText = "";

            String text = "";
            String t = "";
            t += input0.nextLine();
            while(input0.hasNext()) {
                t += "\n";
                t += input0.nextLine();
            }
            text = Conversion.strToBits(t);
            text = pad(text, 64);

            System.out.println(text);
            System.out.println(text.length());

            System.out.println("key=\"aaaaaaaa\"" );

            System.out.println("original binary, padded: [" + text + "]");
            System.out.println(Conversion.bitsToStr(text));
            String ciphertext = encryptCBC(text, key, iv);
            System.out.println("ciphertext: [" + ciphertext + "]");
            for (int i = 0; i < 10; i++) {
                key = KeySchedule.scheduleForEncryption(key);
            }
            String plaintext = decryptCBC(ciphertext, key, iv);
            System.out.println("decrypted binary: [" + plaintext + "]");
            System.out.println(Conversion.bitsToStr(plaintext));

        } catch (FileNotFoundException e){
            System.out.println("file not found");
            return;
        }
    }

    /**
     * Runs Cipher Block Chaining mode for encryption
     */
    public static String encryptCBC(String text, String key, String iv){
        String ciphertext = "", mblock = "", cblock = "";

        mblock = xor(text.substring(0, 64), iv);
        cblock = encrypt(mblock, key);
        ciphertext += cblock;
        for(int x = 1; x < text.length() / 64; x++) {
            mblock = xor(text.substring(x * 64, x * 64 + 64), cblock);
            cblock = encrypt(mblock, key);
            ciphertext += cblock;
        }

        return ciphertext;
    }

    /**
     * Runs encryption through the feistel network
     */
    public static String encrypt(String text, String key){
        for (int i = 0; i < 10; i++) {
            key = KeySchedule.scheduleForEncryption(key);
            text = FeistelNetwork.iterate(text, key.substring(0, 32));
        }
        return text;
    }

    /**
     * Runs Cipher Block Chaining mode for decryption
     */
    public static String decryptCBC(String ciphertext, String key, String iv){
        String result = "", mblock = "", cblock = "";

        int length = ciphertext.length();

        for(int x = 1; x < length / 64; x++) {

            cblock = ciphertext.substring(length - 64 * x, length - 64 * (x-1));
            mblock = xor(decrypt(cblock, key), ciphertext.substring(length - 64 * (x+1), length - 64 * x));
            result = mblock + result;

        }

        cblock = ciphertext.substring(0, 64);
        mblock = decrypt(cblock, key);
        mblock = xor(mblock, iv);
        result = mblock + result;

        return result;
    }

    /**
     * Runs decryption through the feistel network
     */
    public static String decrypt(String text, String key){
        for (int i = 0; i < 10; i++) {
            key = KeySchedule.scheduleForDecryption(key);
            text = FeistelNetwork.iterate(text, key.substring(0, 32));
        }
        return text;
    }

    /**
     * runs XOR operation on two binary strings
     */
    public static String xor(String t1, String t2){
        String s = "";
        if (t1.length() != t2.length()){
            System.out.println("xor: invalid lengths");
        } else {
            for(int x = 0; x < t1.length(); x++){
                if(t1.charAt(x) == t2.charAt(x))
                    s += "0";
                else
                    s += "1";
            }
        }
        return s;
    }

    /**
     * Pads a string to match a desired length
     * @param n: desired length
     */
    public static String pad(String s, int n){
        while(s.length() % 64 != 0)
            s += "0";
        return s;
    }

    /**
     * Expands a bit string for printing
     */
    private static String expand(String str) {
        String result = "";
        for (int i = 0; i < str.length(); i++)
            result += str.charAt(i) + ", ";

        result = result.substring(0, result.length() - 2);
        return result;
    }

}
