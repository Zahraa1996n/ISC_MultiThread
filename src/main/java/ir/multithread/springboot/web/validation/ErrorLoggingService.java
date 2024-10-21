package ir.multithread.springboot.web.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ErrorLoggingService {

    private final List<ErrorRecord> errorRecords = new ArrayList<>();

    public void logError(String fileName, String errorCode, String errorDescription) {
        ErrorRecord errorRecord = new ErrorRecord(fileName, errorCode, errorDescription, LocalDateTime.now());
        errorRecords.add(errorRecord);
    }

    public void writeErrorsToJson(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); 
        objectMapper.writeValue(new File(filePath), errorRecords);
    }

    public static class ErrorRecord {
        private String fileName;
        private int recordNumber;
        private String errorCode;
        private String errorDescription;
        private LocalDateTime errorDate;

        public ErrorRecord(String fileName, String errorCode, String errorDescription, LocalDateTime errorDate) {
            this.fileName = fileName;
            this.errorCode = errorCode;
            this.errorDescription = errorDescription;
            this.errorDate = errorDate;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public int getRecordNumber() {
            return recordNumber;
        }

        public void setRecordNumber(int recordNumber) {
            this.recordNumber = recordNumber;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

        public String getErrorDescription() {
            return errorDescription;
        }

        public void setErrorDescription(String errorDescription) {
            this.errorDescription = errorDescription;
        }

        public LocalDateTime getErrorDate() {
            return errorDate;
        }

        public void setErrorDate(LocalDateTime errorDate) {
            this.errorDate = errorDate;
        }
    }
}

