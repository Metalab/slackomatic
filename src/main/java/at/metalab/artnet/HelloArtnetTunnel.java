package at.metalab.artnet;

import org.apache.commons.lang3.ArrayUtils;

import artnet4j.ArtNet;
import artnet4j.packets.ArtDmxPacket;

public class HelloArtnetTunnel {

	public static void main(String[] args) throws Exception {
		new HelloArtnetTunnel().go(args);
	}

	public void go(String[] args) throws Exception {
		ArtNet artnet = new ArtNet();

		artnet.start();
		
		int leds = 36; // 18 rgb pixel on each side
		
		byte[] color1 = new byte[] {};
		for(int i = 0; i < leds; i++) {
			color1 = ArrayUtils.add(color1, (byte) 255); // r
			color1 = ArrayUtils.add(color1, (byte) 0);   // g
			color1 = ArrayUtils.add(color1, (byte) 255); // b
		}

		byte[] color2 = new byte[] {};
		for(int i = 0; i < leds; i++) {
			color2 = ArrayUtils.add(color2, (byte) 0);   // r
			color2 = ArrayUtils.add(color2, (byte) 255); // g
			color2 = ArrayUtils.add(color2, (byte) 255); // b
		}

		int universe = 0; // Blinkentunnel 2.0
		String ip = "10.20.255.255"; // n.b. broadcast
	
		ArtDmxPacket packetColor1= new ArtDmxPacket();
		packetColor1.setUniverse(0, universe);
		packetColor1.setSequenceID(0);
		packetColor1.setDMX(color1, color1.length);

		ArtDmxPacket packetColor2 = new ArtDmxPacket();
		packetColor2.setUniverse(0, universe);
		packetColor2.setSequenceID(0);
		packetColor2.setDMX(color2, color2.length);
		
		for (;;) {
			Thread.sleep(1000);
			System.out.println("color1");
			for(int i = 0; i < 10; i++) { // send multiple times due to possible packet loss
				artnet.unicastPacket(packetColor1, ip);
			}
			
			Thread.sleep(1000);
			System.out.println("color2");
			for(int i = 0; i < 10; i++) { // send multiple times due to possible packet loss
				artnet.unicastPacket(packetColor2, ip);
			}
		}
	}

}
