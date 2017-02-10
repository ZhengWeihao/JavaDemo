package com.zhengweihao.netty.demo.modal;

import java.io.Serializable;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public class Message implements Serializable {

	private static final long serialVersionUID = -1602544582582307838L;
	
	public Message(String msg) {
		this.setMsg(msg);
	}
	
	private String msg;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	@Override
	public String toString() {
		return msg;
	}
	
	public ChannelBuffer getChannelBuffer() {
		ChannelBuffer cb = ChannelBuffers.buffer(msg.length() * 2);
		cb.writeBytes(msg.getBytes());
		return cb;
	}
	

}
