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
 * SynchronizedFactorizer
 *
 * <p>Servlet that caches last result, but with unnacceptably poor concurrency
 */
@ThreadSafe
public class SynchronizedFactorizer extends GenericServlet implements Servlet {
  @GuardedBy("this")
  private BigInteger lastNumber;

  @GuardedBy("this")
  private BigInteger[] lastFactors;

  @Override
  public synchronized void service(ServletRequest request, ServletResponse response)
      throws IOException {
    BigInteger i = extractFromRequest(request);
    if (i.equals(lastNumber)) {
      encodeIntoResponse(response, lastFactors);
    } else {
      BigInteger[] factors = factor(i);
      lastNumber = i;
      lastFactors = factors;
      encodeIntoResponse(response, factors);
    }
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
