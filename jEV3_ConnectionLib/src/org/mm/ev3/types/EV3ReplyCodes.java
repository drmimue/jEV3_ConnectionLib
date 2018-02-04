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

public enum EV3ReplyCodes {
	SUCCESS((byte)0x00),
	UNKNOWN_HANDLE((byte) 0x01),
	HANDLE_NOT_READY((byte) 0x02),
	CORRUPT_FILE((byte) 0x03),
	NO_HANDLES_AVAILABLE((byte) 0x04),
	NO_PERMISSION((byte) 0x05),
	ILLEGAL_PATH((byte) 0x06),
	FILE_EXITS((byte) 0x07),
	END_OF_FILE((byte) 0x08),
	SIZE_ERROR((byte) 0x09),
	UNKNOWN_ERROR((byte) 0x0A),
	ILLEGAL_FILENAME((byte) 0x0B),
	ILLEGAL_CONNECTION((byte) 0x0C);
	
	byte bValue;
	EV3ReplyCodes(byte b) {
		bValue=b;
	}
	
	public byte value() {
		return bValue;
	}
}
