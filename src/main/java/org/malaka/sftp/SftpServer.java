package org.malaka.sftp;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sshd.SshServer;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.Session;
import org.apache.sshd.common.file.FileSystemView;
import org.apache.sshd.common.file.nativefs.NativeFileSystemFactory;
import org.apache.sshd.common.file.nativefs.NativeFileSystemView;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.command.ScpCommandFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.sftp.SftpSubsystem;

import java.io.File;

/**
 * Created by malaka on 3/2/18.
 */
public class SftpServer {

    String output = "/home/malaka/MyFolder/VfsVersionUpgrade/VFSSenario/OutPut/";

    void startServer() {

        String testFolder = "/home/malaka/MyFolder/VfsVersionUpgrade/VFSSenario/sftpOutput/";
        File testFol = new File(testFolder);
        String USERNAME = "admin";
        String PASSWORD = "admin";

        SshServer sshd = SshServer.setUpDefaultServer();
        sshd.setFileSystemFactory(new NativeFileSystemFactory() {
            @Override
            public FileSystemView createFileSystemView(final Session session) {
                return new NativeFileSystemView(session.getUsername(), false) {
                    public String getVirtualUserDir() {
                        return testFol.getAbsolutePath();
                    }
                };
            };
        });
        sshd.setPort(8001);
        sshd.setSubsystemFactories(Arrays.<NamedFactory<Command>>asList(new SftpSubsystem.Factory()));
        sshd.setCommandFactory(new ScpCommandFactory());
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider
                ("/home/malaka/MyFolder/VfsVersionUpgrade/VFSSenario/sftp/id_rsa.pub"));
        sshd.setPasswordAuthenticator(new PasswordAuthenticator() {
            @Override
            public boolean authenticate(final String username, final String password, final ServerSession session) {
                return StringUtils.equals(username, USERNAME) && StringUtils.equals(password, PASSWORD);
            }
        });
        try {
            sshd.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
       /* FTPServerManager ftpServerManager = new FTPServerManager(
                8086, output, "admin", "admin");
        ftpServerManager.startFtpServer();*/
    }

    public static void main(String[] args) {
        SftpServer sftpServer = new SftpServer();
        sftpServer.startServer();
    }

}
