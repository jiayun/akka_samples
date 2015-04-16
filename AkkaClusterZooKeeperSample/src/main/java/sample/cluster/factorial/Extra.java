package sample.cluster.factorial;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.cluster.routing.*;

import java.util.Arrays;
import java.util.Collections;

//not used, only for documentation
abstract class FactorialFrontend2 extends UntypedActor {
    //#router-lookup-in-code
    int totalInstances = 100;
    Iterable<String> routeesPaths = Arrays.asList("/user/factorialBackend", "");
    boolean allowLocalRoutees = true;
    String useRole = "backend";
    ActorRef backend = getContext().actorOf(
            new ClusterRouterGroup(new AdaptiveLoadBalancingGroup(
                    HeapMetricsSelector.getInstance(), Collections.<String>emptyList()),
                    new ClusterRouterGroupSettings(totalInstances, routeesPaths,
                            allowLocalRoutees, useRole)).props(), "factorialBackendRouter2");
    //#router-lookup-in-code
}

//not used, only for documentation
abstract class FactorialFrontend3 extends UntypedActor {
    //#router-deploy-in-code
    int totalInstances = 100;
    int maxInstancesPerNode = 3;
    boolean allowLocalRoutees = false;
    String useRole = "backend";
    ActorRef backend = getContext().actorOf(
            new ClusterRouterPool(new AdaptiveLoadBalancingPool(
                    SystemLoadAverageMetricsSelector.getInstance(), 0),
                    new ClusterRouterPoolSettings(totalInstances, maxInstancesPerNode,
                            allowLocalRoutees, useRole)).props(Props
                    .create(FactorialBackend.class)), "factorialBackendRouter3");
    //#router-deploy-in-code
}
