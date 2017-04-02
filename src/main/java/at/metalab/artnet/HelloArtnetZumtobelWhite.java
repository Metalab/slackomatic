package at.metalab.artnet;

import org.apache.commons.lang3.ArrayUtils;

import artnet4j.ArtNet;
import artnet4j.packets.ArtDmxPacket;

public class HelloArtnetZumtobelWhite {

	public static void main(String[] args) throws Exception {
		new HelloArtnetZumtobelWhite().go(args);
	}

	public void go(String[] args) throws Exception {
		ArtNet artnet = new ArtNet();

		artnet.start();
		
		byte[] color1 = new byte[] {};
		// Lampe 1
		color1 = ArrayUtils.add(color1, (byte) 0);   // r
		color1 = ArrayUtils.add(color1, (byte) 0);   // g
		color1 = ArrayUtils.add(color1, (byte) 0);   // b
		color1 = ArrayUtils.add(color1, (byte) 255); // a
		color1 = ArrayUtils.add(color1, (byte) 255); // w		
		// Lampe 2
		color1 = ArrayUtils.add(color1, (byte) 0);   // r
		color1 = ArrayUtils.add(color1, (byte) 0);   // g
		color1 = ArrayUtils.add(color1, (byte) 0);   // b
		color1 = ArrayUtils.add(color1, (byte) 255); // a
		color1 = ArrayUtils.add(color1, (byte) 255); // w		

		byte[] color2 = new byte[] {};
		// Lampe 1
		color2 = ArrayUtils.add(color2, (byte) 0); // r
		color2 = ArrayUtils.add(color2, (byte) 0); // g
		color2 = ArrayUtils.add(color2, (byte) 0); // b
		color2 = ArrayUtils.add(color2, (byte) 0); // a
		color2 = ArrayUtils.add(color2, (byte) 0); // w
		// Lampe 2
		color2 = ArrayUtils.add(color2, (byte) 0); // r
		color2 = ArrayUtils.add(color2, (byte) 0); // g
		color2 = ArrayUtils.add(color2, (byte) 0); // b
		color2 = ArrayUtils.add(color2, (byte) 0); // a
		color2 = ArrayUtils.add(color2, (byte) 0); // w

		int universe = 1; // Zumtobel
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
