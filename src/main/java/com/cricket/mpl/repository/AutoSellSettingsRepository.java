package com.cricket.mpl.repository;

import com.cricket.mpl.entity.Auction;
import com.cricket.mpl.entity.AutoSaleSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AutoSellSettingsRepository extends JpaRepository<AutoSaleSettings,String> {


}
