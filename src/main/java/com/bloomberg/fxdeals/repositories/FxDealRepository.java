package com.bloomberg.fxdeals.repositories;

import com.bloomberg.fxdeals.aspects.ToLog;
import com.bloomberg.fxdeals.model.FxDeal;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;




@Repository
public interface FxDealRepository extends JpaRepository<FxDeal, String> {
/*
    I avoided using the JpaRepository.save(Entity e) method
    because it can perform either an insert or an update,
    depending on whether the entity already exists.
    The project requirements specify that duplicate
    requests should not be imported.
*/
    @ToLog
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "INSERT INTO fx_deal (id, amount, from_currency_iso, timestamp, to_currency_iso, deal_type) "
            + "VALUES (:#{#d.getId()}, :#{#d.getAmount()}, :#{#d.getFromCurrencyISO()}, :#{#d.getTimestamp()}," +
            " :#{#d.getToCurrencyISO()}," + " :#{#d.getDealType()})" , nativeQuery = true)
    void insert(@Param("d") FxDeal d);


}
