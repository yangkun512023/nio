package com.yk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 
 * @title io
 * @todo io һ�����
 * @param
 * @return 
 * @date Aug 18, 2017
 * @author yangk
 */
public class Io {
	/**
	 * �ļ�����
	 * @throws IOException 
	 */
	public void filecopy() throws IOException{
		File srcFile= new File("D:\\1.jpg");//ԭʼ·��
		File targetFile =new File("D:\\cc.jpg");//Ŀ��·��
		if(!targetFile.exists()){
			targetFile.createNewFile();
		}
		FileInputStream in=new FileInputStream(srcFile);
		FileOutputStream out =new FileOutputStream(targetFile);
		byte[] bts=new byte[1024];
		int length=0;
		System.out.println("���ƿ�ʼ");
		while((length=in.read(bts))!= -1){
			out.write(bts, 0, length);
		}
		System.out.println("���ƽ���");
		
		in.close();
		out.close();
	}
	public static void main(String[] args) throws IOException {
		Io io=new Io();
		io.filecopy();
	}
}
