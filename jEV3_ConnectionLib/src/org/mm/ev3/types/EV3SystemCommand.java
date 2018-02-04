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

public enum EV3SystemCommand {
	BEGIN_DOWNLOAD((byte)0x92), // Begin file download
	CONTINUE_DOWNLOAD((byte)0x93), // Continue file download
	BEGIN_UPLOAD((byte)0x94), // Begin file upload
	CONTINUE_UPLOAD((byte)0x95), // Continue file upload
	BEGIN_GETFILE((byte)0x96), // Begin get bytes from a file (while writing to the file)
	CONTINUE_GETFILE((byte)0x97), // Continue get byte from a file (while writing to the file)
	CLOSE_FILEHANDLE((byte)0x98), // Close file handle
	LIST_FILES((byte)0x99), // List files
	CONTINUE_LIST_FILES((byte)0x9A), // Continue list files
	CREATE_DIR((byte)0x9B), // Create directory
	DELETE_FILE((byte)0x9C), // Delete
	LIST_OPEN_HANDLES((byte)0x9D), // List handles
	WRITEMAILBOX((byte)0x9E), // Write to mailbox
	BLUETOOTHPIN((byte)0x9F), // Transfer trusted pin code to brick
	ENTERFWUPDATE((byte)0xA0); // Restart the brick in Firmware update mode

	byte bValue;
	EV3SystemCommand(byte b) {
		bValue=b;
	}

	public byte value() {
		return bValue;
	}
}
