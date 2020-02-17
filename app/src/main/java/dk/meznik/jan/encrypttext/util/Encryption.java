package dk.meznik.jan.encrypttext.util;

import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {
    private static final String TAG = "ENCRYPTION";

    private static final String AES_MODE = "AES/CBC/PKCS7Padding";
    private static final String ENCODING = "UTF-8";

    private static final String HASH_ALGORITHM = "SHA-256";
    private static final String SALT = "7938zQ2B1ZC7IF5duG49Jx02x29491t6921805H5ppN43IT9Ti6l3GAJH72B722M";
    private static final String SALT2 = "MfoM95KRP4F7mb4vPAyn9yBZRP16UHOhHlSrnC9IEOtJHzjwyZrTCbi3lFhiaSCB";

    private static final byte[] ivBytes = {0x42, 0x59, (byte)0xAF, 0x51, (byte)0xFF, (byte)0xB3,
            0x02, 0x68, 0x62, (byte)0xCE,(byte) 0xDA, 0x11, 0x00, (byte)0xE9, 0x44, 0x01};

    private static SecretKeySpec generateKey(final String password) throws NoSuchAlgorithmException,
                                                                    UnsupportedEncodingException {

        // Create a SHA-256 hash of the password
        final MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);

        // Salt the password and get bytes using ENCODING.
        // It should not be necessary to salt with AES, but I do it nonetheless
        byte[] bytes = (password+SALT).getBytes(ENCODING);
        digest.update(bytes, 0, bytes.length);

        // Get the HASH
        byte[] key = digest.digest();

        // Using SHA-256, this returns a 256bit key
        return new SecretKeySpec(key, "AES");
    }

    public static String encryptDefault(String message) throws GeneralSecurityException {
        return encrypt(SALT2, message);
    }

    public static String decryptDefault(String message) throws GeneralSecurityException {
        return decrypt(SALT2, message);
    }

    public static String encrypt(final String password, String message) throws GeneralSecurityException {
        try {
            final SecretKeySpec key = generateKey(password);

            Cipher cipher = Cipher.getInstance(AES_MODE);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
            byte[] cipherText = cipher.doFinal(message.getBytes(ENCODING));

            return Base64.encodeToString(cipherText, Base64.NO_WRAP);

        } catch (UnsupportedEncodingException e) {
            throw new GeneralSecurityException(e);
        }
    }

    public static String decrypt(final String password, String base64EncodedCipherText)
            throws GeneralSecurityException {

        try {
            final SecretKeySpec key = generateKey(password);

            byte[] decodedCipherText = Base64.decode(base64EncodedCipherText, Base64.NO_WRAP);

            Cipher cipher = Cipher.getInstance(AES_MODE);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);

            byte[] decryptedBytes = cipher.doFinal(decodedCipherText);
            return new String(decryptedBytes, ENCODING);

        } catch (UnsupportedEncodingException e) {
            throw new GeneralSecurityException(e);
        }
    }
}
