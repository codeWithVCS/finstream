package com.vcs.finstream.controller;

import com.vcs.finstream.model.AnalyticsResponse;
import com.vcs.finstream.model.Transaction;
import com.vcs.finstream.service.CsvParsingService;
import com.vcs.finstream.service.TransactionAnalyticsService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final CsvParsingService csvParsingService;
    private final TransactionAnalyticsService transactionAnalyticsService;

    public AnalyticsController(CsvParsingService csvParsingService,
                               TransactionAnalyticsService transactionAnalyticsService) {
        this.csvParsingService = csvParsingService;
        this.transactionAnalyticsService = transactionAnalyticsService;
    }

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AnalyticsResponse> uploadAndAnalyze(
            @RequestParam("file") MultipartFile file) {

        List<Transaction> transactions = csvParsingService.parse(file);

        AnalyticsResponse response =
                transactionAnalyticsService.analyze(transactions);

        return ResponseEntity.ok(response);
    }
}