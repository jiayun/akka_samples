package sample.cluster.factorial;

import akka.actor.UntypedActor;
import akka.dispatch.Futures;
import akka.dispatch.Mapper;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.Patterns;
import scala.concurrent.Future;

import java.math.BigInteger;
import java.util.concurrent.Callable;

//#backend
public class FactorialBackend extends UntypedActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) {
        if (message instanceof Integer) {
            final Integer n = (Integer) message;

            log.info("Got message {}", n);

            Future<BigInteger> f = Futures.future(new Callable<BigInteger>() {
                public BigInteger call() {
                    return factorial(n);
                }
            }, getContext().dispatcher());

            Future<FactorialResult> result = f.map(
                    new Mapper<BigInteger, FactorialResult>() {
                        public FactorialResult apply(BigInteger factorial) {
                            return new FactorialResult(n, factorial);
                        }
                    }, getContext().dispatcher());

            Patterns.pipe(result, getContext().dispatcher()).to(getSender());

        } else {
            unhandled(message);
        }
    }

    BigInteger factorial(int n) {
        BigInteger acc = BigInteger.ONE;
        for (int i = 1; i <= n; ++i) {
            acc = acc.multiply(BigInteger.valueOf(i));
        }
        return acc;
    }
}
//#backend

