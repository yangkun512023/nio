package com.yk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


public class Nio {
	public void filecopy() throws IOException{
		File srcFile= new File("D:\\1.jpg");//ԭʼ·��
		File targetFile =new File("D:\\cc.jpg");//Ŀ��·��
		if(!targetFile.exists()){
			targetFile.createNewFile();
		}
		FileInputStream in=new FileInputStream(srcFile);
		FileOutputStream out =new FileOutputStream(targetFile);
		
		FileChannel inChanl = in.getChannel();
		FileChannel outChanl = out.getChannel();
		
		ByteBuffer dst =ByteBuffer.allocate(1024);
		int length=0;
		System.out.println("���ƿ�ʼ");
		while((length=inChanl.read(dst))!= -1){
//			capacity(����)���������ܹ��������ݵ����ֵ���������������ܸı䡣
//			limit(�Ͻ�)���������ĵ�һ�����ܱ�����д��Ԫ�ء����ߣ��������ִ�Ԫ�صļ�����
//			position(λ��)����һ��Ҫ������д��Ԫ�ص����������� get �� put ��������¡�
//			mark(���)��һ������λ�á����� mark() ���趨 mark=postion������ reset() �趨position= mark��������趨ǰ��δ�����(undefined)��
//
//			���ĸ�����֮������ ѭ���¹�ϵ:
//			0 <= mark <= position <= limit <= capacity
			System.out.println(dst.position());
			
			dst.flip();//�������� posion=0
			outChanl.write(dst);
			
			
			dst.clear();
			
		}
		inChanl.close();
		outChanl.close();
		System.out.println("���ƽ���");
		
		in.close();
		out.close();
	}
	public static void main(String[] args) {
		Nio nio=new Nio();
		try {
			nio.filecopy();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
