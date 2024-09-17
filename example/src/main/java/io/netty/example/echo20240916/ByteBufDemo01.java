package io.netty.example.echo20240916;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ResourceLeakDetector;

import java.nio.charset.StandardCharsets;

public class ByteBufDemo01 {
    
    public static void main(String[] args) {
        // leakDetection相关属性的设置：io.netty.util.ResourceLeakDetector
        System.setProperty("io.netty.leakDetection.level", ResourceLeakDetector.Level.PARANOID.name()); // 默认值：Level.SIMPLE
        System.setProperty("io.netty.leakDetection.targetRecords", "4"); // 默认值：4
        
        ByteBuf byteBuf01 = Unpooled.directBuffer(100, 100);
        // 默认Allocator：UnpooledByteBufAllocator
        // Unpooled.directBuffer
        //   -> AbstractByteBufAllocator.directBuffer(int, int)
        //     -> UnpooledByteBufAllocator.newDirectBuffer
        //       -> buf = new UnpooledByteBufAllocator$InstrumentedUnpooledUnsafeNoCleanerDirectByteBuf
        //       -> AbstractByteBufAllocator.toLeakAwareBuffer(buf)
    
        byte[] data = "hello".getBytes();
        byteBuf01.writeBytes(data);
        
        byte[] dataRead = new byte[data.length];
        byteBuf01.readBytes(dataRead);
        System.out.println("read result: " + new String(dataRead));
    
        System.out.println("refCnt: " + byteBuf01.refCnt());
        
        byteBuf01.release();
        // -> io.netty.buffer.AdvancedLeakAwareByteBuf.release()
        //   -> io.netty.util.ResourceLeakTracker.record()
        //     -> io.netty.util.ResourceLeakDetector.DefaultResourceLeak.record0
        //       -> todo: xx
        //   -> io.netty.buffer.SimpleLeakAwareByteBuf.release()
        //    public boolean release() {
        //        if (super.release()) {
        //            closeLeak();
        //            return true;
        //        }
        //        return false;
        //    }
        //     -> io.netty.buffer.WrappedByteBuf.release()
        //        return handleRelease(updater.release(this));
        //       -> io.netty.util.internal.ReferenceCountUpdater.release(T) // 释放引用计数
        //     public final boolean release(T instance) {
        //        int rawCnt = nonVolatileRawCnt(instance);
        //        return rawCnt == 2 ? tryFinalRelease0(instance, 2) || retryRelease0(instance, 1)
        //                : nonFinalRelease0(instance, 1, rawCnt, toLiveRealRefCnt(rawCnt, 1));
        //     }
        //         -> io.netty.util.internal.ReferenceCountUpdater.nonVolatileRawCnt // 获取到当前的(释放前)引用计数为2
        //           -> io.netty.util.internal.PlatformDependent.getInt(java.lang.Object, long)
        //             -> io.netty.util.internal.PlatformDependent0.getInt(java.lang.Object, long)
        //               -> UNSAFE.getInt(object, fieldOffset)
        //         -> io.netty.util.internal.ReferenceCountUpdater.tryFinalRelease0
        //           -> updater().compareAndSet(instance, expectRawCnt, 1) // updater()返回结果的类型：AtomicIntegerFieldUpdater<T> 实际：java.util.concurrent.atomic.AtomicIntegerFieldUpdater$AtomicIntegerFieldUpdaterImpl
        //           // 也就是这里通过CAS方式执行更新，如果成功则
        //           ：return rawCnt == 2 ? (tryFinalRelease0(instance, 2) || retryRelease0(instance, 1))
        //                : nonFinalRelease0(instance, 1, rawCnt, toLiveRealRefCnt(rawCnt, 1));
        //           // 三元运算符（?:）的优先级高于逻辑或运算符（||）
        //           // 如果第一次尝试tryFinalRelease0，如果失败则执行retryRelease0
        //           // retryRelease0会有死循环尝试释放，直到成功或者超出引用计数抛出异常
        //       -> io.netty.buffer.AbstractReferenceCountedByteBuf.handleRelease
        //         -> io.netty.buffer.UnpooledDirectByteBuf.deallocate
        //   protected void deallocate() {
        //        ByteBuffer buffer = this.buffer;
        //        if (buffer == null) {
        //            return;
        //        }
        //
        //        this.buffer = null; // 先将引用置空
        //
        //        if (!doNotFree) {
        //            freeDirect(buffer); // 再释放直接内存
        //        }
        //    }
        //           -> io.netty.buffer.UnpooledDirectByteBuf.freeDirect
        //             -> io.netty.buffer.UnpooledByteBufAllocator.InstrumentedUnpooledUnsafeNoCleanerDirectByteBuf.freeDirect
        //               -> io.netty.buffer.UnpooledUnsafeNoCleanerDirectByteBuf.freeDirect
        //                 -> io.netty.util.internal.PlatformDependent.freeDirectNoCleaner
        //                   -> io.netty.util.internal.PlatformDependent0.freeMemory
        //                     -> UNSAFE.freeMemory(address); // 调用UNSAFE释放内存
        //                   -> io.netty.util.internal.PlatformDependent.decrementMemoryCounter
        //               -> io.netty.buffer.UnpooledByteBufAllocator.decrementDirect
        //                 -> metric.directCounter.add(-amount);
        
        System.out.println("refCnt: " + byteBuf01.refCnt());
        
        
        // 未指定最大，则最大为 io.netty.buffer.AbstractByteBufAllocator.DEFAULT_MAX_CAPACITY
        // DEFAULT_MAX_CAPACITY = Integer.MAX_VALUE
        ByteBuf byteBuf02 = Unpooled.directBuffer(100);
    }
    
}
