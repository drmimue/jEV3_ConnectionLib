package org.mm.ev3.testing;
import java.net.Socket;
import java.net.SocketException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import java.io.*;
import java.util.regex.*;

import org.mm.ev3.types.EV3CommandType;
import org.mm.ev3.types.EV3SystemCommand;

public class EV3_do_nothing_wifi {
	static final byte  opNop                        = (byte)  0x01;
//	static final byte  DIRECT_COMMAND_REPLY         = (byte)  0x00;
//	static final byte  DIRECT_COMMAND_NO_REPLY 		= (byte)  0x80;
	static InputStream in;
	static OutputStream out;
	
//	enum CommandType {
//		DIRECT_COMMAND_REPLY((byte)  0x00),
//		DIRECT_COMMAND_NO_REPLY((byte)  0x80),
//		SYSTEM_COMMAND_REPLY((byte)0x01),
//		SYSTEM_COMMAND_NO_REPLY((byte)0x81);
//		
//		byte bValue;
//		CommandType(byte b) {
//			bValue=b;
//		}
//		
//		public byte value() {
//			return bValue;
//		}
//		
//	}
//	enum SystemCommand {
//		BEGIN_DOWNLOAD((byte)0x92), // Begin file download
//		CONTINUE_DOWNLOAD((byte)0x93), // Continue file download
//		BEGIN_UPLOAD((byte)0x94), // Begin file upload
//		CONTINUE_UPLOAD((byte)0x95), // Continue file upload
//		BEGIN_GETFILE((byte)0x96), // Begin get bytes from a file (while writing to the file)
//		CONTINUE_GETFILE((byte)0x97), // Continue get byte from a file (while writing to the file)
//		CLOSE_FILEHANDLE((byte)0x98), // Close file handle
//		LIST_FILES((byte)0x99), // List files
//		CONTINUE_LIST_FILES((byte)0x9A), // Continue list files
//		CREATE_DIR((byte)0x9B), // Create directory
//		DELETE_FILE((byte)0x9C), // Delete
//		LIST_OPEN_HANDLES((byte)0x9D), // List handles
//		WRITEMAILBOX((byte)0x9E), // Write to mailbox
//		BLUETOOTHPIN((byte)0x9F), // Transfer trusted pin code to brick
//		ENTERFWUPDATE((byte)0xA0); // Restart the brick in Firmware update mode
//		
//		byte bValue;
//		SystemCommand(byte b) {
//			bValue=b;
//		}
//		
//		public byte value() {
//			return bValue;
//		}
//	}
	
//	enum CommandReplyType {
//		SYSTEM_REPLY       ((byte) 0x03),
//		SYSTEM_REPLY_ERROR ((byte) 0x05),
//		DIRECT_REPLY       ((byte) 0x02), // Direct command reply OK
//		DIRECT_REPLY_ERROR ((byte) 0x04);
//		
//		byte bValue;
//		CommandReplyType(byte b) {
//			bValue=b;
//		}
//		
//		public byte value() {
//			return bValue;
//		}
//		
//	}
	
//	enum ReplyCodes {
//		SUCCESS((byte)0x00),
//		UNKNOWN_HANDLE((byte) 0x01),
//		HANDLE_NOT_READY((byte) 0x02),
//		CORRUPT_FILE((byte) 0x03),
//		NO_HANDLES_AVAILABLE((byte) 0x04),
//		NO_PERMISSION((byte) 0x05),
//		ILLEGAL_PATH((byte) 0x06),
//		FILE_EXITS((byte) 0x07),
//		END_OF_FILE((byte) 0x08),
//		SIZE_ERROR((byte) 0x09),
//		UNKNOWN_ERROR((byte) 0x0A),
//		ILLEGAL_FILENAME((byte) 0x0B),
//		ILLEGAL_CONNECTION((byte) 0x0C);
//		
//		byte bValue;
//		ReplyCodes(byte b) {
//			bValue=b;
//		}
//		
//		public byte value() {
//			return bValue;
//		}
//	}

	

//	public static void connectWifi () 	throws IOException, SocketException {
//		
//		// listen for a UDP broadcast from the EV3 on port 3015
//		DatagramSocket listener = new DatagramSocket(3015);
//		DatagramPacket packet_r = new DatagramPacket(new byte[67], 67);
//		listener.receive(packet_r); // receive the broadcast message
//		String broadcast_message = new String(packet_r.getData());
//		
//		/* pick serial number, port, name and protocol 
//        from the broadcast message */
//		Pattern broadcast_pattern = 
//				Pattern.compile("Serial-Number: (\\w*)\\s\\n" +
//						"Port:\\s(\\d{4,4})\\s\\n" +
//						"Name:\\s(\\w+)\\s\\n" +
//						"Protocol:\\s(\\w+)\\s\\n");
//		Matcher matcher = broadcast_pattern.matcher(broadcast_message);
//		String serial_number, name, protocol;
//		int port;
//		if(matcher.matches()) {
//			serial_number = matcher.group(1);
//			port = Integer.valueOf(matcher.group(2));
//			name = matcher.group(3);
//			protocol = matcher.group(4);
//		}
//		else {
//			throw new IOException("Unexpected Broadcast message: " + broadcast_message);
//		}
//		InetAddress adr = packet_r.getAddress();
//		// connect the EV3 with its address and port
//		listener.connect(adr, port);
//		/* Send an UDP message back to the EV3 
//        to make it accept a TCP/IP connection */
//		listener.send(new DatagramPacket(new byte[1], 1));
//		// close the UDP connection
//		listener.close();
//
//		// Establish a TCP/IP connection with EV3s address and port
//		Socket socket = new Socket(adr, port);
//		in     = socket.getInputStream();
//		out    = socket.getOutputStream();
//
//		// Send an unlock message to the EV3 over TCP/IP
//		String unlock_message = "GET /target?sn=" + serial_number + 
//				"VMTP1.0\n" + 
//				"Protocol: " + protocol;
//		out.write(unlock_message.getBytes());
//
//		byte[] reply = new byte[16];           // read reply
//		in.read(reply);
//		if (! (new String(reply)).startsWith("Accept:EV340")) {
//			throw new IOException("No wifi connection established " + name);
//		} 
//	}

//	public static ByteBuffer sendDirectCmd (
//			ByteBuffer operations,
//			int local_mem, 
//			int global_mem) throws IOException {
//		
//		ByteBuffer buffer = ByteBuffer.allocateDirect(operations.position() + 7);
//		buffer.order(ByteOrder.LITTLE_ENDIAN);
//		buffer.putShort((short) (operations.position() + 5));   // length
//		buffer.putShort((short) 42);                            // counter
//		buffer.put(EV3CommandType.DIRECT_COMMAND_REPLY.value());                       // type
//		buffer.putShort((short) (local_mem*1024 + global_mem)); // header
//		for (int i=0; i<operations.position(); i++) {           // operations
//			buffer.put(operations.get(i));
//		}
//
//		byte[] cmd = new byte [buffer.position()];
//		for (int i=0; i<buffer.position(); i++) cmd[i] = buffer.get(i);
//		out.write(cmd);
//		printHex("Sent", buffer);
//
//		byte[] reply = new byte[global_mem + 5];
//		in.read(reply);
//		buffer = ByteBuffer.wrap(reply);
//		buffer.position(reply.length);
//		printHex("Recv", buffer);
//
//		return buffer;
//	}

//	public static void sendMailBox (
//			String key,
//			float value) throws IOException {
//		int len=key.length()+12;
//		System.out.println("len: "+len);
//		ByteBuffer buffer = ByteBuffer.allocateDirect(len+3);
//		buffer.order(ByteOrder.LITTLE_ENDIAN);
//		buffer.putShort((short) (len));   // length
//		buffer.putShort((short) 42);                            // counter
//		buffer.put(EV3CommandType.SYSTEM_COMMAND_NO_REPLY.value());   // type
//		buffer.put(EV3SystemCommand.WRITEMAILBOX.value());   		// type
//		buffer.put((byte)(key.length()+1));   						// type
//		buffer.put(key.getBytes("ASCII"));
//		buffer.put((byte)0);
//		buffer.putShort((short)4);
//		buffer.putFloat(value);
//		
//		byte[] cmd = new byte [buffer.position()];
//		for (int i=0; i<buffer.position(); i++) cmd[i] = buffer.get(i);
//		out.write(cmd);
//		printHex("Sent", buffer);
//
////		byte[] reply = new byte[global_mem + 5];
////		in.read(reply);
////		buffer = ByteBuffer.wrap(reply);
////		buffer.position(reply.length);
////		printHex("Recv", buffer);
////
////		return buffer;
//	}
	
	

//	public static void main (String args[] ) {
//		try {
//			connectWifi();
//
//			
////			ByteBuffer operations = ByteBuffer.allocateDirect(1);
////			operations.put(opNop);
////
////			ByteBuffer reply = sendDirectCmd(operations, 0, 0);
//			
//			ReadThread rt= new ReadThread(in);
//			rt.start();
//			
//			sendMailBox("abcN", 0.5f);
//			
//			System.in.read();
//		}
//		catch (Exception e) {
//			e.printStackTrace(System.err);
//		}
//	}
	
	
	public static class ReadThread extends Thread {
		InputStream in=null;
		
		public  ReadThread(InputStream i) {
			in=i;
		}
		
		public void run() {
			int r=0;
			try {
			while((r=in.read())>=0) {
				System.out.println("read: "+r+" "+Integer.toHexString(r));
			}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
