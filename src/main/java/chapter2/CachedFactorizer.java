package chapter2;

import annotations.GuardedBy;
import annotations.ThreadSafe;

import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.math.BigInteger;

/**
 * CachedFactorizer
 *
 * <p>Servlet that caches its last request and result
 */
@ThreadSafe
public class CachedFactorizer extends GenericServlet implements Servlet {
  @GuardedBy("this")
  private BigInteger lastNumber;

  @GuardedBy("this")
  private BigInteger[] lastFactors;

  @GuardedBy("this")
  private long hits;

  @GuardedBy("this")
  private long cacheHits;

  @Override
  public void service(ServletRequest request, ServletResponse response) throws IOException {
    BigInteger i = extractFromRequest(request);
    BigInteger[] factors = null;
    synchronized (this) {
      ++hits;
      if (i.equals(lastNumber)) {
        ++cacheHits;
        factors = lastFactors.clone();
      }
    }

    if (factors == null) {
      factors = factor(i);
      synchronized (this) {
        lastNumber = i;
        lastFactors = factors;
      }
    }

    encodeIntoResponse(response, factors);
  }

  public synchronized long getHits() {
    return hits;
  }

  public synchronized double getCacheHitRatio() {
    return (double) cacheHits / (double) hits;
  }

  private BigInteger[] factor(BigInteger i) {
    // Doesn't really factor
    return new BigInteger[] {i};
  }

  private void encodeIntoResponse(ServletResponse response, BigInteger[] factors)
      throws IOException {
    response.getWriter().println(factors);
  }

  private BigInteger extractFromRequest(ServletRequest request) {
    return new BigInteger(request.getParameter("number"));
  }
}
