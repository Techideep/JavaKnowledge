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
            "xo0EXhH3aAEEALdOPSoSRYhu8UJJ4hqwCyzAMzlznY/RKVVKwTqWGvFofKYkUqrG\n" +
            "OEpKk9KaAwDqCLy6YqkKBX6+zBvlmoDjOXQJ0oHbklh8L976DnYTSv729Qj6AgYF\n" +
            "SEfuPpqrs+IojrrxjqVystTjN2gkIHi44SdHlVryQI2uO56sL3fq63JPABEBAAHN\n" +
            "HERlZXB0aSA8ZGFyb3JhQHRpbGxzdGVyLmNvbT7CrQQTAQoAFwUCXhH3aAIbLwML\n" +
            "CQcDFQoIAh4BAheAAAoJEMl4oFCFO62HXr0EAIK0usLvCSbOhhFpLbie6dl/1oqf\n" +
            "rLGPsQGNCLnhnPWcSGruxPJDmXFjemrdIMUs1bInV26cQqjkI7MiZg2RnWOxvqiw\n" +
            "zQmV3jFxxucc6AlrK4mspp+Y9XtECoXjDbVZO6KL57vttkUuk9P4vobhs9sAsX55\n" +
            "W/yewMqsX2QsOQrqzo0EXhH3aAEEALds1gq2O65wg0urRjnUEW1Q3kQhXQGWsryq\n" +
            "1me5NdOqRlGoS2APb+tuOoQHGv9Y6pV7mCnDsveB87CvOtffypSDkxtrdtjUOFpz\n" +
            "KApr9t8pbrVaUbpxU5KeTC4XFuYX2s100rjvx5xqxMMBgy6YxAFWwJ0wJFcKN6OD\n" +
            "2J7MwNuDABEBAAHCwIMEGAEKAA8FAl4R92gFCQ8JnAACGy4AqAkQyXigUIU7rYed\n" +
            "IAQZAQoABgUCXhH3aAAKCRADD51CzMbRb0qZA/0cyiLsDT90jklVQDXfnfGFT+MG\n" +
            "OzrJ7nnBSQ3VzwI7mvI5YW5P62X/tOWnZIQd8yI/Oaw/0J/0dSLlXKX0E8sqJloH\n" +
            "n1sDcmn3SB9GbGi/GNKLv2WkZ17bpakVjrzMmFZxHw6GHJg3zGLZOBYK7sQd40V6\n" +
            "N+YcXGXQEOQI7A8uzyR0A/9jCBRJCQ36xlpPljQCTB1ZmqFZX1W7ClnprEFbrITS\n" +
            "Gsw6IUshDmeC6hEzlrofjx+aSxVKFzW24Yr/mrt4USOTMC0ICmwp7ts1CGFNlUaU\n" +
            "uoZ4wMbBMSFz2/Yg/8+d5rLvghIfkqUX9/PEzYNFLbh4sUrOztK9Lxn+jTuXSc66\n" +
            "ZM6NBF4R92gBBADiHKKbGr4jiSqIBfTSEeSLtAjCsauL683no2tYE0qMAPg75UGv\n" +
            "vRBW8+PeRK+p4sFWoMOaSsXA9wuFB65LUW6Ei+j3X2DLF6fFRtpVSrgVt/fmb8FX\n" +
            "9bhnZkjwZM3hhCvsmDl+zWpqmpewKopD3X1B3ISAvbAymA+3abU+5PGNBwARAQAB\n" +
            "wsCDBBgBCgAPBQJeEfdoBQkPCZwAAhsuAKgJEMl4oFCFO62HnSAEGQEKAAYFAl4R\n" +
            "92gACgkQgYrHuyMtFuVHiAP8DOo/o/MgqVgzSgSx/I8DZ/D8JfU6U8qmxC1PaQwI\n" +
            "ePvQKNYLlJKoZAm4cKu//C07SS2VI75SJTQH5M73aAvjrVweaLsqeMPsY/LPIb33\n" +
            "AxSPbo0ZG5ORRZIm8bF15DZ27IuZn5/uSquXEKQi7QDEPQ5h8Z9A8D3WccoWCDBF\n" +
            "xSOhhgP/e8mPVCAOuQxpOmNE7Ndhy8urCUEUNLYfl5Qb7G4Zm6LLif3hg10SjqwV\n" +
            "gvrmn6pDE5iEkG4SgNyzjj6/2Il/VvxNPEqlDQyeMz29EBRfHASZub9Cwtm22e9M\n" +
            "dyRcehRnUpaFfVLe5LZD5WzWb3aZ8pUDa7d+r5Jpeg2Apg1mxBk=\n" +
            "=LMvG";

    public static String PRIVATEKEY =
            "xcFGBF4R92gBBAC3Tj0qEkWIbvFCSeIasAsswDM5c52P0SlVSsE6lhrxaHymJFKq\n" +
                    "xjhKSpPSmgMA6gi8umKpCgV+vswb5ZqA4zl0CdKB25JYfC/e+g52E0r+9vUI+gIG\n" +
                    "BUhH7j6aq7PiKI668Y6lcrLU4zdoJCB4uOEnR5Va8kCNrjuerC936utyTwARAQAB\n" +
                    "/gkDCA0sgFBBcui+YNiTm/PAba6PDsLQU9ZQQOxVx680/K2J6vc1GRewx4zxiSbe\n" +
                    "5xxabB5tNxeToLWARELOSDPIiiCpgkopSVJqSY9uw2MIsg6Q4qHfborEPPRFgKsE\n" +
                    "s7VN96RBQbxMSkJWBMb+s12HSU+LJb7BlauE2KpjTawt7ZtV7tHA1Gdg1g1tYk9H\n" +
                    "c+DtYMspLXMWEUN5b6xBkGOMSh/8BZyF2qrqbodrsjfu/DulEi5y2rs3xfcqjGVO\n" +
                    "DSPhgaS7XeykmVkIlgGX1/pmgGbHXNhIeqsElNOyvFxeVG5b4gW1eFn9JLgEB9Ky\n" +
                    "2tK+BNMIPvgvYMNRWnm2k67MuEQC4WgBXjWZdzsL0ZaZGYzWxR9xfWEVi2aXquSg\n" +
                    "QD35D8dw29D9YSPXSuklkeGrFV8a3Tk271MkbstRYsRlYtURbYVHdWPpsY4nna7P\n" +
                    "gOdBXcywewQ6QtaXrcrR0FRjufEdMjFZwBhrDf6unjlN0pkT9xyZmsDNHERlZXB0\n" +
                    "aSA8ZGFyb3JhQHRpbGxzdGVyLmNvbT7CrQQTAQoAFwUCXhH3aAIbLwMLCQcDFQoI\n" +
                    "Ah4BAheAAAoJEMl4oFCFO62HXr0EAIK0usLvCSbOhhFpLbie6dl/1oqfrLGPsQGN\n" +
                    "CLnhnPWcSGruxPJDmXFjemrdIMUs1bInV26cQqjkI7MiZg2RnWOxvqiwzQmV3jFx\n" +
                    "xucc6AlrK4mspp+Y9XtECoXjDbVZO6KL57vttkUuk9P4vobhs9sAsX55W/yewMqs\n" +
                    "X2QsOQrqx8FFBF4R92gBBAC3bNYKtjuucINLq0Y51BFtUN5EIV0BlrK8qtZnuTXT\n" +
                    "qkZRqEtgD2/rbjqEBxr/WOqVe5gpw7L3gfOwrzrX38qUg5Mba3bY1DhacygKa/bf\n" +
                    "KW61WlG6cVOSnkwuFxbmF9rNdNK478ecasTDAYMumMQBVsCdMCRXCjejg9iezMDb\n" +
                    "gwARAQAB/gkDCCpjpdoNQ9MuYHrg1GAjoQasgJC0BUGNILWoM8cKg2K++1imF7/v\n" +
                    "51eZzk8oKKviFLtnX3kie2WG74B/rSae6AQun2pvUUxjp5WKXF8i56TFMrGV1K/n\n" +
                    "cPiAur//u6YAkQQXmZ93WDmFQp3Ds5O4/j3T42itqQd2WKUvadfav+cQfw9M6Cyo\n" +
                    "GeTYv0hj049d4fgIMd0SPhGOF4gGqQ8h8uYUrDhHFtARSvUMZSUg5lfZtac1qWGS\n" +
                    "tho7evnynHVr7fNKhqrRkwXH2TqS1xqT2N4a05iZKiPujI9C5a45grKnYqwqca1N\n" +
                    "SWdKlBa12cNFd/FP6vIoYWbfy/i4PXgdmukW4LYUTCLArBEpOe1Q9HRk6jCmbuyV\n" +
                    "iVE/DKCLYFskkgfVqzj5Cx7bGwjM6HrdocCOMIkQTV6zDiKVdbqO58gKFjhYtNst\n" +
                    "GoBw/ZesYqEvqEwXMwabwCCcYopE5WhnHJLuJtDsXX1WWZmdKQmLYH5E3RoFPcLA\n" +
                    "gwQYAQoADwUCXhH3aAUJDwmcAAIbLgCoCRDJeKBQhTuth50gBBkBCgAGBQJeEfdo\n" +
                    "AAoJEAMPnULMxtFvSpkD/RzKIuwNP3SOSVVANd+d8YVP4wY7OsnuecFJDdXPAjua\n" +
                    "8jlhbk/rZf+05adkhB3zIj85rD/Qn/R1IuVcpfQTyyomWgefWwNyafdIH0ZsaL8Y\n" +
                    "0ou/ZaRnXtulqRWOvMyYVnEfDoYcmDfMYtk4FgruxB3jRXo35hxcZdAQ5AjsDy7P\n" +
                    "JHQD/2MIFEkJDfrGWk+WNAJMHVmaoVlfVbsKWemsQVushNIazDohSyEOZ4LqETOW\n" +
                    "uh+PH5pLFUoXNbbhiv+au3hRI5MwLQgKbCnu2zUIYU2VRpS6hnjAxsExIXPb9iD/\n" +
                    "z53msu+CEh+SpRf388TNg0UtuHixSs7O0r0vGf6NO5dJzrpkx8FGBF4R92gBBADi\n" +
                    "HKKbGr4jiSqIBfTSEeSLtAjCsauL683no2tYE0qMAPg75UGvvRBW8+PeRK+p4sFW\n" +
                    "oMOaSsXA9wuFB65LUW6Ei+j3X2DLF6fFRtpVSrgVt/fmb8FX9bhnZkjwZM3hhCvs\n" +
                    "mDl+zWpqmpewKopD3X1B3ISAvbAymA+3abU+5PGNBwARAQAB/gkDCPC+GpxqfDCE\n" +
                    "YNeYrLMHlVcG/5S0hV1dj77I8e8doqHhAd0Gb77v3rEJ5u2648reLsg+7F7HHecS\n" +
                    "+aWnzVJ4kNpCq9jELWL9wJ6FAUito0LigV7Wy31QA3b03v2abUAeOXaNF7Am7tmp\n" +
                    "LuM2wVCtTLdt88nGhV+XCgBcDLYys3aXiPsqc3CNUprcyVOcm2vPhL8afW0WnA4R\n" +
                    "DrgU9G9UNqE7ewkXrIOU0bDhOBd8y9S5+u8XhG7ISF0P2oXWQFIIII1v89wqhP75\n" +
                    "M586S26hUDNz6hpmAFa/L1ryMW2ep+WcNNVxjEyyUa5ZkOlfV7YWZKjcwFZwDdk+\n" +
                    "E5z0JVf3uc5ms5AoNd0Xtk8fEkiS2Rmfxy+8InMUXTNifqTJQssY8wu0XimZO4iZ\n" +
                    "tArSfuTnnMQWqnZJ10eTN+KHbDbFcLCDDjEAgtWH3X7U3Mey0lVq1+QhdaHfto6s\n" +
                    "P0kQVh6AyuQBhWcRf87pTytvYW9c8nZ0EcSlT9bCwIMEGAEKAA8FAl4R92gFCQ8J\n" +
                    "nAACGy4AqAkQyXigUIU7rYedIAQZAQoABgUCXhH3aAAKCRCBise7Iy0W5UeIA/wM\n" +
                    "6j+j8yCpWDNKBLH8jwNn8Pwl9TpTyqbELU9pDAh4+9Ao1guUkqhkCbhwq7/8LTtJ\n" +
                    "LZUjvlIlNAfkzvdoC+OtXB5ouyp4w+xj8s8hvfcDFI9ujRkbk5FFkibxsXXkNnbs\n" +
                    "i5mfn+5Kq5cQpCLtAMQ9DmHxn0DwPdZxyhYIMEXFI6GGA/97yY9UIA65DGk6Y0Ts\n" +
                    "12HLy6sJQRQ0th+XlBvsbhmbosuJ/eGDXRKOrBWC+uafqkMTmISQbhKA3LOOPr/Y\n" +
                    "iX9W/E08SqUNDJ4zPb0QFF8cBJm5v0LC2bbZ70x3JFx6FGdSloV9Ut7ktkPlbNZv\n" +
                    "dpnylQNrt36vkml6DYCmDWbEGQ==\n" +
                    "=OEJC";

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


