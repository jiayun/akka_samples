package org.akka.essentials.localnode;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import scala.concurrent.Await;
import scala.concurrent.Future;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.Patterns;
import scala.concurrent.duration.Duration;
import akka.util.Timeout;

public class LocalActor extends UntypedActor {
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	Timeout timeout = new Timeout(Duration.create(5, "seconds"));

	ActorRef remoteActor;

	@Override
	public void preStart() {
		//Get a reference to the remote actor
		remoteActor = getContext().actorFor(
				"akka.tcp://RemoteNodeApp@10.211.55.5:2552/user/remoteActor");
	}

	@Override
	public void onReceive(Object message) throws Exception {
		Future<Object> future = Patterns.ask(remoteActor, message.toString(),
				timeout);
		String result = (String) Await.result(future, timeout.duration());
		log.info("Message received from Server -> {}", result);
	}
}
