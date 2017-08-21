package com.yk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 
 * @title io
 * @todo io 一般调用
 * @param
 * @return 
 * @date Aug 18, 2017
 * @author yangk
 */
public class Io {
	/**
	 * 文件复制
	 * @throws IOException 
	 */
	public void filecopy() throws IOException{
		File srcFile= new File("D:\\1.jpg");//原始路径
		File targetFile =new File("D:\\cc.jpg");//目标路径
		if(!targetFile.exists()){
			targetFile.createNewFile();
		}
		FileInputStream in=new FileInputStream(srcFile);
		FileOutputStream out =new FileOutputStream(targetFile);
		byte[] bts=new byte[1024];
		int length=0;
		System.out.println("复制开始");
		while((length=in.read(bts))!= -1){
			out.write(bts, 0, length);
		}
		System.out.println("复制结束");
		
		in.close();
		out.close();
	}
	public static void main(String[] args) throws IOException {
		Io io=new Io();
		io.filecopy();
	}
}
