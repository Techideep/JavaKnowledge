package com.pgp;


import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPCompressedData;
import org.bouncycastle.openpgp.PGPCompressedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;

public class PGPExample {
    private static String PUBLICKEY="";

    private static String PRIVATEKEY =
            "";

       /* public static void encrypt() throws Exception {

            Security.addProvider(new BouncyCastleProvider());

            //Load Public Key File
            FileInputStream key = new FileInputStream("/Users/deeptiarora/Tillster/frd188/documents_20191218/Stored Value Solutions UAT File Encryption Key.asc");
         //   PGPPublicKey pubKey = KeyBasedFileProcessorUtil.readPublicKey(key);

            //Output file
            FileOutputStream out = new FileOutputStream("target/enc.bpg");

            //Input file
            String inputFilename = "src/main/resources/plaintext.txt";

            //Other settings
            boolean armor = false;
            boolean integrityCheck = false;
            KeyBasedFileProcessorUtil.encryptFile(out, inputFilename,
                    pubKey, armor, integrityCheck);
        }
*/
       public static void main(String args[]) throws IOException,PGPException,NoSuchProviderException,Exception{
     /*      InputStream inputStream = new ByteArrayInputStream(PUBLICKEY.getBytes(Charset.forName("UTF-8")));
           OutputStream outputStream = new FileOutputStream("/backup/workspace/CoreJava/src/main/resources/afterencrypt/TILLS");
           PGPUtil.encryptFile(outputStream,"/backup/workspace/CoreJava/src/main/resources/TILLS",PGPUtil.readPublicKey(inputStream),false,false);
*/
           //Decrypt File
           InputStream keyStream = new ByteArrayInputStream(PRIVATEKEY.getBytes(Charset.forName("UTF-8")));
           File file = new File("/backup/workspace/CoreJava/src/main/resources/afterencrypt/TILLS_20200113");
           InputStream is = new FileInputStream(file);
           OutputStream os = new FileOutputStream("/backup/workspace/CoreJava/src/main/resources/afterdecrypt/TILLS_20200113.txt");
           PGPUtilClass.decryptFile(is,os,keyStream,"Tillster".toCharArray());
       }

    public static void encryptFile(OutputStream out, String fileName,
                                   PGPPublicKey encKey, boolean armor, boolean withIntegrityCheck)
            throws IOException, NoSuchProviderException, PGPException
    {
        Security.addProvider(new BouncyCastleProvider());

        if (armor) {
            out = new ArmoredOutputStream(out);
        }

        ByteArrayOutputStream bOut = new ByteArrayOutputStream();

        PGPCompressedDataGenerator comData = new PGPCompressedDataGenerator(
                PGPCompressedData.ZIP);

        org.bouncycastle.openpgp.PGPUtil.writeFileToLiteralData(comData.open(bOut),
                PGPLiteralData.BINARY, new File(fileName));

        comData.close();

     /*   PGPEncryptedDataGenerator cPk = new PGPEncryptedDataGenerator(
                PGPEncryptedData.CAST5, withIntegrityCheck,
                new SecureRandom(), "BC");

        cPk.addMethod(encKey);*/

        byte[] bytes = bOut.toByteArray();

     /*  OutputStream cOut = cPk.open(out, bytes.length);

        cOut.write(bytes);

        cOut.close();
*/
        out.close();
    }

}
