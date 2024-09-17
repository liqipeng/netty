package io.netty.example.echo20240916;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class RefCntOverflowDemo {
    
    public static void main(String[] args) {
        System.out.println("Integer.MAX_VALUE: " + Integer.MAX_VALUE );
        
        ByteBuf buf = Unpooled.directBuffer(1, 1);
        System.out.println(buf.refCnt());
        // buf.retain(Integer.MAX_VALUE / 3); // ok
        buf.retain(Integer.MAX_VALUE / 2);
        System.out.println(buf.refCnt());
    }
    
}
