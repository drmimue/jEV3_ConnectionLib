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
package org.mm.ev3.types;

public enum EV3CommandType {
	DIRECT_COMMAND_REPLY((byte)  0x00),
	DIRECT_COMMAND_NO_REPLY((byte)  0x80),
	SYSTEM_COMMAND_REPLY((byte)0x01),
	SYSTEM_COMMAND_NO_REPLY((byte)0x81);
	
	byte bValue;
	EV3CommandType(byte b) {
		bValue=b;
	}
	
	public byte value() {
		return bValue;
	}
}
