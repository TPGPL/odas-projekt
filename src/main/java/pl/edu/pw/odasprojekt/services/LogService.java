package pl.edu.pw.odasprojekt.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pw.odasprojekt.model.domain.EventType;
import pl.edu.pw.odasprojekt.model.domain.LogEvent;
import pl.edu.pw.odasprojekt.model.domain.UserData;
import pl.edu.pw.odasprojekt.repositories.LogRepository;

import java.util.Date;

@Service
public class LogService {
    private final LogRepository logRepository;

    @Autowired
    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public Iterable<LogEvent> getClientLogs(String clientNumber) {
        return logRepository.findAllByUserClientNumber(clientNumber);
    }

    public void createUserLog(HttpServletRequest request, EventType type, UserData user) {
        var ip = extractIpAddress(request);
        var log = LogEvent.builder()
                .address(ip)
                .event(type)
                .user(user)
                .build();

        createLog(log);
    }

    public void createHoneypotLog(HttpServletRequest request, String path) {
        var ip = extractIpAddress(request);
        var log = LogEvent.builder()
                .address(ip)
                .details(path)
                .event(EventType.HoneypotCapture)
                .build();

        createLog(log);
    }

    private void createLog(LogEvent event) {
        var currentDate = new Date();

        event.setCreatedAt(currentDate);
        logRepository.save(event);
    }

    private String extractIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");

        if (xForwardedForHeader != null && !xForwardedForHeader.isEmpty()) {
            return xForwardedForHeader.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }
}
