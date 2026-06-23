package despesaspessoais.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Order(1)
public class RateLimitFilter implements Filter {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    private Bucket createBucket() {
        return Bucket.builder()
                .addLimit(Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1))))
                .build();
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;

        if (request.getRequestURI().startsWith("/api/auth")) {
            String ip = request.getRemoteAddr();
            Bucket bucket = cache.computeIfAbsent(ip, k -> createBucket());

            if (!bucket.tryConsume(1)) {
                HttpServletResponse response = (HttpServletResponse) res;
                response.setStatus(429);
                response.setContentType("application/json");
                response.getWriter().write(
                        "{\"erro\": \"Muitas tentativas. Aguarde 1 minuto e tente novamente.\"}"
                );
                return;
            }
        }

        chain.doFilter(req, res);
    }
}