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
    private static String PUBLICKEY="-----BEGIN PGP PUBLIC KEY BLOCK-----\n" +
            "Version: Keybase OpenPGP v1.0.0\n" +
            "Comment: https://keybase.io/crypto\n" +
            "\n" +
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
            "=LMvG\n" +
            "-----END PGP PUBLIC KEY BLOCK-----";

    private static String PRIVATEKEY =
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

  /*  private static String PUBLICKEY="xo0EXhXFQAEEAOZ3gnw+n4m46+kp5ps0QxDj2cRXipWJY6fnY1wKBZCW0Xng9WEG\n" +
            "I1zH6geI82PXJFMKuqa8W8Ksx9wAQ9VyGTi8GnjDqe4r2gNvOggJRpaA09J3wgJi\n" +
            "6+o8U6JPfPQ7jW6o2Iju/kD9es+B3KfTco/GZlOCAkcdORo3vvgHrtuFABEBAAHN\n" +
            "HGRlZXB0aSA8ZGFyb3JhQHRpbGxzdGVyLmNvbT7CrQQTAQoAFwUCXhXFQAIbLwML\n" +
            "CQcDFQoIAh4BAheAAAoJEKFHRPPRoJklu3sD/1zRxCjQzQ7f6Yy6+pqEzA8vmfrU\n" +
            "OB9h15JcGrJme70I5oggQG77XEjf7S38TUxHrDOzVnDiiQ3ChZo0eA1FOFhYnkAy\n" +
            "Q/VADBA9785e9h0QVdqQk/lN8C09XK+INpTVP2hP9XVueXZhW3CFGB6O2sVS032U\n" +
            "eCXuwPAYx0H43KZ1zo0EXhXFQAEEANOdwTUYJ96BVJLVf/i/k0EzFHJ3QRBA3v7u\n" +
            "t0L+lq9XH/3BLNywzsb00bR6z7VCRcAoh2O5HODbBBoMf6GEQehlJZbyn8CIpPF4\n" +
            "w5+LdoT3DVG/nMRKVvfNzvSfXAUu9fWGY/s8pJ4SbXjq0W1ohUFrZCXcZLh9kZA1\n" +
            "aXVvzzlBABEBAAHCwIMEGAEKAA8FAl4VxUAFCQ8JnAACGy4AqAkQoUdE89GgmSWd\n" +
            "IAQZAQoABgUCXhXFQAAKCRApzGLVKvftndKUA/48Gr6RybzkQzf8sqTKzApqdml/\n" +
            "WjFMJIHymreo3SQr1tMXtKWoqPTKtiU9YknCzFUpsbKpFjfAW4v+Sjz+i/O13IcZ\n" +
            "FYrdoFqzwoR/6JKz/umrulJo4bhDQFClGrSY8gg+M7B2XNqZOzq71evYxb+Be7+t\n" +
            "cQ8duC+yoPd/ZDuwSbPhBACJqxCB8htXQOXraH2/5epxNpyph88tWoWCkvf046FM\n" +
            "Ys6LqpgIF7X4XSGYEAmU4FzJh5JCssaAuAcI3c08QMKdNUd0x6k2541UvJmJMV0I\n" +
            "ZdNnP4q/HOqMrIKIapFV1mJEqsTTi8d3IJb6ORmVADmIfl4/wjnX6W4i4ugbpDJL\n" +
            "UM6NBF4VxUABBACrEcFUCFVHQ7F5wdGnRLkl9WznbMwGFn9/7V3N5xSGwufvs7js\n" +
            "Ddq62SobvHLbRhtzGmylVRW9iyf2TCEHmlnMap2oe6q1muRmnZe1ciR9/h3TuFkR\n" +
            "stu9Fzwm4CEACc8ODXn7R9Kz7SEBEBm5zpj43n/4bbBX1ZyJNmZS8DzivQARAQAB\n" +
            "wsCDBBgBCgAPBQJeFcVABQkPCZwAAhsuAKgJEKFHRPPRoJklnSAEGQEKAAYFAl4V\n" +
            "xUAACgkQdD54D1d8Wy1SRQP/SIsjry8cLpkamWs3n0E9veuP2qhifkeixngmGvP1\n" +
            "F6ZI2G95i2XE5mzDgJcU0nbChae6BOIOmWuZSiwvPUCAdHVwUifY8CVLUw4cWhSf\n" +
            "dkEJ3OoPXTe8H4jO5+B72di1iF/mta6P5d8Nlv4mg9zN9lnIPK3UGk0qmIk1kcF8\n" +
            "s5KwPwQAiTsa+9QP9dkOCkA8noopWPEwFRjq0gj20LPuWVIHnUwLfNQAkF1Mx33Y\n" +
            "mfVq+qTAXe+8QG65DsoimmeGkmNomlV9r8c1DdW1mya7yZeTvE9wIAF2eG3vdZPa\n" +
            "Gbr3YKM6MC3uCiVPg9DkT0VssPJdIahGnsbvCw7UxSxfPPASjSI=\n" +
            "=ftZ4";
    private static String PRIVATEKEY="xcFGBF4VxUABBADmd4J8Pp+JuOvpKeabNEMQ49nEV4qViWOn52NcCgWQltF54PVh\n" +
            "BiNcx+oHiPNj1yRTCrqmvFvCrMfcAEPVchk4vBp4w6nuK9oDbzoICUaWgNPSd8IC\n" +
            "YuvqPFOiT3z0O41uqNiI7v5A/XrPgdyn03KPxmZTggJHHTkaN774B67bhQARAQAB\n" +
            "/gkDCLTtkZt6mP7cYLZyWPao775xZE7CH1he9vCAZAP/W79rVSUnPXEJMfRv9gvG\n" +
            "/8t1rYvaqb7oI56FemTophF2Db0VwgNbtO4D3+51aMG+iFVH2jPlV+ukUWtwfpNO\n" +
            "RyKSs/LSPFaAV/HG7W3lo9co0r1iD5frUyQp3bdR+rZ/lZt4Q+B3jmm/1SuNHnMm\n" +
            "XgOvXYiF8Pz//bPsPzTICgfEkOs8pMUL7DYen0hCP2n0H96k0SMPKdTpbXXNJYlk\n" +
            "dUlHQivu1dWWQ4Jrqq58YPnfJ9EJMFyPuaBxan2cnY8s/TEbH6F8mbR54qpU4HeO\n" +
            "BvLJKas/8Wxs5wz9Tmr/SFccxlF7PtYvv8v1Jm3JbOtetZmC8+oDXid2vQUIhqcM\n" +
            "1oojwjMsbczYJuxR5XyrT88j7obqyBCD1CIoHbFH+aTZuM0gF1DPYbP6peN0eIht\n" +
            "Z4xAWjwHWwLvfcgo3UR/b9+M2fqTMy5HiplWDFFgQyWC/LGLt2auW/rNHGRlZXB0\n" +
            "aSA8ZGFyb3JhQHRpbGxzdGVyLmNvbT7CrQQTAQoAFwUCXhXFQAIbLwMLCQcDFQoI\n" +
            "Ah4BAheAAAoJEKFHRPPRoJklu3sD/1zRxCjQzQ7f6Yy6+pqEzA8vmfrUOB9h15Jc\n" +
            "GrJme70I5oggQG77XEjf7S38TUxHrDOzVnDiiQ3ChZo0eA1FOFhYnkAyQ/VADBA9\n" +
            "785e9h0QVdqQk/lN8C09XK+INpTVP2hP9XVueXZhW3CFGB6O2sVS032UeCXuwPAY\n" +
            "x0H43KZ1x8FGBF4VxUABBADTncE1GCfegVSS1X/4v5NBMxRyd0EQQN7+7rdC/pav\n" +
            "Vx/9wSzcsM7G9NG0es+1QkXAKIdjuRzg2wQaDH+hhEHoZSWW8p/AiKTxeMOfi3aE\n" +
            "9w1Rv5zESlb3zc70n1wFLvX1hmP7PKSeEm146tFtaIVBa2Ql3GS4fZGQNWl1b885\n" +
            "QQARAQAB/gkDCHV5SUEFPiCqYHp05dnEDKX02lwJb1FP8y3pNBMb0pOKTubzrAJj\n" +
            "0reoUhYMKzXoZVImDyxaCg9bH0GYhaXoU7EnhPq/U04q2Z4UZybJeebsAhrf6Ru2\n" +
            "kzhOZtn+ujzF0+kZoCX3zp431et0/Vu0u/UxpGUsVtawV4H+JN99AyJhfcArNpaR\n" +
            "7TvGPjFwpEHTPJdlQOPOAGikxrLjbCFoS6kD9fOr3tnqMLulz2qBsGkxjmuWJkeF\n" +
            "SMkiNAj8PyXBjmS7ZixNxiIgUSK7Hhi3NSN5ypXmmycvxiZZjfsqlILOCI6bbqd/\n" +
            "50ouVg9wCKcqwQZGjuTUJDZJFt0dg6G+C+FcBeqEk2KSTYfbCOjsnUC1k8UyOJvV\n" +
            "NBUQz37mSfOaSWzOYDOdD0S0JWfsz8x9ElkNaZAj28Eyik92mihYqQsUaWj14W7m\n" +
            "Ctc4nG6sCl9GPDlXptStS4VKi1vitykkI99O1vA6Ki5lPEtElvGHS+GekNW98h3C\n" +
            "wIMEGAEKAA8FAl4VxUAFCQ8JnAACGy4AqAkQoUdE89GgmSWdIAQZAQoABgUCXhXF\n" +
            "QAAKCRApzGLVKvftndKUA/48Gr6RybzkQzf8sqTKzApqdml/WjFMJIHymreo3SQr\n" +
            "1tMXtKWoqPTKtiU9YknCzFUpsbKpFjfAW4v+Sjz+i/O13IcZFYrdoFqzwoR/6JKz\n" +
            "/umrulJo4bhDQFClGrSY8gg+M7B2XNqZOzq71evYxb+Be7+tcQ8duC+yoPd/ZDuw\n" +
            "SbPhBACJqxCB8htXQOXraH2/5epxNpyph88tWoWCkvf046FMYs6LqpgIF7X4XSGY\n" +
            "EAmU4FzJh5JCssaAuAcI3c08QMKdNUd0x6k2541UvJmJMV0IZdNnP4q/HOqMrIKI\n" +
            "apFV1mJEqsTTi8d3IJb6ORmVADmIfl4/wjnX6W4i4ugbpDJLUMfBRgReFcVAAQQA\n" +
            "qxHBVAhVR0OxecHRp0S5JfVs52zMBhZ/f+1dzecUhsLn77O47A3autkqG7xy20Yb\n" +
            "cxpspVUVvYsn9kwhB5pZzGqdqHuqtZrkZp2XtXIkff4d07hZEbLbvRc8JuAhAAnP\n" +
            "Dg15+0fSs+0hARAZuc6Y+N5/+G2wV9WciTZmUvA84r0AEQEAAf4JAwha18WL8EPQ\n" +
            "j2AFy2+ekat7WNm2ZWSK5R9tgMBSv5AB3mBXxiF6kTTGBJ2HUz4TKVVWMacYl+rZ\n" +
            "PweBB/iMvF6EkV/OBOMAoBTOGYxa2fFuxgmu+7hYuZ00p/wlvVO2QfSlEsr18XSy\n" +
            "uZy/5Gc84Q3H6qfWdjw/HN09upRXVPV5w2v7IYamIYuR+AabbHlKE0OlIGJ2tr8G\n" +
            "guYT/OtyZBEWPW08xnj9E+oj9npScEPjFudxxwUQqwaCppkdcnI8arfPGlt1meiB\n" +
            "aFwt+Cl4pu3O4DLnVHLrfH+r+tYFA+M5uo0CDKIW0Jtpfw0jM4hq+NOrjjr0XNyn\n" +
            "SwyHdWgnou2h1SshARmBHFKVFlIz9baZ0QBpKAMV9qW72SmIO3gwRaP1Sz5RIjX3\n" +
            "IUJ3qMRCUijLoFaE4YV17GLkig1Qy2XRLNkYcqUjgxuwgklPVtM2nlkZvUm6QZG0\n" +
            "mxUAXtraCqyKQHqagM29PZBK/wqb4AdEhqU3bcaDwsCDBBgBCgAPBQJeFcVABQkP\n" +
            "CZwAAhsuAKgJEKFHRPPRoJklnSAEGQEKAAYFAl4VxUAACgkQdD54D1d8Wy1SRQP/\n" +
            "SIsjry8cLpkamWs3n0E9veuP2qhifkeixngmGvP1F6ZI2G95i2XE5mzDgJcU0nbC\n" +
            "hae6BOIOmWuZSiwvPUCAdHVwUifY8CVLUw4cWhSfdkEJ3OoPXTe8H4jO5+B72di1\n" +
            "iF/mta6P5d8Nlv4mg9zN9lnIPK3UGk0qmIk1kcF8s5KwPwQAiTsa+9QP9dkOCkA8\n" +
            "noopWPEwFRjq0gj20LPuWVIHnUwLfNQAkF1Mx33YmfVq+qTAXe+8QG65DsoimmeG\n" +
            "kmNomlV9r8c1DdW1mya7yZeTvE9wIAF2eG3vdZPaGbr3YKM6MC3uCiVPg9DkT0Vs\n" +
            "sPJdIahGnsbvCw7UxSxfPPASjSI=\n" +
            "=UU46";*/
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
