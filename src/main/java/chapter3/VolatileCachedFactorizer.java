package chapter3;

import annotations.ThreadSafe;

import javax.servlet.*;
import java.io.IOException;
import java.math.BigInteger;

/**
 * VolatileCachedFactorizer
 * <p/>
 * Caching the last result using a volatile reference to an immutable holder object
 *
 */
@ThreadSafe
public class VolatileCachedFactorizer extends GenericServlet implements Servlet {
    private volatile OneValueCache cache = new OneValueCache(null, null);
    @Override
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        BigInteger number = extractFromRequest(request);
        BigInteger[] factors = cache.getFactors(number);
        if (factors == null) {
            factors = factor(number);
            cache = new OneValueCache(number, factors);
        }
        encodeIntoResponse(response, factors);
    }

    private void encodeIntoResponse(ServletResponse response, BigInteger[] factors) throws IOException {
        response.getWriter().println(factors);
    }

    private BigInteger extractFromRequest(ServletRequest request) {
        return new BigInteger(request.getParameter("number"));
    }

    private BigInteger[] factor(BigInteger i) {
        // Doesn't really factor
        return new BigInteger[] {i};
    }
}
