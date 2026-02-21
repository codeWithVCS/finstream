package com.vcs.finstream.service;

import com.vcs.finstream.model.Category;
import com.vcs.finstream.model.Transaction;
import com.vcs.finstream.model.TransactionType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CsvParsingService {

    public List<Transaction> parse(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Uploaded file is empty or null");
        }

        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            return reader.lines()
                    .skip(1) // skip header
                    .map(this::mapToTransaction)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file", e);
        }
    }

    private Transaction mapToTransaction(String line) {

        String[] parts = line.split(",");

        if (parts.length != 6) {
            throw new RuntimeException("Invalid CSV format: " + line);
        }

        String transactionId = parts[0].trim();
        String userId = parts[1].trim();
        BigDecimal amount = new BigDecimal(parts[2].trim());
        TransactionType type = TransactionType.valueOf(parts[3].trim().toUpperCase());
        Category category = Category.valueOf(parts[4].trim().toUpperCase());
        LocalDateTime timestamp = LocalDateTime.parse(parts[5].trim());

        return new Transaction(
                transactionId,
                userId,
                amount,
                type,
                category,
                timestamp
        );
    }
}