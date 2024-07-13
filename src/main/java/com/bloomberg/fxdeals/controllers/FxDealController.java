package com.bloomberg.fxdeals.controllers;

import com.bloomberg.fxdeals.aspects.ToLog;
import com.bloomberg.fxdeals.model.FxDeal;
import com.bloomberg.fxdeals.services.FxDealService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deal")
public class FxDealController {
    FxDealService fxDealService;

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
    @GetMapping
    public @ResponseBody Iterable<FxDeal> getAllDeals() {
        return fxDealService.getAllDeals();
    }
}


