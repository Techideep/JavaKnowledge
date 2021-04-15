package com.sftp;

import com.jcraft.jsch.*;
import java.io.*;
import java.util.Enumeration;

public class sftp  {

    private static String remoteHost = "testsftp.storedvalue.com";
    private static String username = "TILLS-UAT";
    private static String password = "?qddyiao1znJwc";

    public static void main(String[] args) {
           try {
               //setupJsch();
               whenUploadFileUsingJsch_thenSuccess();
               whenDownloadFileUsingJsch_thenSuccess();
           } catch (Exception e) {
               e.printStackTrace();
           }
    }

    private static ChannelSftp setupJsch() throws JSchException {
        JSch jsch = new JSch();
        jsch.setKnownHosts("/Users/deeptiarora/.ssh/known_hosts");
        Session jschSession = jsch.getSession(username, remoteHost,22);
        jschSession.setPassword(password);
        jschSession.connect();
        return (ChannelSftp) jschSession.openChannel("sftp");
    }

    public static void whenUploadFileUsingJsch_thenSuccess() throws JSchException, SftpException {
        ChannelSftp channelSftp = setupJsch();
        channelSftp.connect();

        String localFile = "/backup/workspace/CoreJava/src/main/resources/TestReconfile.txt";
        String remoteDir = "/";

        channelSftp.put(localFile, remoteDir + "TestReconfile.txt");

        channelSftp.exit();
    }

    public static void whenDownloadFileUsingJsch_thenSuccess() throws JSchException, SftpException {
        ChannelSftp channelSftp = setupJsch();
        channelSftp.connect();

        String remoteFile = "/TestReconfile.txt";
        String localDir = "/backup/workspace/CoreJava/src/main/resources/output/";

        channelSftp.get(remoteFile, localDir + "TestReconfile.txt");

        channelSftp.exit();
    }
}
