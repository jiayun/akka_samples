package org.akka.essentials.remotenode;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.kernel.Bootable;
import com.typesafe.config.ConfigFactory;

public class RemoteNodeApplicationMain {

	public static void main(String[] args) {

		final ActorSystem system = ActorSystem.create("RemoteNodeApp", ConfigFactory
				.load().getConfig("RemoteSys"));

		system.actorOf(Props.create(RemoteActor.class), "remoteActor");

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				system.shutdown();
			}
		});
	}
}
