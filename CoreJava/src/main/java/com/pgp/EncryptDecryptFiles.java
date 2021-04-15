package com.pgp;

import org.bouncycastle.openpgp.PGPException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.security.NoSuchProviderException;

public class EncryptDecryptFiles {

    public static void main(String args[]) throws IOException,PGPException,NoSuchProviderException,Exception{

        InputStream inputStream = new ByteArrayInputStream(SimplePgpUtil.PUBLICKEY.getBytes(Charset.forName("UTF-8")));
        OutputStream outputStream = new FileOutputStream("/backup/workspace/CoreJava/src/main/resources/afterencrypt/TILLS2");
        SimplePgpUtil.encryptFile(outputStream,"/backup/workspace/CoreJava/src/main/resources/TILLS",SimplePgpUtil.readPublicKey(inputStream),false,false);

        InputStream keyStream = new ByteArrayInputStream(SimplePgpUtil.PRIVATEKEY.getBytes(Charset.forName("UTF-8")));
        File file = new File("/backup/workspace/CoreJava/src/main/resources/afterencrypt/TILLS2");
        InputStream is = new FileInputStream(file);
        OutputStream os = new FileOutputStream("/backup/workspace/CoreJava/src/main/resources/afterdecrypt/TILLS2.txt");
        SimplePgpUtil.decryptFile(is,os,keyStream,"Tillster".toCharArray());
    }
}
