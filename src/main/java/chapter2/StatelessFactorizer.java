package chapter2;

import annotations.ThreadSafe;

import javax.servlet.*;
import java.io.IOException;
import java.math.BigInteger;

/**
 * StatelessFactorizer
 *
 * A stateless servlet
 */
@ThreadSafe
public class StatelessFactorizer extends GenericServlet implements Servlet {
    @Override
    public void service(ServletRequest request, ServletResponse response) throws IOException {
        BigInteger i = extractFromRequest(request);
        BigInteger[] factors = factor(i);
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
