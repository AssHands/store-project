package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.repository.*;
import com.ak.store.catalogue.util.CatalogueMapper;
import com.ak.store.common.model.catalogue.view.CharacteristicView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;


//    Session session = entityManager.unwrap(Session.class);
//    SessionFactory sessionFactory = session.getSessionFactory();
//    sessionFactory.getCache();
//    Session session = entityManager.unwrap(Session.class);
//    Statistics statistics = session.getSessionFactory().getStatistics();
//    CacheRegionStatistics cacheStatistics = statistics.getDomainDataRegionStatistics("static-data");
@Service
@RequiredArgsConstructor
public class CharacteristicService {
    private final CatalogueMapper catalogueMapper;
    private final CharacteristicRepo characteristicRepo;

    public List<Characteristic> findAllCharacteristicByCategoryId(Long categoryId) {
        return characteristicRepo.findAllWithTextValuesByCategoryId(categoryId);
    }

    public Characteristic findOne(Long id) {
        return characteristicRepo.findById(id).orElseThrow(() -> new RuntimeException("no characteristic found"));
    }
}