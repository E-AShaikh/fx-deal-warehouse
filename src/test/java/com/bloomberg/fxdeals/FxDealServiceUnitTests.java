package com.bloomberg.fxdeals;

import com.bloomberg.fxdeals.model.FxDeal;
import com.bloomberg.fxdeals.repositories.FxDealRepository;
import com.bloomberg.fxdeals.services.FxDealService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FxDealServiceUnitTests {
    @Mock
    private FxDealRepository fxDealRepository;

    @InjectMocks
    private FxDealService fxDealService;

    private FxDeal deal1;
    private FxDeal deal2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        deal1 = new FxDeal();
        deal1.setFromCurrencyISO("USD");
        deal1.setToCurrencyISO("EUR");
        deal1.setTimestamp(LocalDateTime.now());
        deal1.setAmount(new BigDecimal("100.00"));

        deal2 = new FxDeal();
        deal2.setFromCurrencyISO("GBP");
        deal2.setToCurrencyISO("JPY");
        deal2.setTimestamp(LocalDateTime.now().minusHours(1));
        deal2.setAmount(new BigDecimal("50.00"));
    }

//    @Test
//    public void testGetAllDeals() {
//        List<FxDeal> deals = Arrays.asList(deal1, deal2);
//        when(fxDealRepository.findAll()).thenReturn(deals);
//
//        Iterable<FxDeal> result = fxDealService.getAllDeals(0, 10);
//        assertNotNull(result);
//        assertEquals(deals, result);
//    }

    @Test
    public void testSaveFxDeal() {
        fxDealService.saveFxDeal(deal1);
        verify(fxDealRepository, times(1)).insert(deal1);
    }

    @Test
    public void testSaveFxDeals_allSavedSuccessfully() {
        List<FxDeal> deals = Arrays.asList(deal1, deal2);

        Map<FxDeal, String> results = fxDealService.saveFxDeals(deals);
        assertEquals(2, results.size());
        assertEquals("Saved", results.get(deal1));
        assertEquals("Saved", results.get(deal2));
        verify(fxDealRepository, times(1)).insert(deal1);
        verify(fxDealRepository, times(1)).insert(deal2);
    }

    @Test
    public void testSaveFxDeals_someSavedSuccessfully() {
        List<FxDeal> deals = Arrays.asList(deal1, deal2);

        doThrow(new DataIntegrityViolationException("")).when(fxDealRepository).insert(deal1);

        Map<FxDeal, String> results = fxDealService.saveFxDeals(deals);
        assertEquals(2, results.size());
        assertEquals("The request has already been imported!", results.get(deal1));
        assertEquals("Saved", results.get(deal2));
        verify(fxDealRepository, times(1)).insert(deal1);
        verify(fxDealRepository, times(1)).insert(deal2);
    }
}
