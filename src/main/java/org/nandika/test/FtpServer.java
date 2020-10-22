package org.nandika.test;

import org.wso2.carbon.automation.extensions.servers.ftpserver.FTPServerManager;

public class FtpServer {

    String output = "/home/malaka/MyFolder/VfsVersionUpgrade/VFSSenario/OutPut/";

    void startServer() {
        FTPServerManager ftpServerManager = new FTPServerManager(
                8086, output, "admin", "admin");
        ftpServerManager.startFtpServer();
    }

    public static void main(String[] args) {
        new FtpServer().startServer();

    }
}
