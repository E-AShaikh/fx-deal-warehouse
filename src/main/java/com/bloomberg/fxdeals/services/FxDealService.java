package com.bloomberg.fxdeals.services;


import com.bloomberg.fxdeals.aspects.ToLog;
import com.bloomberg.fxdeals.model.FxDeal;
import com.bloomberg.fxdeals.repositories.FxDealRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FxDealService {
    private FxDealRepository fxDealRepository;

    public FxDealService(FxDealRepository fxDealRepository) {
        this.fxDealRepository = fxDealRepository;
    }

    @ToLog
    public Iterable<FxDeal> getAllDeals() {
        return fxDealRepository.findAll();
    }

    @ToLog
    public void saveFxDeal(FxDeal deal) {
        fxDealRepository.insert(deal);
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
    public Map<FxDeal, String> saveFxDeals(List<FxDeal> deals) {
        Map<FxDeal, String> results = new HashMap<>();

        for (FxDeal deal : deals) {
            try {
                fxDealRepository.insert(deal);
                results.put(deal, "Saved");
            } catch (DataIntegrityViolationException e) {
                results.put(deal, "The request has already been imported!");
            }
        }
        return results;
    }
}
