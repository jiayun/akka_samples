package sample.cluster.transformation;

import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class TransformationBackendMain {

    public static void main(String[] args) {
        // Override the configuration of the port when specified as program argument
        final String hostname = args.length > 0 ? args[0] : "127.0.0.1";
        final String port = args.length > 1 ? args[1] : "0";

        final Config config = ConfigFactory.parseString("akka.remote.netty.tcp.hostname=" + hostname).
                withFallback(ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port)).
                withFallback(ConfigFactory.parseString("akka.cluster.roles = [backend]")).
                withFallback(ConfigFactory.load());

        ActorSystem system = ActorSystem.create("ClusterSystem", config);

        system.actorOf(Props.create(TransformationBackend.class), "backend");

    }

}
