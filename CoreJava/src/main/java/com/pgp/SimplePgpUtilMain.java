package com.pgp;

import org.bouncycastle.openpgp.PGPException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class SimplePgpUtilMain {

    public static String PUBLICKEY=
           "";

    public static String PRIVATEKEY =
            "";

    public static void main(String args[]) throws IOException, PGPException,Exception {
        String str ="Testing encryption algo using pgp";
       String data = encryptMessage(str);
        System.out.println("After Encryption"+ data);
        System.out.println(""+decryptMessage(str));

    }

    private static String encryptMessage(String data) throws IOException, PGPException {
        //the private key file

        InputStream fis = new ByteArrayInputStream(PUBLICKEY.getBytes(Charset.forName("UTF-8")));
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        //convert the data to input stream
        InputStream stream = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));

        //encrypt it!
        SimplePgpUtil.encryptStream(out, SimplePgpUtil.readPublicKey(fis), stream);

        //close all the stream
        out.close();
        stream.close();

        return new String(out.toByteArray());

    }

    private static String decryptMessage(String message) throws IOException, PGPException ,Exception{

        //trim it, just to be safe
        message = message.trim();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        //convert encrypted message to input stream
     //   ByteArrayInputStream bais = new ByteArrayInputStream(message.getBytes(StandardCharsets.UTF_8));

        InputStream bais = new FileInputStream("/backup/workspace/CoreJava/src/main/resources/afterencrypt/TILLS_20200113");
        //public key file
        InputStream fis =new ByteArrayInputStream(PRIVATEKEY.getBytes(Charset.forName("UTF-8")));

        // the real action. 'password1' is the passphrase
        SimplePgpUtil.decryptFile(bais, baos, fis, new String("Tillster").toCharArray());

        //close all the stream
        baos.close();
        bais.close();
        fis.close();

        //return the decrypted message
        return new String(baos.toByteArray());

    }
}


