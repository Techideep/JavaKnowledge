package com.pgp;

import java.io.ByteArrayOutputStream;
        import java.io.File;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.OutputStream;
        import java.security.NoSuchProviderException;
        import java.security.SecureRandom;
        import java.security.Security;
        import java.util.Iterator;

        import org.bouncycastle.bcpg.ArmoredOutputStream;
        import org.bouncycastle.bcpg.PublicKeyAlgorithmTags;
        import org.bouncycastle.bcpg.sig.KeyFlags;
        import org.bouncycastle.jce.provider.BouncyCastleProvider;
        import org.bouncycastle.openpgp.PGPCompressedData;
        import org.bouncycastle.openpgp.PGPCompressedDataGenerator;
        import org.bouncycastle.openpgp.PGPEncryptedData;
        import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
        import org.bouncycastle.openpgp.PGPEncryptedDataList;
        import org.bouncycastle.openpgp.PGPException;
        import org.bouncycastle.openpgp.PGPLiteralData;
        import org.bouncycastle.openpgp.PGPLiteralDataGenerator;
        import org.bouncycastle.openpgp.PGPObjectFactory;
        import org.bouncycastle.openpgp.PGPOnePassSignatureList;
        import org.bouncycastle.openpgp.PGPPrivateKey;
        import org.bouncycastle.openpgp.PGPPublicKey;
        import org.bouncycastle.openpgp.PGPPublicKeyEncryptedData;
        import org.bouncycastle.openpgp.PGPPublicKeyRing;
        import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
        import org.bouncycastle.openpgp.PGPSecretKey;
        import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
        import org.bouncycastle.openpgp.PGPSignature;
        import org.bouncycastle.openpgp.PGPSignatureSubpacketVector;
      //  import org.bouncycastle.openpgp.PGPUtil;
        import org.bouncycastle.openpgp.operator.PBESecretKeyDecryptor;
        import org.bouncycastle.openpgp.operator.bc.BcKeyFingerprintCalculator;
        import org.bouncycastle.openpgp.operator.bc.BcPBESecretKeyDecryptorBuilder;
        import org.bouncycastle.openpgp.operator.bc.BcPGPDataEncryptorBuilder;
        import org.bouncycastle.openpgp.operator.bc.BcPGPDigestCalculatorProvider;
        import org.bouncycastle.openpgp.operator.bc.BcPublicKeyDataDecryptorFactory;
        import org.bouncycastle.openpgp.operator.bc.BcPublicKeyKeyEncryptionMethodGenerator;
         import static org.bouncycastle.openpgp.PGPUtil.getDecoderStream;
import static org.bouncycastle.openpgp.PGPUtil.writeFileToLiteralData;

public class SimplePgpUtil {

    public static String PUBLICKEY=
            "-----BEGIN PGP PUBLIC KEY BLOCK-----\n" +
                    "Version: Keybase OpenPGP v1.0.0\n" +
                    "Comment: https://keybase.io/crypto\n" +
                    "\n" +
                    "xo0EXh3EYQEEALlEjOPLOTS8mimRpRlw6U1X1wPtSn7E3CAOg02fDQ688n3nBRO7\n" +
                    "N2vnoWbCSSi1iVK4t9iMybl/GpW0wqOzESqmcJCvf0n+jwK63SPpyL3cUTIMNuk8\n" +
                    "AEFkJyV8ojRMvGX7XgSyt+6+Bi/v6ym2kQEOyjqT35ywKJ8EGezn0dNnABEBAAHN\n" +
                    "IURlZXB0aSA8YXJvcmEuZGVlcHRpMDVAZ21haWwuY29tPsKtBBMBCgAXBQJeHcRh\n" +
                    "AhsvAwsJBwMVCggCHgECF4AACgkQrP1V03XVBw4eaAP/cGHenC/BiOUszQCPeB5V\n" +
                    "uXgMvQZhUOb0mSeGFg9IujnowFmqApLgWpkcBjZrERS+rN31uEg49NNZxR5q0mtl\n" +
                    "TSlJbVCU+kIADcrV4MYD61UaOwi3l89klgW9cwGsKMzl4MsXks2MEiizb5pSVY4A\n" +
                    "onZKOS6nB/dYFLgEbt8VpbLOjQReHcRhAQQAy65g5jBnvY0K9sUUeNJXfB6Fyd5C\n" +
                    "dmNOEJRkTXW1hY5qRwusi2dVTp+TmjA1IMth3Meoi5vY7T12wucsjGc0z44FcdIj\n" +
                    "Afmef3vMYROx7o89q7ad3G8Geu/akibMxDmigBz3HH2Nn6rz9hr6IflsAp8gJdca\n" +
                    "Xc3Y48M7v54somcAEQEAAcLAgwQYAQoADwUCXh3EYQUJDwmcAAIbLgCoCRCs/VXT\n" +
                    "ddUHDp0gBBkBCgAGBQJeHcRhAAoJEP9ylis23Xydpm8EAJRJwOVnByH9euyoecSG\n" +
                    "Tbzfmv89mp/HMAcQPoK1ipxACPtr8sm6t2OwvR0QRBdVOcPv/VeLP9/rRSG9Cv31\n" +
                    "vlGE+0OctqA0UJFoXN/UZRNw2bfWCPSMwcRes5vyej4l0a4z4+5Z8auOOXm9DAvX\n" +
                    "MSQnNA4bWpcqaDwjHVxx7ZIW608D/3qH48QyWS1buQD/XN/KeLV5r2AUs6OMDR9R\n" +
                    "7sPwhiy5MWBCJLKKEintrXMA2tf8jg/njgxgSH6joPsAqOc/WDd5otlcBd4p7wUo\n" +
                    "shKdAjox0ayuZUEFCY/fCs4MY15XsoPYbGhVYKbvfdLkMKIZ8oabIFkYq/hQ6zK3\n" +
                    "flZBRIF7zo0EXh3EYQEEALXwvEFMbLKGNZlSiuFr1oi92aQb/FfUMIvhR2ig34wZ\n" +
                    "W9u60CFS7CLw98RdvaARbICTWlxdKmdM8oKUhl5D3L1yiKs/MdoPl50Y3XkS3wr8\n" +
                    "BOTyP8v5Lh7qYLZ7LaNcwCYmxihshjZXfch5HHTCFLxy/ENdIJYua9eNyoDY1+u1\n" +
                    "ABEBAAHCwIMEGAEKAA8FAl4dxGEFCQ8JnAACGy4AqAkQrP1V03XVBw6dIAQZAQoA\n" +
                    "BgUCXh3EYQAKCRC7wFUAr0V/t2QnA/9Uzr7kHdSPFDQb/t43iFnZ18IHcnGWLF0Z\n" +
                    "cSiJlFiAb9YNOvbmuvNHMvvvhwdZ267bQ3P/wXqZu7PCwJCURB9k3gbjuGnYZjVS\n" +
                    "EjYCkSgBY9Q2Yh/S93OYyRStJ/HZQRnIQkjJximbzG0WHUWqzfVeg7oqg29lwPLg\n" +
                    "DNQTU1GUbEcyA/4pmmipzUobrY+nYlb3gcw0Jx5fHjeP+J4nYT5drRIq15Qf7ZZr\n" +
                    "BFoou6CBHaovpM+XnlTeDeZRjp+4J2it6Dmtp8/rqHpzg3F6lTYQuvrVMdBGdIt8\n" +
                    "+N48E7E81f7PrVXEDvudE59KlXNJXxkyecpYH9bTMW0PVmH1d4bJhUcsfg==\n" +
                    "=2sEj\n" +
                    "-----END PGP PUBLIC KEY BLOCK-----";

    public static String PRIVATEKEY ="-----BEGIN PGP PRIVATE KEY BLOCK-----\n" +
            "Version: Keybase OpenPGP v1.0.0\n" +
            "Comment: https://keybase.io/crypto\n" +
            "\n" +
            "xcFGBF4dxGEBBAC5RIzjyzk0vJopkaUZcOlNV9cD7Up+xNwgDoNNnw0OvPJ95wUT\n" +
            "uzdr56FmwkkotYlSuLfYjMm5fxqVtMKjsxEqpnCQr39J/o8Cut0j6ci93FEyDDbp\n" +
            "PABBZCclfKI0TLxl+14EsrfuvgYv7+sptpEBDso6k9+csCifBBns59HTZwARAQAB\n" +
            "/gkDCGl57TDS5+OCYGTsVKqm9FV2KlHfRCkqZIVHg5wkO9K0moQdTBvM5rj6I9uf\n" +
            "YpJrqgKxSxsOeV9071wsT1HX+hUp8ZMSmSqY+mLK1OBn9KOLcBxVRCrXckfCyAJ3\n" +
            "vBKfXNKRQ1WMZF5+KmJhYZbSclpJMuKXTa7r0/cDQ79ScvUVl+mBHo+GmfFoA9VZ\n" +
            "XAcGN6HX3qfFZiDs5RTjgP/Mrvo8TH+nZhvDW6MKBkQi8/QLowqYgJ2B2Ov6ajnI\n" +
            "U50WxoDWg170cmYmtGIS1cRMwPFaNpOqqn7HUZCgiUDes5WA4ztW/E+UvdAju/ap\n" +
            "G5BweDibGJYRNNXQ8GKMH18YUB8337cdA992/FWxcQI+3yxqAcp8JAmoth+ePLj2\n" +
            "7FPcmQEZIGVjUuAzoz4ziGhsnFsRVTiYK2lrJhRVTqtynVhJ+cAshGOe1VeRAlO8\n" +
            "AnioKkq7qopB/jkJAYb8zI38+HI+bem02t6rcvZvPx8Za/ggoCBfv7fNIURlZXB0\n" +
            "aSA8YXJvcmEuZGVlcHRpMDVAZ21haWwuY29tPsKtBBMBCgAXBQJeHcRhAhsvAwsJ\n" +
            "BwMVCggCHgECF4AACgkQrP1V03XVBw4eaAP/cGHenC/BiOUszQCPeB5VuXgMvQZh\n" +
            "UOb0mSeGFg9IujnowFmqApLgWpkcBjZrERS+rN31uEg49NNZxR5q0mtlTSlJbVCU\n" +
            "+kIADcrV4MYD61UaOwi3l89klgW9cwGsKMzl4MsXks2MEiizb5pSVY4AonZKOS6n\n" +
            "B/dYFLgEbt8VpbLHwUYEXh3EYQEEAMuuYOYwZ72NCvbFFHjSV3wehcneQnZjThCU\n" +
            "ZE11tYWOakcLrItnVU6fk5owNSDLYdzHqIub2O09dsLnLIxnNM+OBXHSIwH5nn97\n" +
            "zGETse6PPau2ndxvBnrv2pImzMQ5ooAc9xx9jZ+q8/Ya+iH5bAKfICXXGl3N2OPD\n" +
            "O7+eLKJnABEBAAH+CQMISQf6MBJrNdtgkV6RtJZRIL9NJW4pFkw5uP7wMlpDJ9/0\n" +
            "wGUlO0l6AgXaJejNxFA1B02U57Y5Gx/Eq9ikorBj1teIZm/r8gTG849sPkOHs1yS\n" +
            "SJtM67QRmzq+pT3XhC3HBwzXGTNciRun4u1CDicUMSlMczMAT0KCDCuxcMPJ9kRH\n" +
            "V9E/cDdzg+6Z3UvGCIZh3tFaEDG9Oj5QwnPOl327O7AexcxIhI5pZ9F34k9eWa3P\n" +
            "0gCQ+DotApFjWCKCk1Ynsl1UJH3BxByNyb6RxqDpRaUpHfx1Y+X+jxwRscxwNd1Y\n" +
            "UZJn/Ge1d8LpVkAw+LuKMEimUSNWBuLkbfP00GvTwnWBzjM/IWDRc/9Dy25o80hs\n" +
            "swki6Xt4YnUcnjsFWlQWYwdybUqOV/MzUcwEIJdoXh2mppjoBVuW2eRJzagZYYwu\n" +
            "aX0+Ah+RYCRF0ud9zuNLxgb30du5hHOTfIVdsa3/HohiEs/8PsTyr1CsWPG4KExQ\n" +
            "QyQQAMLAgwQYAQoADwUCXh3EYQUJDwmcAAIbLgCoCRCs/VXTddUHDp0gBBkBCgAG\n" +
            "BQJeHcRhAAoJEP9ylis23Xydpm8EAJRJwOVnByH9euyoecSGTbzfmv89mp/HMAcQ\n" +
            "PoK1ipxACPtr8sm6t2OwvR0QRBdVOcPv/VeLP9/rRSG9Cv31vlGE+0OctqA0UJFo\n" +
            "XN/UZRNw2bfWCPSMwcRes5vyej4l0a4z4+5Z8auOOXm9DAvXMSQnNA4bWpcqaDwj\n" +
            "HVxx7ZIW608D/3qH48QyWS1buQD/XN/KeLV5r2AUs6OMDR9R7sPwhiy5MWBCJLKK\n" +
            "EintrXMA2tf8jg/njgxgSH6joPsAqOc/WDd5otlcBd4p7wUoshKdAjox0ayuZUEF\n" +
            "CY/fCs4MY15XsoPYbGhVYKbvfdLkMKIZ8oabIFkYq/hQ6zK3flZBRIF7x8FGBF4d\n" +
            "xGEBBAC18LxBTGyyhjWZUorha9aIvdmkG/xX1DCL4UdooN+MGVvbutAhUuwi8PfE\n" +
            "Xb2gEWyAk1pcXSpnTPKClIZeQ9y9coirPzHaD5edGN15Et8K/ATk8j/L+S4e6mC2\n" +
            "ey2jXMAmJsYobIY2V33IeRx0whS8cvxDXSCWLmvXjcqA2NfrtQARAQAB/gkDCI8a\n" +
            "P5wdUC8TYHA/zlTyUc5iw0drYuF1qljkIcmgxrnPOvFcdEKG8dmE/aiOrN69kDs8\n" +
            "2jxTMBaIVSfza29f8hXUT/A9cCBywvIifDiKFy1+Cn3jvFsPeYDQJ92/gwV3zlwx\n" +
            "UX8pX1f0NcBEaBrx2Craq/NvKxmJ2xpcg+XKvfhEQi4Q2wqItjkjRkDC/3TEo9gm\n" +
            "WgB63qIWHFD41K+6MaM2GyGheQgkR1ehF81GDH2YfRevMjbVzrPHiX/EIdyqZ064\n" +
            "9suzV7W7jBld7RRkaQEUo32oWDIMOP6KnCMFg+IS/6QT35LLnSBYna1P7Gatvhy3\n" +
            "4rsCuxsNuWmrGrFaD3yaHOeGzy/AzCWMoD+T830637LOfOYhNRboqLmK4lW+f/zV\n" +
            "3b8+3J/H1ElDqglqlF6WpkNMrmp4xcIZDL361jnNL24qTjB7w46Uka5w8ADJSsWW\n" +
            "oPkfF4BIn72qazNibkaoWCu2NKPkFCB8jWVcbntJdj5hui3CwIMEGAEKAA8FAl4d\n" +
            "xGEFCQ8JnAACGy4AqAkQrP1V03XVBw6dIAQZAQoABgUCXh3EYQAKCRC7wFUAr0V/\n" +
            "t2QnA/9Uzr7kHdSPFDQb/t43iFnZ18IHcnGWLF0ZcSiJlFiAb9YNOvbmuvNHMvvv\n" +
            "hwdZ267bQ3P/wXqZu7PCwJCURB9k3gbjuGnYZjVSEjYCkSgBY9Q2Yh/S93OYyRSt\n" +
            "J/HZQRnIQkjJximbzG0WHUWqzfVeg7oqg29lwPLgDNQTU1GUbEcyA/4pmmipzUob\n" +
            "rY+nYlb3gcw0Jx5fHjeP+J4nYT5drRIq15Qf7ZZrBFoou6CBHaovpM+XnlTeDeZR\n" +
            "jp+4J2it6Dmtp8/rqHpzg3F6lTYQuvrVMdBGdIt8+N48E7E81f7PrVXEDvudE59K\n" +
            "lXNJXxkyecpYH9bTMW0PVmH1d4bJhUcsfg==\n" +
            "=x0x5\n" +
            "-----END PGP PRIVATE KEY BLOCK----";


    // private static final int BUFFER_SIZE = 1 << 16; // should always be power
    // of 2
    private static final int KEY_FLAGS = 27;
    private static final int[] MASTER_KEY_CERTIFICATION_TYPES = new int[] { PGPSignature.POSITIVE_CERTIFICATION,
            PGPSignature.CASUAL_CERTIFICATION, PGPSignature.NO_CERTIFICATION, PGPSignature.DEFAULT_CERTIFICATION };

    public static PGPPublicKey readPublicKey(InputStream in) throws IOException, PGPException {
        InputStream ins = getDecoderStream(in);

        PGPPublicKeyRingCollection keyRingCollection = new PGPPublicKeyRingCollection(ins,
                new BcKeyFingerprintCalculator());

        //
        // we just loop through the collection till we find a key suitable for
        // encryption, in the real
        // world you would probably want to be a bit smarter about this.
        //
        PGPPublicKey publicKey = null;

        //
        // iterate through the key rings.
        //
        Iterator<PGPPublicKeyRing> rIt = keyRingCollection.getKeyRings();

        while (publicKey == null && rIt.hasNext()) {
            PGPPublicKeyRing kRing = rIt.next();
            Iterator<PGPPublicKey> kIt = kRing.getPublicKeys();
            while (publicKey == null && kIt.hasNext()) {
                PGPPublicKey key = kIt.next();

                if (key.isEncryptionKey()) {
                    publicKey = key;
                }
            }
        }

        if (publicKey == null) {
            throw new IllegalArgumentException("Can't find public key in the key ring.");
        }
        if (!isForEncryption(publicKey)) {
            throw new IllegalArgumentException("KeyID " + publicKey.getKeyID() + " not flagged for encryption.");
        }

        return publicKey;
    }

    public static void encryptStream(OutputStream out, PGPPublicKey encKey, InputStream streamData)
            throws IOException, PGPException {
        Security.addProvider(new BouncyCastleProvider());

        out = new ArmoredOutputStream(out);

        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        PGPCompressedDataGenerator comData = new PGPCompressedDataGenerator(PGPCompressedData.ZIP);

        writeStreamToLiteralData(comData.open(bOut), PGPLiteralDataGenerator.BINARY, PGPLiteralData.CONSOLE,
                streamData);

        comData.close();

        BcPGPDataEncryptorBuilder dataEncryptor = new BcPGPDataEncryptorBuilder(PGPEncryptedData.TRIPLE_DES);
        dataEncryptor.setWithIntegrityPacket(true);
        dataEncryptor.setSecureRandom(new SecureRandom());

        PGPEncryptedDataGenerator encryptedDataGenerator = new PGPEncryptedDataGenerator(dataEncryptor);
        encryptedDataGenerator.addMethod(new BcPublicKeyKeyEncryptionMethodGenerator(encKey));

        byte[] bytes = bOut.toByteArray();
        OutputStream cOut = encryptedDataGenerator.open(out, bytes.length);
        cOut.write(bytes);
        cOut.close();
        out.close();
    }

    /**
     * decrypt the passed in message stream
     */
    @SuppressWarnings("unchecked")
    public static void decryptFile(InputStream in, OutputStream out, InputStream keyIn, char[] passwd)
            throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        in = org.bouncycastle.openpgp.PGPUtil.getDecoderStream(in);

        PGPObjectFactory pgpF = new PGPObjectFactory(in, new BcKeyFingerprintCalculator());
        PGPEncryptedDataList enc;

        Object o = pgpF.nextObject();
        //
        // the first object might be a PGP marker packet.
        //
        if (o instanceof PGPEncryptedDataList) {
            enc = (PGPEncryptedDataList) o;
        } else {
            enc = (PGPEncryptedDataList) pgpF.nextObject();
        }

        //
        // find the secret key
        //
        Iterator<PGPPublicKeyEncryptedData> it = enc.getEncryptedDataObjects();
        PGPPrivateKey sKey = null;
        PGPPublicKeyEncryptedData pbe = null;

        while (sKey == null && it.hasNext()) {
            pbe = it.next();

            sKey = findPrivateKey(keyIn, pbe.getKeyID(), passwd);
        }

        if (sKey == null) {
            throw new IllegalArgumentException("Secret key for message not found.");
        }

        InputStream clear = pbe.getDataStream(new BcPublicKeyDataDecryptorFactory(sKey));

        PGPObjectFactory plainFact = new PGPObjectFactory(clear, new BcKeyFingerprintCalculator());

        Object message = plainFact.nextObject();

        if (message instanceof PGPCompressedData) {
            PGPCompressedData cData = (PGPCompressedData) message;
            PGPObjectFactory pgpFact = new PGPObjectFactory(cData.getDataStream(), new BcKeyFingerprintCalculator());

            message = pgpFact.nextObject();
        }

        if (message instanceof PGPLiteralData) {
            PGPLiteralData ld = (PGPLiteralData) message;

            InputStream unc = ld.getInputStream();
            int ch;

            while ((ch = unc.read()) >= 0) {
                out.write(ch);
            }
        } else if (message instanceof PGPOnePassSignatureList) {
            throw new PGPException("Encrypted message contains a signed message - not literal data.");
        } else {
            throw new PGPException("Message is not a simple encrypted file - type unknown.");
        }

        if (pbe.isIntegrityProtected()) {
            if (!pbe.verify()) {
                throw new PGPException("Message failed integrity check");
            }
        }
    }

    /**
     * Load a secret key ring collection from keyIn and find the private key
     * corresponding to keyID if it exists.
     *
     * @param keyIn
     *            input stream representing a key ring collection.
     * @param keyID
     *            keyID we want.
     * @param pass
     *            passphrase to decrypt secret key with.
     * @return
     * @throws IOException
     * @throws PGPException
     * @throws NoSuchProviderException
     */
    public static PGPPrivateKey findPrivateKey(InputStream keyIn, long keyID, char[] pass)
            throws IOException, PGPException, NoSuchProviderException {
        PGPSecretKeyRingCollection pgpSec = new PGPSecretKeyRingCollection(getDecoderStream(keyIn),
                new BcKeyFingerprintCalculator());
        return findPrivateKey(pgpSec.getSecretKey(keyID), pass);

    }

    /**
     * Load a secret key and find the private key in it
     *
     * @param pgpSecKey
     *            The secret key
     * @param pass
     *            passphrase to decrypt secret key with
     * @return
     * @throws PGPException
     */
    public static PGPPrivateKey findPrivateKey(PGPSecretKey pgpSecKey, char[] pass) throws PGPException {
        if (pgpSecKey == null)
            return null;

        PBESecretKeyDecryptor decryptor = new BcPBESecretKeyDecryptorBuilder(new BcPGPDigestCalculatorProvider())
                .build(pass);
        return pgpSecKey.extractPrivateKey(decryptor);
    }

    public static void encryptFile(OutputStream out, String fileName, PGPPublicKey encKey, boolean armor,
                                   boolean withIntegrityCheck) throws IOException, NoSuchProviderException, PGPException {
        Security.addProvider(new BouncyCastleProvider());

        if (armor) {
            out = new ArmoredOutputStream(out);
        }

        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        PGPCompressedDataGenerator comData = new PGPCompressedDataGenerator(PGPCompressedData.ZIP);

        writeFileToLiteralData(comData.open(bOut), PGPLiteralData.BINARY, new File(fileName));

        comData.close();

        BcPGPDataEncryptorBuilder dataEncryptor = new BcPGPDataEncryptorBuilder(PGPEncryptedData.TRIPLE_DES);
        dataEncryptor.setWithIntegrityPacket(withIntegrityCheck);
        dataEncryptor.setSecureRandom(new SecureRandom());

        PGPEncryptedDataGenerator encryptedDataGenerator = new PGPEncryptedDataGenerator(dataEncryptor);
        encryptedDataGenerator.addMethod(new BcPublicKeyKeyEncryptionMethodGenerator(encKey));

        byte[] bytes = bOut.toByteArray();
        OutputStream cOut = encryptedDataGenerator.open(out, bytes.length);
        cOut.write(bytes);
        cOut.close();
        out.close();
    }

    private static void writeStreamToLiteralData(OutputStream os, char fileType, String name, InputStream streamData)
            throws IOException {
        int bufferLength = 2048;

        byte[] buff = new byte[bufferLength];
        PGPLiteralDataGenerator lData = new PGPLiteralDataGenerator();
        OutputStream pOut = lData.open(os, fileType, name, PGPLiteralData.NOW, buff);

        byte[] buffer = new byte[bufferLength];
        int len;
        while ((len = streamData.read(buffer)) > 0) {
            pOut.write(buffer, 0, len);
        }

        pOut.close();
    }

    @SuppressWarnings("deprecation")
    public static boolean isForEncryption(PGPPublicKey key) {
        if (key.getAlgorithm() == PublicKeyAlgorithmTags.RSA_SIGN || key.getAlgorithm() == PublicKeyAlgorithmTags.DSA
                || key.getAlgorithm() == PublicKeyAlgorithmTags.EC
                || key.getAlgorithm() == PublicKeyAlgorithmTags.ECDSA) {
            return false;
        }

        return hasKeyFlags(key, KeyFlags.ENCRYPT_COMMS | KeyFlags.ENCRYPT_STORAGE);
    }

    @SuppressWarnings("unchecked")
    private static boolean hasKeyFlags(PGPPublicKey encKey, int keyUsage) {
        if (encKey.isMasterKey()) {
            for (int i = 0; i != SimplePgpUtil.MASTER_KEY_CERTIFICATION_TYPES.length; i++) {
                for (Iterator<PGPSignature> eIt = encKey
                        .getSignaturesOfType(SimplePgpUtil.MASTER_KEY_CERTIFICATION_TYPES[i]); eIt.hasNext();) {
                    PGPSignature sig = eIt.next();
                    if (!isMatchingUsage(sig, keyUsage)) {
                        return false;
                    }
                }
            }
        } else {
            for (Iterator<PGPSignature> eIt = encKey.getSignaturesOfType(PGPSignature.SUBKEY_BINDING); eIt.hasNext();) {
                PGPSignature sig = eIt.next();
                if (!isMatchingUsage(sig, keyUsage)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isMatchingUsage(PGPSignature sig, int keyUsage) {
        if (sig.hasSubpackets()) {
            PGPSignatureSubpacketVector sv = sig.getHashedSubPackets();
            if (sv.hasSubpacket(SimplePgpUtil.KEY_FLAGS)) {
                if (sv.getKeyFlags() == 0 && keyUsage == 0) {
                    return false;
                }
            }
        }
        return true;
    }

}