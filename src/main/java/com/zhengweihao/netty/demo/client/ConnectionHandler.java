package com.zhengweihao.netty.demo.client;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import com.zhengweihao.netty.demo.modal.Message;

public class ConnectionHandler extends SimpleChannelHandler {

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		System.out.println("client connected ..");
		
		e.getChannel().write(new Message("hello server"));
		System.out.println("client said hi ..");
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		Message message = (Message) e.getMessage();
		System.out.println("server said:" + message);
		
		e.getChannel().write(new Message("bye bye"));
		System.out.println("client said bye");
	}

}
