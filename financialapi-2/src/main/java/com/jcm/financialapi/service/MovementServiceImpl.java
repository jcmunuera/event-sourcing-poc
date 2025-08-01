package com.jcm.financialapi.service;

import com.jcm.financialcontract.dto.Movement;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovementServiceImpl implements MovementService {

    @Override
    public List<Movement> getMovements(String accountId, String from, String to) {
        System.out.println( "MovementService: getMovements() - accountId: " + accountId);
        // Generate dummy movements
        List<Movement> movements = Arrays.asList(
                new Movement("1", "Received transfer", 500.00, LocalDate.of(2025, 2, 1)),
                new Movement("2", "Credit card payment", -150.00, LocalDate.of(2025, 2, 10)),
                new Movement("3", "Cash deposit", 200.00, LocalDate.of(2025, 2, 15)),
                new Movement("4", "Supermarket purchase", -75.00, LocalDate.of(2025, 2, 20))
        );

        // Parse date filters
        LocalDate fromDate = from != null ? LocalDate.parse(from) : LocalDate.of(2000, 1, 1);
        LocalDate toDate = to != null ? LocalDate.parse(to) : LocalDate.of(2100, 12, 31);

        // Filter movements by date range
        return movements.stream()
                .filter(m -> !m.getDate().isBefore(fromDate) && !m.getDate().isAfter(toDate))
                .collect(Collectors.toList());
    }
}
