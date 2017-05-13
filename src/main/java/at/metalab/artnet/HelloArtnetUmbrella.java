package at.metalab.artnet;

import org.apache.commons.lang3.ArrayUtils;

import artnet4j.ArtNet;
import artnet4j.packets.ArtDmxPacket;

public class HelloArtnetUmbrella {

	public static void main(String[] args) throws Exception {
		new HelloArtnetUmbrella().go(args);
	}

	public void go(String[] args) throws Exception {
		final int numUmbrellas = 12;
		final int artnetUniverse = 3;
		final int artnetSubnet = 0;
		final String ip = "10.20.255.255"; // n.b. broadcast
		final int resends = 10;

		ArtNet artnet = new ArtNet();

		artnet.start();

		// payload for a single umbrella
		byte[] color1 = new byte[] {};
		color1 = ArrayUtils.add(color1, (byte) 255); // r
		color1 = ArrayUtils.add(color1, (byte) 0); // g
		color1 = ArrayUtils.add(color1, (byte) 255); // b
		color1 = ArrayUtils.add(color1, (byte) 0); // a
		color1 = ArrayUtils.add(color1, (byte) 0); // w

		// payload for a single umbrella
		byte[] color2 = new byte[] {};
		color2 = ArrayUtils.add(color2, (byte) 0); // r
		color2 = ArrayUtils.add(color2, (byte) 0); // g
		color2 = ArrayUtils.add(color2, (byte) 0); // b
		color2 = ArrayUtils.add(color2, (byte) 255); // a
		color2 = ArrayUtils.add(color2, (byte) 0); // w

		// payload for all umbrellas at once
		byte[] fullColor1 = new byte[] {};
		for (int i = 0; i < numUmbrellas; i++) {
			fullColor1 = ArrayUtils.addAll(fullColor1, color1);
		}

		// payload for all umbrellas at once
		byte[] fullColor2 = new byte[] {};
		for (int i = 0; i < numUmbrellas; i++) {
			fullColor2 = ArrayUtils.addAll(fullColor2, color2);
		}

		for (;;) {
			byte[] colorData1 = fullColor1;
			byte[] colorData2 = fullColor2;
			System.out.println("Starting a new cycle through the umbrellas");

			// prepare the two ArtDmxPackets which will be sent in the loop
			// below
			ArtDmxPacket packetColor1 = buildPacket(artnetSubnet, artnetUniverse, colorData1, colorData1.length);
			ArtDmxPacket packetColor2 = buildPacket(artnetSubnet, artnetUniverse, colorData2, colorData2.length);

			for (int j = 0; j < 3; j++) {
				Thread.sleep(1000);
				System.out.println("Sending color1 -> " + artnetSubnet + ":" + artnetUniverse + " (dmxDataLength="
						+ packetColor1.getDmxData().length + ")");
				for (int i = 0; i < resends; i++) { // send multiple times
													// due to
					// possible packet loss
					artnet.unicastPacket(packetColor1, ip);
				}

				Thread.sleep(1000);
				System.out.println("Sending color2 -> " + artnetSubnet + ":" + artnetUniverse + " (dmxDataLength="
						+ packetColor2.getDmxData().length + ")");
				for (int i = 0; i < resends; i++) { // send multiple times
													// due to
					// possible packet loss
					artnet.unicastPacket(packetColor2, ip);
				}
			}
		}
	}

	private ArtDmxPacket buildPacket(int artnetSubnet, int artnetUniverse, byte[] data, int numChannels) {
		ArtDmxPacket artDmxPacket = new ArtDmxPacket();
		artDmxPacket.setUniverse(artnetSubnet, artnetUniverse);
		artDmxPacket.setSequenceID(0);
		artDmxPacket.setDMX(data, numChannels);

		return artDmxPacket;
	}

}
