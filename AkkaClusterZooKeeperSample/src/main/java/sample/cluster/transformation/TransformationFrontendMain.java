package sample.cluster.transformation;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.ExtendedActorSystem;
import akka.actor.Props;
import akka.cluster.seed.ZookeeperClusterSeed;
import akka.dispatch.OnSuccess;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import sample.cluster.transformation.TransformationMessages.TransformationJob;
import scala.concurrent.ExecutionContext;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TransformationFrontendMain {

    public static void main(String[] args) {
        // Override the configuration of the port when specified as program argument
        final String hostname = args.length > 0 ? args[0] : "127.0.0.1";
        final String port = args.length > 1 ? args[1] : "0";

        final Config config = ConfigFactory.parseString("akka.remote.netty.tcp.hostname=" + hostname).
                withFallback(ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port)).
                withFallback(ConfigFactory.parseString("akka.cluster.roles = [frontend]")).
                withFallback(ConfigFactory.load());

        ActorSystem system = ActorSystem.create("ClusterSystem", config);
        new ZookeeperClusterSeed((ExtendedActorSystem)system).join();

        final ActorRef frontend = system.actorOf(
                Props.create(TransformationFrontend.class), "frontend");
        final FiniteDuration interval = Duration.create(2, TimeUnit.SECONDS);
        final Timeout timeout = new Timeout(Duration.create(5, TimeUnit.SECONDS));
        final ExecutionContext ec = system.dispatcher();
        final AtomicInteger counter = new AtomicInteger();
        system.scheduler().schedule(interval, interval, new Runnable() {
            public void run() {
                Patterns.ask(frontend,
                        new TransformationJob("hello-" + counter.incrementAndGet()),
                        timeout).onSuccess(new OnSuccess<Object>() {
                    public void onSuccess(Object result) {
                        System.out.println(result);
                    }
                }, ec);
            }

        }, ec);

    }
}
