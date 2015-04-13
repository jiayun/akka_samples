package org.akka.essentials.remotenode;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class RemoteActor extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
			// Get reference to the message sender and reply back
			log.info("Message received -> {}", message);
			getSender().tell(message + " got something", null);
		}
	}
}
