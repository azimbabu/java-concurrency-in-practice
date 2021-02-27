package chapter3;

import annotations.Immutable;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * OneValueCache
 * <p/>
 * Immutable holder for caching a number and its factors
 *
 */
@Immutable
public class OneValueCache {
    private final BigInteger lastNumber;
    private final BigInteger[] lastFactors;

    public OneValueCache(BigInteger number, BigInteger[] factors) {
        this.lastNumber = number;
        this.lastFactors = factors != null ? Arrays.copyOf(factors, factors.length) : null;
    }

    public BigInteger[] getFactors(BigInteger number) {
        if (lastNumber == null || !lastNumber.equals(number)) {
            return null;
        } else {
            return Arrays.copyOf(lastFactors, lastFactors.length);
        }
    }
}
