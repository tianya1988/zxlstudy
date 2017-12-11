//package nio;
//
//import java.io.IOException;
//import java.io.RandomAccessFile;
//import java.nio.channels.FileChannel;
//import java.nio.channels.SelectionKey;
//import java.nio.channels.Selector;
//import java.util.Iterator;
//import java.util.Set;
//
///**
//* Created by jason on 17-12-5.
//*/
//public class SelectorTest {
//    public static void main(String[] args) throws IOException{
//
//        Selector selector = Selector.open();
//        channel.configureBlocking(false);
//        SelectionKey key = channel.register(selector, SelectionKey.OP_READ);
//        while(true) {
//            int readyChannels = selector.select();
//            if(readyChannels == 0) continue;
//            Set selectedKeys = selector.selectedKeys();
//            Iterator keyIterator = selectedKeys.iterator();
//            while(keyIterator.hasNext()) {
//                SelectionKey key = keyIterator.next();
//                if(key.isAcceptable()) {
//                    // a connection was accepted by a ServerSocketChannel.
//                } else if (key.isConnectable()) {
//                    // a connection was established with a remote server.
//                } else if (key.isReadable()) {
//                    // a channel is ready for reading
//                } else if (key.isWritable()) {
//                    // a channel is ready for writing
//                }
//                keyIterator.remove();
//            }
//        }
//    }
//}
