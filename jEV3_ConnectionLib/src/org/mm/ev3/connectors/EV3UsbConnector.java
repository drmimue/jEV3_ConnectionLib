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
package org.mm.ev3.connectors;
import java.net.Socket;
import java.net.SocketException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import java.io.*;
import java.util.regex.*;


import org.mm.ev3.EV3Utils;
import org.mm.ev3.types.EV3CommandType;
import org.mm.ev3.types.EV3SystemCommand;
import org.usb4java.Context;
import org.usb4java.Device;
import org.usb4java.DeviceDescriptor;
import org.usb4java.DeviceHandle;
import org.usb4java.DeviceList;
import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ByteOrder;

public class EV3UsbConnector extends EV3Connector{

	static final short ID_VENDOR_LEGO = (short) 0x0694;
	static final short ID_PRODUCT_EV3 = (short) 0x0005;
	static final byte  EP_IN          = (byte)  0x82;
	static final byte  EP_OUT         = (byte)  0x01;

	//	static final byte  opNop                        = (byte)  0x01;
	//	static final byte  DIRECT_COMMAND_REPLY         = (byte)  0x00;

	DeviceHandle handle;
	Context context;

	public void connect () throws Exception {
		context = new Context();
		int result = LibUsb.init(context);
		if (result != LibUsb.SUCCESS) throw new LibUsbException("Unable to initialize libusb.", result);

		Device device = null;
		DeviceList list = new DeviceList();
		result = LibUsb.getDeviceList(null, list);
		if (result < 0){
			throw new RuntimeException("Unable to get device list. Result=" + result);
		}
		boolean found = false;
		for (Device dev: list) {
			DeviceDescriptor descriptor = new DeviceDescriptor();
			result = LibUsb.getDeviceDescriptor(dev, descriptor);
			if (result != LibUsb.SUCCESS) {
				throw new LibUsbException("Unable to read device descriptor", result);
			}
//			System.out.println(descriptor.idVendor()+" ("+ID_VENDOR_LEGO+") "+descriptor.idProduct()+" ("+ID_PRODUCT_EV3+")");
			if (  descriptor.idVendor()  == ID_VENDOR_LEGO
					|| descriptor.idProduct() == ID_PRODUCT_EV3) {
				System.out.println("device: "+descriptor.idVendor()+" : "+descriptor.idProduct()+ " "+descriptor.toString());
				device = dev;
				found = true;
				break;
			}
		}
		if (! found) {
			LibUsb.freeDeviceList(list, true);
			throw new RuntimeException("Lego EV3 device not found.");
		}

		handle = new DeviceHandle(); 
		result = LibUsb.open(device, handle);
		System.out.println("device: "+device.toString());

		if (result != LibUsb.SUCCESS) {
			LibUsb.freeDeviceList(list, true);
			throw new LibUsbException("Unable to open USB device", result);
		}
		boolean detach = LibUsb.kernelDriverActive(handle, 0) != 0;

		if (detach) result = LibUsb.detachKernelDriver(handle, 0);
		if (result != LibUsb.SUCCESS && result != LibUsb.ERROR_NOT_SUPPORTED) {
			LibUsb.freeDeviceList(list, true);
			throw new LibUsbException("Unable to detach kernel driver", result);
		}

		result = LibUsb.claimInterface(handle, 0);
		LibUsb.freeDeviceList(list, true);
		if (result != LibUsb.SUCCESS) {
			throw new LibUsbException("Unable to claim interface", result);
		}
	}

	public void sendData(ByteBuffer buffer)  throws Exception {
		IntBuffer transferred = IntBuffer.allocate(1);
		int result = LibUsb.bulkTransfer(handle, EP_OUT, buffer, transferred, 1000); 
//		System.out.println("result: "+result+" "+LibUsb.ERROR_BUSY+" "+LibUsb.ERROR_INVALID_PARAM);
		if (result != LibUsb.SUCCESS) {
			System.out.println("result: "+result+" "+LibUsb.errorName(result));
			throw new LibUsbException("Unable to write data("+result+")", transferred.get(0));
		}
		EV3Utils.printHex("Sent", buffer);
	}

	public ByteBuffer readData(int global_mem) throws Exception {
		ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
		IntBuffer transferred = IntBuffer.allocate(1);
		int result = LibUsb.bulkTransfer(handle, EP_IN, buffer, transferred, 100);
		if (result != LibUsb.SUCCESS) {
			throw new LibUsbException("Unable to read data("+result+")", result);
		}
		buffer.position(global_mem + 5);
		EV3Utils.printHex("Recv", buffer);
		return buffer;
	}

	public void close () throws Exception{
		LibUsb.releaseInterface(handle, 0);
		LibUsb.close(handle);
		LibUsb.exit(context);
	}


	//	public  ByteBuffer sendDirectCmd (ByteBuffer operations,
	//			int local_mem, int global_mem) {
	//		ByteBuffer buffer = ByteBuffer.allocateDirect(operations.position() + 7);
	//		buffer.order(ByteOrder.LITTLE_ENDIAN);
	//		buffer.putShort((short) (operations.position() + 5));   // length
	//		buffer.putShort((short) 42);                            // counter
	//		buffer.put(DIRECT_COMMAND_REPLY);                       // type
	//		buffer.putShort((short) (local_mem*1024 + global_mem)); // header
	//		for (int i=0; i < operations.position(); i++) {         // operations
	//			buffer.put(operations.get(i));
	//		}
	//
	//		IntBuffer transferred = IntBuffer.allocate(1);
	//		int result = LibUsb.bulkTransfer(handle, EP_OUT, buffer, transferred, 100); 
	//		if (result != LibUsb.SUCCESS) {
	//			throw new LibUsbException("Unable to write data", transferred.get(0));
	//		}
	//		EV3Utils.printHex("Sent", buffer);
	//
	//		buffer = ByteBuffer.allocateDirect(1024);
	//		transferred = IntBuffer.allocate(1);
	//		result = LibUsb.bulkTransfer(handle, EP_IN, buffer, transferred, 100);
	//		if (result != LibUsb.SUCCESS) {
	//			throw new LibUsbException("Unable to read data", result);
	//		}
	//		buffer.position(global_mem + 5);
	//		EV3Utils.printHex("Recv", buffer);
	//
	//		return buffer;
	//	}



	//	public void connect() 	throws Exception {
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
	//		setIn(socket.getInputStream());
	//		setOut(socket.getOutputStream());
	//
	//		// Send an unlock message to the EV3 over TCP/IP
	//		String unlock_message = "GET /target?sn=" + serial_number + 
	//				"VMTP1.0\n" + 
	//				"Protocol: " + protocol;
	//		getOut().write(unlock_message.getBytes());
	//
	//		byte[] reply = new byte[16];           // read reply
	//		getIn().read(reply);
	//		if (! (new String(reply)).startsWith("Accept:EV340")) {
	//			throw new IOException("No wifi connection established " + name);
	//		} 
	//	}


	//	public ByteBuffer sendDirectCmd (
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
	//		getOut().write(cmd);
	//		EV3Utils.printHex("Sent", buffer);
	//
	//		byte[] reply = new byte[global_mem + 5];
	//		getIn().read(reply);
	//		buffer = ByteBuffer.wrap(reply);
	//		buffer.position(reply.length);
	//		EV3Utils.printHex("Recv", buffer);
	//
	//		return buffer;
	//	}
	//
	//	public void sendMailBox (
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
	//		getOut().write(cmd);
	//		EV3Utils.printHex("Sent", buffer);
	//
	////		byte[] reply = new byte[global_mem + 5];
	////		getIn().read(reply);
	////		buffer = ByteBuffer.wrap(reply);
	////		buffer.position(reply.length);
	////		printHex("Recv", buffer);
	////
	////		return buffer;
	//	}

	//	public void sendMailBox (
	//			String key,
	//			String value) throws IOException {
	//		int len=key.length()+value.length()+8;
	//		System.out.println("len: "+len);
	//		ByteBuffer buffer = ByteBuffer.allocateDirect(len+3);
	//		buffer.order(ByteOrder.LITTLE_ENDIAN);
	//		buffer.putShort((short) (len));   // length
	//		buffer.putShort((short) 42);                            // counter
	//		buffer.put(EV3CommandType.SYSTEM_COMMAND_NO_REPLY.value());   // type
	//		buffer.put(EV3SystemCommand.WRITEMAILBOX.value());   		// type
	//		buffer.put((byte)(key.length()+1));   						// type
	//		buffer.put(key.getBytes("ASCII"));
	//		buffer.putShort((short)(value.length()));   						// type
	//		buffer.put(value.getBytes("ASCII"));
	//
	//		byte[] cmd = new byte [buffer.position()];
	//		for (int i=0; i<buffer.position(); i++) cmd[i] = buffer.get(i);
	//		getOut().write(cmd);
	//		EV3Utils.printHex("Sent", buffer);
	//	}


	//	public static class ReadThread extends Thread {
	//		EV3Connector ev3Con=null;
	//
	//		public  ReadThread(EV3Connector ev3C) {
	//			ev3Con=ev3C;
	//		}
	//
	//		public void run() {
	//			int r=0;
	//			try {
	//				while((r=ev3Con.getIn().read())>=0) {
	//					System.out.println("read: "+r+" "+Integer.toHexString(r));
	//				}
	//			}catch(Exception e) {
	//				e.printStackTrace();
	//			}
	//		}
	//	}
}
