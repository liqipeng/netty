package io.netty.example.echo2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ScatteringByteChannel;
import java.nio.channels.SocketChannel;

public class ByteBufDemo {

    public static void main(String[] args) throws IOException {
        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(10);
        byteBuf.writeBytes("hello222222222222222222222222!".getBytes());
        // Transfers the specified source array's data to this buffer starting at the current writerIndex and increases the writerIndex by the number of the transferred bytes (= src.length). If this.writableBytes is less than src.length, ensureWritable(int) will be called in an attempt to expand capacity to accommodate.
        // 进行写操作时，如果可写空间不足，则ensureWritable会尝试扩容

        ByteBuf byteBuf2 = PooledByteBufAllocator.DEFAULT.heapBuffer(10);
        byteBuf2.writeBytes("hello222222222222222222222222!".getBytes());

        java.nio.channels.SocketChannel socketChannel = null;
        final int expectedWrittenBytes = byteBuf2.readableBytes();
        byteBuf2.writeBytes(socketChannel, expectedWrittenBytes);
    }

}
