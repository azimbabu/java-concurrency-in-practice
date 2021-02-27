package chapter2;

import annotations.NotThreadSafe;

import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * UnsafeCachingFactorizer
 *
 * <p>Servlet that attempts to cache its last result without adequate atomicity
 */
@NotThreadSafe
public class UnsafeCachingFactorizer extends GenericServlet implements Servlet {
  private final AtomicReference<BigInteger> lastNumber = new AtomicReference<>();
  private final AtomicReference<BigInteger[]> lastFactors = new AtomicReference<BigInteger[]>();

  @Override
  public void service(ServletRequest request, ServletResponse response) throws IOException {
    BigInteger i = extractFromRequest(request);
    if (i.equals(lastNumber.get())) {
      encodeIntoResponse(response, lastFactors.get());
    } else {
      BigInteger[] factors = factor(i);
      lastNumber.set(i);
      lastFactors.set(factors);
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
