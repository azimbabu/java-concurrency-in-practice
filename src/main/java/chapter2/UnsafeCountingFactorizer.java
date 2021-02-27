package chapter2;

import annotations.NotThreadSafe;

import javax.servlet.*;
import java.io.IOException;
import java.math.BigInteger;

/**
 * UnsafeCountingFactorizer
 *
 * Servlet that counts requests without the necessary synchronization
 *
 */
@NotThreadSafe
public class UnsafeCountingFactorizer extends GenericServlet implements Servlet {
    private long count = 0;

    @Override
    public void service(ServletRequest request, ServletResponse response) throws IOException {
        BigInteger i = extractFromRequest(request);
        BigInteger[] factors = factor(i);
        ++count;
        encodeIntoResponse(response, factors);
    }

    private BigInteger[] factor(BigInteger i) {
        // Doesn't really factor
        return new BigInteger[] {i};
    }

    private void encodeIntoResponse(ServletResponse response, BigInteger[] factors) throws IOException {
        response.getWriter().println(factors);
    }

    private BigInteger extractFromRequest(ServletRequest request) {
        return new BigInteger(request.getParameter("number"));
    }
}
