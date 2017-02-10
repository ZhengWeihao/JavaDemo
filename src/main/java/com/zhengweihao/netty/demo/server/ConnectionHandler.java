package com.zhengweihao.netty.demo.server;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import com.zhengweihao.netty.demo.modal.Message;

public class ConnectionHandler extends SimpleChannelHandler {

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		System.out.println("someone connected ..");
	}
	
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		System.out.println("someone disconnected ..");
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		Message message = (Message) e.getMessage();
		System.out.println("someone send:" + message);
		
		e.getChannel().write(new Message("you ok?"));
		System.out.println("server responded ..");
	}
}
