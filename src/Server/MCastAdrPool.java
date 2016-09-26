package Server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Stack;

public class MCastAdrPool {
	Stack<InetAddress> stack;


	public MCastAdrPool() {
		stack = new Stack<>();
		for (int i = 255; i >= 0; i--) {
			try {
				stack.push(InetAddress.getByName(new String("225.225.225." + i)));
			} catch (UnknownHostException e) {
				throw new RuntimeException("InetAddress-Erzeugung der MulticastAdressen fehlgeschalgen");
			}
		}
	}


	public InetAddress getMCastAdr() {
		return stack.pop();
	}


	public void returnMCastAdr(InetAddress iadr) {
		stack.push(iadr);
	}


	public boolean isEmpty() {
		return stack.isEmpty();
	}
}
