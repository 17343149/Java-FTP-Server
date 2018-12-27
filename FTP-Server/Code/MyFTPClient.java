package FTPServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class MyFTPClient {
	public String hostname = "127.0.0.1";
	public int port = 21;
	public String username = "root";
	public String password = "123";
	public FTPClient myFTP = null;
	
	/**
	 * ��ʼ��FTP������
	 */
	public void initFTPClient()
	{
		myFTP = new FTPClient();
		myFTP.setControlEncoding("utf-8");
		try {
			System.out.println("Connecting ftp������: " + hostname + " : " + port);
			myFTP.connect(hostname, port);
			myFTP.login(username, password);
			int reply = myFTP.getReplyCode();
			if(!FTPReply.isPositiveCompletion(reply)) 
				System.out.println("Connect failed!");
			else 
				System.out.println("Connect successful!");
		}catch(Exception err) {
			System.out.println("��ʼ��ʧ��!");
			err.printStackTrace();
		}
	}
	
	/**
	 * �ϴ��ļ�
	 */
	public boolean uploadFile(String pathName) {
		boolean flag = false;
		InputStream inputStream = null;
		String originFileName = null;
		String fileName = null;
		int temp;
		try {
			MyUI tempUI = new MyUI();
			while(tempUI.getStr() == null) {System.out.print("");}
			originFileName = tempUI.getStr();
			fileName = tempUI.fileName;
			System.out.println("�ϴ��ļ���...");
			inputStream = new FileInputStream(new File(originFileName));
			initFTPClient();
			myFTP.setFileType(myFTP.BINARY_FILE_TYPE);
			
			//��������ھʹ���Ŀ¼
			myFTP.makeDirectory(pathName);
			
			changeWorkingDirectory(pathName);
			myFTP.storeFile(fileName, inputStream);
			inputStream.close();
			myFTP.logout();
			flag = true;
			System.out.println("�ϴ��ļ�" + fileName + "�ɹ�!");
		}catch(Exception err) {
			System.out.println("�ϴ��ļ�ʧ��!");
			err.printStackTrace();
		}finally {
			if(myFTP.isConnected()) {
				try {
					myFTP.disconnect();
				}catch(Exception err) {
					System.out.println("�ر�FTP������ʧ��!");
					err.printStackTrace();
				}
			}
			if(inputStream != null) {
				try {
					inputStream.close();
				}catch(Exception err) {
					System.out.println("InputStream close failed!");
				}
			}
		}
		return flag;
	}
	
	/**
	 * �ı�Ŀ¼·��
	 */
	public boolean changeWorkingDirectory(String directory) {
		boolean flag = true;
		try {
			flag = myFTP.changeWorkingDirectory(directory);
			if(flag)
				System.out.println("�����ļ��� " + directory + "�ɹ�!");
			else {
				System.out.println("�����ļ��� " + directory + "ʧ��!");
				flag = false;
			}
		}catch(Exception err) {
			System.out.println("Something wrong in change directory!");
		}
		return flag;
	}
    
  /**
   * �ж�ftp�������ļ��Ƿ����    
   * @param path
   * @return
   * @throws IOException
   */
    public boolean existFile(String path) throws IOException {
            boolean flag = false;
            FTPFile[] ftpFileArr = myFTP.listFiles(path);
            if (ftpFileArr.length > 0) {
                flag = true;
            }
            return flag;
        }
    
    /**
     * ����Ŀ¼
     * @param dir
     * @return
     */
    public boolean makeDirectory(String dir) {
        boolean flag = true;
        try {
            flag = myFTP.makeDirectory(dir);
            if (flag) {
                System.out.println("�����ļ���" + dir + " �ɹ���");

            } else {
                System.out.println("�����ļ���" + dir + " ʧ�ܣ�");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    /** �����ļ�
     * @param pathname FTP�������ļ�Ŀ¼
     * @param filename �ļ�����
     * @param localpath ���غ���ļ�·��
     * */
     public boolean downloadFile(String pathname, String filename, String localpath){ 
         boolean flag = false; 
         OutputStream os=null;
         try { 
        	 initFTPClient();
             //�л�FTPĿ¼ 
             myFTP.changeWorkingDirectory(pathname); 
             FTPFile[] ftpFiles = myFTP.listFiles();
             for(FTPFile file : ftpFiles){ 
                 if(filename.equalsIgnoreCase(file.getName())){ 
                	 System.out.println("��ʼ�����ļ� " + file.getName());
                     File localFile = new File(localpath + file.getName()); 
                     System.out.println(localpath + file.getName());
                     os = new FileOutputStream(localFile); 
                     myFTP.retrieveFile(file.getName(), os); 
                     os.close(); 
                     flag = true; 
                 } 
             } 
             if(flag)
            	 System.out.println("�����ļ��ɹ�!");
             else
            	 System.out.println("�����ļ�ʧ��!");
     
             myFTP.logout(); 
         } catch (Exception e) { 
             e.printStackTrace(); 
         } finally{ 
             if(myFTP.isConnected()){ 
                 try{
                     myFTP.disconnect();
                 }catch(IOException e){
                     e.printStackTrace();
                 }
             } 
             if(null != os){
                 try {
                     os.close();
                 } catch (IOException e) {
                     e.printStackTrace();
                 } 
             } 
         } 
         return flag; 
     }
     
     /**
      * showĿ¼�µ��ļ�
      * @param args
      */
     public void showFiles(String pathName) {
    	 try {
    		 initFTPClient();
    		 if(myFTP.changeWorkingDirectory(pathName)) {
	    		 FTPFile[] ftpFiles = myFTP.listFiles();
	    		 System.out.println("In path: " + pathName);
	    		 for(FTPFile myFile : ftpFiles) {
	    			 System.out.print(myFile.getName() + "\t");
	    		 }
	    		 System.out.println();
    		 }
    	 }catch(Exception err) {
    		 err.printStackTrace();
    	 }
     }
    
    public static void main(String[]args) {
    	MyFTPClient myftp = new MyFTPClient();
    	
    	//��Ŀ¼�µ��ļ�
    	myftp.showFiles("");
    	
    	//�ϴ��ļ�����Ŀ¼
    	myftp.uploadFile("");
    	
    	//����ftp��Ŀ¼�ļ�test.txt��D://
    	myftp.downloadFile("", "test.txt", "D://");
    }
	
}
