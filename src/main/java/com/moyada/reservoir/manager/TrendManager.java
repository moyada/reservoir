package com.moyada.reservoir.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Component
public class TrendManager {

    private static final int calInterval = 6 * 2;
    private static final long cacheTime = TimeUnit.HOURS.toMillis(1L);

    @Autowired
    private CacheService cacheService;

    public double getFirstRecord(Integer id) {
        String key = id.toString();
        int size = cacheService.size(key);
        if (size < calInterval) {
            return 0D;
        }
        Double val = cacheService.getFirst(key, Double.class);
        if (val == null) {
            return 0D;
        }
        return val;
    }

    public void record(Integer id, Double value) {
        cacheService.add(id.toString(), value, cacheTime);
    }
}
