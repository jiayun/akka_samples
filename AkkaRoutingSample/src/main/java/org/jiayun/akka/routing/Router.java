package org.jiayun.akka.routing;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Address;
import akka.actor.Props;
import akka.remote.routing.RemoteRouterConfig;
import akka.routing.RoundRobinPool;
import com.typesafe.config.ConfigFactory;
import org.akka.essentials.remotenode.RemoteActor;

public class Router {

    public static void main(String[] args) throws InterruptedException {
        ActorSystem _system = ActorSystem.create("RemoteRouteeRouterExample", ConfigFactory.load().getConfig("MyRouterExample"));

        Address[] addresses = new Address[]{
                new Address("akka.tcp", "RemoteNodeApp", "10.211.55.6", 2552),
                new Address("akka.tcp", "RemoteNodeApp", "10.211.55.5", 2552)
        };

        ActorRef randomRouter = _system.actorOf(new RemoteRouterConfig(new RoundRobinPool(5), addresses).props(
                Props.create(RemoteActor.class)));

        for (int i = 1; i <= 10; i++) {
            // sends randomly to actors
            randomRouter.tell("Hello " + Integer.toString(i), null);
        }
        _system.shutdown();
    }

}
