package sample.cluster.factorial;

import java.io.Serializable;
import java.math.BigInteger;

public class FactorialResult implements Serializable {
    public final int n;
    public final BigInteger factorial;

    FactorialResult(int n, BigInteger factorial) {
        this.n = n;
        this.factorial = factorial;
    }
}