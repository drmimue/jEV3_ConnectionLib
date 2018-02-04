# jEV3_ConnectionLib
A first try to get access to EV3 from Java. 
Using the "EV3 Communication Developer Kit", I created a small JAVA library. In the first version you can use the WriteMailBox command to 
send messages to EV3. The EV3 can be connected using USB and WIFI.

## Getting Started
Import project into eclipse. Add usb4java 1.2 library, if you want use USB for accessing the EV3.

## Example
```
import org.mm.ev3.EV3Commands;
import org.mm.ev3.connectors.EV3Connector;
import org.mm.ev3.connectors.EV3UsbConnector;
import org.mm.ev3.connectors.EV3WifiConnector;

public class Test {

	public static void main(String[] args) throws Exception {
		EV3Connector ev3Connector=null;
		// use Wifi connector
		ev3Connector=new EV3WifiConnector();
		
		// use usb connector
		//ev3Connector=new EV3UsbConnector();
		
		// connect
		ev3Connector.connect();
		
		// init command class
		EV3Commands ev3Commands= new EV3Commands(ev3Connector);
		
		// writemailbox
		ev3Commands.sendMailBox("command", "beep");
		
		// close the connection
		ev3Connector.close();
	}

}
```
