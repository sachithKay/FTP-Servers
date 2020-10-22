package org.malaka.sftp;

import org.apache.sshd.SshServer;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.PublickeyAuthenticator;
import org.apache.sshd.server.UserAuth;
import org.apache.sshd.server.auth.UserAuthPublicKey;
import org.apache.sshd.server.command.ScpCommandFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.sftp.SftpSubsystem;

import java.io.File;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;


public class SftpPrivate {

    void startServer() {

        SshServer sshd = SshServer.setUpDefaultServer();
        sshd.setPort(22);

        File privateKey = new File("/Users/Sachith/.ssh/id_rsa.pub");
        System.out.println(" Is exists " + privateKey.exists());
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(privateKey.getAbsolutePath()));
        System.out.println(privateKey.getAbsolutePath());
        List<NamedFactory<UserAuth>> userAuthFactories = new ArrayList<NamedFactory<UserAuth>>();
        userAuthFactories.add(new UserAuthPublicKey.Factory());
        sshd.setUserAuthFactories(userAuthFactories);

        sshd.setPublickeyAuthenticator(new PublickeyAuthenticator() {
            public boolean authenticate(String username, PublicKey key, ServerSession session) {
                if ("sftpuser".equals(username)) {
                    return true;
                }
                return false;
            }
        });

        sshd.setCommandFactory(new ScpCommandFactory());

        List<NamedFactory<Command>> namedFactoryList = new ArrayList<NamedFactory<Command>>();
        namedFactoryList.add(new SftpSubsystem.Factory());
        sshd.setSubsystemFactories(namedFactoryList);

        try {
            sshd.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SftpPrivate sftpPrivate = new SftpPrivate();
        sftpPrivate.startServer();
    }
}
