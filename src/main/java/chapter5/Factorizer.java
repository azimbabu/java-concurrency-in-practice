package chapter5;

import javax.servlet.*;
import java.io.IOException;
import java.math.BigInteger;

public class Factorizer extends GenericServlet implements Servlet {
  private final Computable<BigInteger, BigInteger[]> computable = arg -> factor(arg);
  private final Computable<BigInteger, BigInteger[]> cache = new Memoizer<>(computable);

  private BigInteger[] factor(BigInteger i) {
    // Doesn't really factor
    return new BigInteger[] {i};
  }

  @Override
  public void service(ServletRequest request, ServletResponse response)
      throws ServletException, IOException {
    try {
      BigInteger i = extractFromRequest(request);
      encodeIntoResponse(response, cache.compute(i));
    } catch (InterruptedException ex) {
      encodeError(response, "factorization interrupted");
    }
  }

  private void encodeIntoResponse(ServletResponse response, BigInteger[] factors)
      throws IOException {
    response.getWriter().println(factors);
  }

  void encodeError(ServletResponse response, String error) throws IOException {
    response.getWriter().println(error);
  }

  private BigInteger extractFromRequest(ServletRequest request) {
    return new BigInteger(request.getParameter("number"));
  }
}
