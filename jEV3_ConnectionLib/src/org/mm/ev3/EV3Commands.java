/**
MIT License

Copyright (c) 2018 Michael Münchhofen

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package org.mm.ev3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.mm.ev3.connectors.EV3Connector;
import org.mm.ev3.types.EV3CommandType;
import org.mm.ev3.types.EV3SystemCommand;

public class EV3Commands {

	private EV3Connector ev3Connector;

	public EV3Commands(EV3Connector ev3Con) {
		this.ev3Connector=ev3Con;
	}
	
	public void sendMailBox (
			String key,
			float value) throws Exception {
	
		sendMailBox((short)42, key, value);
	}
	
	public void sendMailBox (
			short counter,
			String key,
			float value) throws Exception {
			int len=key.length()+12;
		ByteBuffer buffer = ByteBuffer.allocateDirect(len+3);
		
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort((short) (len));   								// length
		buffer.putShort(counter);                               		// counter
		buffer.put(EV3CommandType.SYSTEM_COMMAND_NO_REPLY.value());   	// type
		buffer.put(EV3SystemCommand.WRITEMAILBOX.value());   			// type
		buffer.put((byte)(key.length()+1));   							// key length with 0
		buffer.put(key.getBytes("ASCII"));
		buffer.put((byte)0);
		buffer.putShort((short)4);                                      // float has length 4
		buffer.putFloat(value);
		ev3Connector.sendData(buffer);
	}
	
	
	public void sendMailBox (
			int counter,
			String key,
			Boolean value) throws Exception {
		System.out.println("not implemented");
	}
	
	public void sendMailBox (
			String key,
			String value) throws Exception {
		sendMailBox((short) 42, key,  value); 
	}
	
	
	public void sendMailBox (
			short counter,
			String key,
			String value) throws Exception {
		
		int len=key.length()+value.length()+8;
		
		ByteBuffer buffer = ByteBuffer.allocateDirect(len+3);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort((short) (len));                                // length
		buffer.putShort(counter);                                      // counter
		buffer.put(EV3CommandType.SYSTEM_COMMAND_NO_REPLY.value());    // type
		buffer.put(EV3SystemCommand.WRITEMAILBOX.value());   		   // type
		buffer.put((byte)(key.length()+1));   						   // key length with 0
		buffer.put(key.getBytes("ASCII"));
		buffer.put((byte)0);
		buffer.putShort((short)(value.length()+1));                    // string value with 0
		buffer.put(value.getBytes("ASCII"));
		buffer.put((byte)0);
		ev3Connector.sendData(buffer);
	}
	public ByteBuffer sendDirectCmd (
			ByteBuffer operations,
			int local_mem, 
			int global_mem) throws Exception {
		return sendDirectCmd((short)42, operations, local_mem, global_mem);
	}
	public ByteBuffer sendDirectCmd (
			short counter,
			ByteBuffer operations,
			int local_mem, 
			int global_mem) throws Exception {
		
		ByteBuffer buffer = ByteBuffer.allocateDirect(operations.position() + 7);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort((short) (operations.position() + 5));   // length
		buffer.putShort(counter);                            // counter
		buffer.put(EV3CommandType.DIRECT_COMMAND_REPLY.value());                       // type
		buffer.putShort((short) (local_mem*1024 + global_mem)); // header
		for (int i=0; i<operations.position(); i++) {           // operations
			buffer.put(operations.get(i));
		}

		ev3Connector.sendData(buffer);
		return ev3Connector.readData(global_mem);
	}
}
