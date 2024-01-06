package pl.edu.pw.odasprojekt.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pw.odasprojekt.model.domain.EventType;
import pl.edu.pw.odasprojekt.model.domain.LogEvent;
import pl.edu.pw.odasprojekt.model.domain.UserData;
import pl.edu.pw.odasprojekt.model.dtos.LogDto;
import pl.edu.pw.odasprojekt.repositories.LogRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LogService {
    private final LogRepository logRepository;

    @Autowired
    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public List<LogDto> getClientLogs(String clientNumber) {
        var logs = logRepository.findAllByUserClientNumber(clientNumber);
        var logDtos = new ArrayList<LogDto>();

        logs.forEach(log -> logDtos.add(convertToDto(log)));

        return logDtos;
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

    private LogDto convertToDto(LogEvent log) {
        var dto = LogDto.builder().address(log.getAddress()).createdAt(log.getCreatedAt());

        switch (log.getEvent()) {
            case LoginFailure -> dto.details("Próba logowania zakończona niepowodzeniem.");
            case LoginSuccess -> dto.details("Próba logowania zakończona sukcesem.");
            case PasswordReset -> dto.details("Zmiana hasła.");
            case ResetRequest -> dto.details("Prośba o zresetowanie hasła.");
        }

        return dto.build();
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
