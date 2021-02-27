package chapter2;

import annotations.ThreadSafe;

import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * CountingFactorizer
 *
 * Servlet that counts requests using AtomicLong
 *
 */
@ThreadSafe
public class CountingFactorizer extends GenericServlet implements Servlet {
    private AtomicLong count = new AtomicLong(0);

    @Override
    public void service(ServletRequest request, ServletResponse response) throws IOException {
        BigInteger i = extractFromRequest(request);
        BigInteger[] factors = factor(i);
        count.incrementAndGet();
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
