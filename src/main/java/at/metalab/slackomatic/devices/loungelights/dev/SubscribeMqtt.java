package at.metalab.slackomatic.devices.loungelights.dev;

import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.Message;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

public class SubscribeMqtt {

	public static void main(String[] args) throws Exception {
		MQTT mqtt = new MQTT();
		mqtt.setHost("tcp://10.20.30.90:1883");

		BlockingConnection connection = mqtt.blockingConnection();
		connection.connect();

		Topic[] topics = { new Topic("lounge-light", QoS.AT_LEAST_ONCE) };
		byte[] qoses = connection.subscribe(topics);

		while (true) {
			Message message = connection.receive();

			System.out.println("received message: "
					+ message.getPayloadBuffer().ascii().toString());
			message.ack();
		}
	}

}
