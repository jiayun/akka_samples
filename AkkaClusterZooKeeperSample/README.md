The content of this folder is derived from the sample of Akka.

https://github.com/akka/akka/tree/master/akka-samples/akka-sample-cluster-java

# Run

java -cp .:./* sample.cluster.transformation.TransformationBackendMain

java -cp .:./* sample.cluster.transformation.TransformationFrontendMain

java -cp .:./* sample.cluster.transformation.TransformationBackendMain 10.211.55.5 2552

java -cp .:./* sample.cluster.transformation.TransformationBackendMain 10.211.55.6 2552

java -cp .:./* sample.cluster.transformation.TransformationFrontendMain 10.211.55.2 2552
