package com.bloomberg.fxdeals.controllers;

import com.bloomberg.fxdeals.aspects.ToLog;
import com.bloomberg.fxdeals.model.FxDeal;
import com.bloomberg.fxdeals.services.FxDealService;
import com.bloomberg.fxdeals.validation.ValidSortField;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/deal")
public class FxDealController {
    private final FxDealService fxDealService;

    public FxDealController(FxDealService fxDealService) {
        this.fxDealService = fxDealService;
    }

    @ToLog
    @PostMapping("/add")
    public ResponseEntity<String> addFxDeal(@Valid @RequestBody FxDeal deal) {
        fxDealService.saveFxDeal(deal);
        return new ResponseEntity<>("Saved", HttpStatus.CREATED);
    }
    /*
    I implemented this method based on the requirement that
    "No rollback allowed, every row imported should be saved in the DB."
    Since the default behavior is to not rollback as long as @Transactional is not used,
    I interpreted this requirement to mean that in a batch save,
    even if one FX deal fails certain criteria, the others should still be saved،
    and thus handle each record individually.
    */
    @ToLog
    @PostMapping("/addDeals")
    public ResponseEntity<Map<FxDeal, String>>  addFxDeals(@Valid @RequestBody List<FxDeal> deals) {
        Map<FxDeal, String> results = fxDealService.saveFxDeals(deals);
        // Check if there are any failures in the results
        boolean hasFailures = results.values().stream().anyMatch(msg -> msg.contains("imported!"));

        // Return 207 Multi-Status if there are failures, otherwise 200 OK
        HttpStatus status = hasFailures ? HttpStatus.MULTI_STATUS : HttpStatus.OK;
        return new ResponseEntity<>(results, status);
    }

    @ToLog
    @GetMapping("/all")
    public @ResponseBody Page<FxDeal> getAllDeals(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                                  @RequestParam(defaultValue = "10") @PositiveOrZero int size,
                                                  @RequestParam(defaultValue = "id") @ValidSortField(entityClass = FxDeal.class) String sortedBy) {
        Page<FxDeal> deals = fxDealService.getAllDeals(page, size, sortedBy);
        if (deals.getTotalElements() == 0) {
            log.info("Empty page has been returned");
        }
        return deals;
    }

    @ToLog
    @GetMapping("/amount")
    public @ResponseBody Page<FxDeal> getDealsByAmount(@RequestParam BigDecimal amount,
                                                       @RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                                       @RequestParam(defaultValue = "10") @PositiveOrZero int size,
                                                       @RequestParam(defaultValue = "id") @ValidSortField(entityClass = FxDeal.class) String sortedBy) {
        Page<FxDeal> deals = fxDealService.getDealsByAmount(amount, page, size, sortedBy);
        if (deals.getTotalElements() == 0) {
            log.info("Empty page has been returned");
        }
        return deals;
    }

    @ToLog
    @GetMapping("/type")
    public @ResponseBody Page<FxDeal> getDealsByDealType(@RequestParam String dealType,
                                                       @RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                                       @RequestParam(defaultValue = "10") @PositiveOrZero int size,
                                                       @RequestParam(defaultValue = "id") @ValidSortField(entityClass = FxDeal.class) String sortedBy) {
        Page<FxDeal> deals = fxDealService.getDealsByDealType(dealType, page, size, sortedBy);
        if (deals.getTotalElements() == 0) {
            log.info("Empty page has been returned");
        }
        return deals;
    }

}


