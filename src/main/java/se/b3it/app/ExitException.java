package se.b3it.app;

import org.springframework.boot.ExitCodeGenerator;

class ExitException extends RuntimeException implements ExitCodeGenerator {

    @Override
    public int getExitCode() {
        return 10;
    }

}
