package com.qtfx;

import java.nio.ByteBuffer;

public class TestByteBuffer {

	public static void main(String[] args) {
		ByteBuffer b = ByteBuffer.allocate(8);
		b.putDouble(1.29842734982);
		
		System.out.println(b.capacity());
		System.out.println(b.limit());
		System.out.println(b.remaining());
		
		b.rewind();
		System.out.println();
		System.out.println(b.capacity());
		System.out.println(b.limit());
		System.out.println(b.remaining());
		
		System.out.println();
		for (int i = 0; i < b.limit(); i++) {
			System.out.print(b.get());
		}
		
		System.out.println();
		System.out.println(b.getDouble());
	}

}
