package com.yk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


public class Nio {
	public void filecopy() throws IOException{
		File srcFile= new File("D:\\1.jpg");//原始路径
		File targetFile =new File("D:\\cc.jpg");//目标路径
		if(!targetFile.exists()){
			targetFile.createNewFile();
		}
		FileInputStream in=new FileInputStream(srcFile);
		FileOutputStream out =new FileOutputStream(targetFile);
		
		FileChannel inChanl = in.getChannel();
		FileChannel outChanl = out.getChannel();
		
		ByteBuffer dst =ByteBuffer.allocate(1024);
		int length=0;
		System.out.println("复制开始");
		while((length=inChanl.read(dst))!= -1){
//			capacity(容量)：缓冲区能够容纳数据的最大值，创建缓冲区后不能改变。
//			limit(上界)：缓冲区的第一个不能被读或写的元素。或者，缓冲区现存元素的计数。
//			position(位置)：下一个要被读或写的元素的索引。调用 get 或 put 函数会更新。
//			mark(标记)：一个备忘位置。调用 mark() 来设定 mark=postion。调用 reset() 设定position= mark。标记在设定前是未定义的(undefined)。
//
//			这四个属性之间总是 循以下关系:
//			0 <= mark <= position <= limit <= capacity
			System.out.println(dst.position());
			
			dst.flip();//重新设置 posion=0
			outChanl.write(dst);
			
			
			dst.clear();
			
		}
		inChanl.close();
		outChanl.close();
		System.out.println("复制结束");
		
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
