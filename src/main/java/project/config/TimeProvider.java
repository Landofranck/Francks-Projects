package project.config;

import jakarta.enterprise.context.ApplicationScoped;

import java.time.Clock;

@ApplicationScoped
public class TimeProvider {
    public Clock clock() {
        return Clock.systemUTC();
    }
}
