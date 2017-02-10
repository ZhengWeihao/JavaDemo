package com.zhengweihao.netty.demo.client;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;

import com.zhengweihao.netty.demo.modal.Message;

public class MainClient {

	public static void main(String[] args) throws InterruptedException {
		ExecutorService bossExecutor = Executors.newCachedThreadPool();
		ExecutorService workerExecutor = Executors.newCachedThreadPool();
		NioClientSocketChannelFactory channelFactory = new NioClientSocketChannelFactory(bossExecutor, workerExecutor);
		ClientBootstrap bootstrap = new ClientBootstrap(channelFactory);

		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new ObjectEncoder(),
						new ObjectDecoder(ClassResolvers.cacheDisabled(this.getClass().getClassLoader())),
						new ConnectionHandler());
			}
		});

		InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8888);
		ChannelFuture future = bootstrap.connect(address);
		
		Channel channel = future.getChannel();
		channel.write(new Message("hei hei"));
		
		Thread.sleep(2000);
		bootstrap.shutdown();
		System.exit(0);
	}
}
