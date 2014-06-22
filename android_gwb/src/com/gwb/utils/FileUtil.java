package com.gwb.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {

//	
//	public static boolean createPath(String path) {
//		
//		
//		
//	}
	
	/**
	 * 复制原来的文件之后删除原来的文件。
	 * 
	 * 
	 * **/
	public static void copyFile(File sourceFile,File targetFile) throws Exception{
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		targetFile.createNewFile();
		try {
			inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
			outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
			
//			缓冲数组
			byte[] b = new byte[1024*5];
			int len;
			while ((len=inBuff.read(b))!=-1) {
				outBuff.write(b,0,len);
			}
			outBuff.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (inBuff!=null) {
				inBuff.close();
			}
			if (outBuff!=null) {
				outBuff.close();
			}
		}
//		delFile(sourceFile.getAbsolutePath());
	}
	
//	/**
//	 * 复制原来的文件之后删除原来的文件。
//	 * 
//	 * 
//	 * **/
//	public static void copyFile(File sourceFile,File targetFilePath) throws Exception{
//		BufferedInputStream inBuff = null;
//		BufferedOutputStream outBuff = null;
//		
//		try {
//			inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
//			outBuff = new BufferedOutputStream(new FileOutputStream(targetFilePath));
//			
////			缓冲数组
//			byte[] b = new byte[1024*5];
//			int len;
//			while ((len=inBuff.read(b))!=-1) {
//				outBuff.write(b,0,len);
//			}
//			outBuff.flush();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally{
//			if (inBuff!=null) {
//				inBuff.close();
//			}
//			if (outBuff!=null) {
//				outBuff.close();
//			}
//		}
////		delFile(sourceFile.getAbsolutePath());
//	}
	
	
	
	/**
     * 
     * @param filepath
     * @throws IOException
     */
    public static void del(String filepath) throws IOException {
        File f = new File(filepath);// 定义文件路径
        if (f.exists() && f.isDirectory()) {// 判断是文件还是目录
            if (f.listFiles().length == 0) {// 若目录下没有文件则直接删除
                f.delete();
            } else {// 若有则把文件放进数组，并判断是否有下级目录
                File delFile[] = f.listFiles();
                int i = f.listFiles().length;
                for (int j = 0; j < i; j++) {
                    if (delFile[j].isDirectory()) {
                        del(delFile[j].getAbsolutePath());// 递归调用del方法并取得子目录路径
                    }
                    delFile[j].delete();// 删除文件
                }
            }
        }
    }
    
   
    /**
     * 
     * @param filepath
     * @throws IOException
     */
    public static void delFile(String filepath) throws IOException {
        File f = new File(filepath);// 定义文件路径
        if (f.exists() && f.isFile()) {// 判断是文件还是目录
           f.delete();
        }
    }
	
    public static void main(String[] args) {
    	File sourceFile = new File("C:\\Users\\Administrator\\Desktop\\test.txt");
    	File destFile = new File("C:\\Users\\Administrator\\Desktop\\test1.txt");
    	
		try {
			FileUtil.copyFile(sourceFile, destFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
