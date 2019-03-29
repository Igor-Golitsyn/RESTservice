package utils;


import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;

public class FTPFunctions {
    // Creating FTP Client instance
    FTPClient ftp = null;
    // Constructor to connect to the FTP Server
    public FTPFunctions(String host, int port, String username, String password) throws Exception{
        ftp = new FTPClient();
        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        int reply;
        ftp.connect(host,port);
        //System.out.println("FTP URL is:"+ftp.getDefaultPort());
        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new Exception("Exception in connecting to FTP Server");
        }
        ftp.login(username, password);
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        ftp.enterLocalPassiveMode();
    }
    // Method to upload the String on the FTP Server
    public void uploadFTPFile(String page, String fileName, String hostDir)
            throws Exception {
        InputStream input = new ByteArrayInputStream(page.getBytes(StandardCharsets.UTF_8));
        try {
            //InputStream input = new FileInputStream(new File(localFileFullName));
            this.ftp.storeFile(hostDir + fileName, input);
        } catch (Exception e) {
        } finally {
            input.close();
        }
    }
    // Download the FTP File from the FTP Server
    public void downloadFTPFile(String source, String destination) {
        try (FileOutputStream fos = new FileOutputStream(destination)) {
            this.ftp.retrieveFile(source, fos);
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    public String getFileFromFTP (String source){
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            this.ftp.retrieveFile(source, os);
            String rezult = new String(os.toByteArray(), StandardCharsets.UTF_8);
            os.close();
            return rezult;
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return "";
    }
    // list the files in a specified directory on the FTP
    public boolean listFTPFiles(String directory, String fileName) throws IOException {
// lists files and directories in the current working directory
        boolean verificationFilename = false;
        FTPFile[] files = ftp.listFiles(directory);
        for (FTPFile file : files) {
            String details = file.getName();
            //System.out.println(details);
            if(details.equals(fileName))
            {
                //System.out.println("Correct Filename");
                verificationFilename=details.equals(fileName);
                //assertTrue("Verification Failed: The filename is not updated at the CDN end.",details.equals(fileName));
            }
        }
        return verificationFilename;
    }

    public Calendar getTimeOfFile(String file,String path){
        try {
            FTPFile[] files = ftp.listFiles(path);
            for (FTPFile ftpFile: files){
                if (ftpFile.getName().compareTo(file)==0) return ftpFile.getTimestamp();
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(1900,Calendar.JANUARY,1);
        return calendar;
    }

    // Disconnect the connection to FTP
    public void disconnect(){
        if (this.ftp.isConnected()) {
            try {
                this.ftp.logout();
                this.ftp.disconnect();
            } catch (IOException f) {
// do nothing as file is already saved to server
            }
        }
    }
    // Main method to invoke the above methods
    public static void main(String[] args) {
        try {
            FTPFunctions ftpobj = new FTPFunctions(ConstantManager.FtpHost, ConstantManager.FtpPort, ConstantManager.FtpTorrentUser, ConstantManager.FtpTorrentPassword);
            ftpobj.uploadFTPFile("C:\\Users\\shruti\\Shruti.txt", "Shruti.txt", "/");
            ftpobj.downloadFTPFile("Shruti.txt", "/users/shruti/Shruti.txt");
            System.out.println("FTP File downloaded successfully");
            boolean result = ftpobj.listFTPFiles("/users/shruti", "shruti.txt");
            System.out.println(result);
            ftpobj.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
