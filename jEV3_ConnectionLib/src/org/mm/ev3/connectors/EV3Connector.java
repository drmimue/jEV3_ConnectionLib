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

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public abstract class EV3Connector {
	
	
	
	public EV3Connector() {
	}

	public abstract void       connect (                 ) throws Exception;
	public abstract void       close   (                 ) throws Exception;
	public abstract void       sendData(ByteBuffer buffer) throws Exception;
	public abstract ByteBuffer readData(int global_mem   ) throws Exception;
	
	public static EV3Connector getConnection(String type) {
		if(type!=null && type.equalsIgnoreCase("wifi")) {
			return new EV3WifiConnector();
		}
		return new EV3UsbConnector();
	}
}
