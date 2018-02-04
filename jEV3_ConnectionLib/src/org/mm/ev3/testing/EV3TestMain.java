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
package org.mm.ev3.testing;

import org.mm.ev3.EV3Commands;
import org.mm.ev3.connectors.EV3Connector;
import org.mm.ev3.connectors.EV3UsbConnector;
import org.mm.ev3.connectors.EV3WifiConnector;

public class EV3TestMain {

	public static void main (String args[] ) {
		EV3Connector ev3Connector=null;
		try {

			//			EV3WifiConnector ev3Connector
			if(args!=null && args.length>0 && args[0].equals("wifi")) {
				System.out.println("WIFI");
				ev3Connector = new EV3WifiConnector();
			} else {
				System.out.println("USB");
				ev3Connector = new EV3UsbConnector();
			}
			
			ev3Connector.connect();

			EV3Commands ev3Commands= new EV3Commands(ev3Connector);

			ev3Commands.sendMailBox("abcN", 0.5f);

			System.in.read();
			ev3Commands.sendMailBox("abcN", 123.456789f);
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		} finally {
			try {
				if(ev3Connector!=null)
					ev3Connector.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}

