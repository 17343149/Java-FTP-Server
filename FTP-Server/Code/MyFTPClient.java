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
	 * 初始化FTP服务器
	 */
	public void initFTPClient()
	{
		myFTP = new FTPClient();
		myFTP.setControlEncoding("utf-8");
		try {
			System.out.println("Connecting ftp服务器: " + hostname + " : " + port);
			myFTP.connect(hostname, port);
			myFTP.login(username, password);
			int reply = myFTP.getReplyCode();
			if(!FTPReply.isPositiveCompletion(reply)) 
				System.out.println("Connect failed!");
			else 
				System.out.println("Connect successful!");
		}catch(Exception err) {
			System.out.println("初始化失败!");
			err.printStackTrace();
		}
	}
	
	/**
	 * 上传文件
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
			System.out.println("上传文件中...");
			inputStream = new FileInputStream(new File(originFileName));
			initFTPClient();
			myFTP.setFileType(myFTP.BINARY_FILE_TYPE);
			
			//如果不存在就创建目录
			myFTP.makeDirectory(pathName);
			
			changeWorkingDirectory(pathName);
			myFTP.storeFile(fileName, inputStream);
			inputStream.close();
			myFTP.logout();
			flag = true;
			System.out.println("上传文件" + fileName + "成功!");
		}catch(Exception err) {
			System.out.println("上传文件失败!");
			err.printStackTrace();
		}finally {
			if(myFTP.isConnected()) {
				try {
					myFTP.disconnect();
				}catch(Exception err) {
					System.out.println("关闭FTP服务器失败!");
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
	 * 改变目录路径
	 */
	public boolean changeWorkingDirectory(String directory) {
		boolean flag = true;
		try {
			flag = myFTP.changeWorkingDirectory(directory);
			if(flag)
				System.out.println("进入文件夹 " + directory + "成功!");
			else {
				System.out.println("进入文件夹 " + directory + "失败!");
				flag = false;
			}
		}catch(Exception err) {
			System.out.println("Something wrong in change directory!");
		}
		return flag;
	}
    
  /**
   * 判断ftp服务器文件是否存在    
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
     * 创建目录
     * @param dir
     * @return
     */
    public boolean makeDirectory(String dir) {
        boolean flag = true;
        try {
            flag = myFTP.makeDirectory(dir);
            if (flag) {
                System.out.println("创建文件夹" + dir + " 成功！");

            } else {
                System.out.println("创建文件夹" + dir + " 失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    /** 下载文件
     * @param pathname FTP服务器文件目录
     * @param filename 文件名称
     * @param localpath 下载后的文件路径
     * */
     public boolean downloadFile(String pathname, String filename, String localpath){ 
         boolean flag = false; 
         OutputStream os=null;
         try { 
        	 initFTPClient();
             //切换FTP目录 
             myFTP.changeWorkingDirectory(pathname); 
             FTPFile[] ftpFiles = myFTP.listFiles();
             for(FTPFile file : ftpFiles){ 
                 if(filename.equalsIgnoreCase(file.getName())){ 
                	 System.out.println("开始下载文件 " + file.getName());
                     File localFile = new File(localpath + file.getName()); 
                     System.out.println(localpath + file.getName());
                     os = new FileOutputStream(localFile); 
                     myFTP.retrieveFile(file.getName(), os); 
                     os.close(); 
                     flag = true; 
                 } 
             } 
             if(flag)
            	 System.out.println("下载文件成功!");
             else
            	 System.out.println("下载文件失败!");
     
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
      * show目录下的文件
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
    	
    	//根目录下的文件
    	myftp.showFiles("");
    	
    	//上传文件到根目录
    	myftp.uploadFile("");
    	
    	//下载ftp根目录文件test.txt到D://
    	myftp.downloadFile("", "test.txt", "D://");
    }
	
}
