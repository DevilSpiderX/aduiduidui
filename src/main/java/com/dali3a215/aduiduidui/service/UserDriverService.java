package com.dali3a215.aduiduidui.service;

import com.dali3a215.aduiduidui.entity.Driver;
import io.vavr.Tuple2;

import java.util.List;

public interface UserDriverService {
    List<Tuple2<Driver, String>> getDriverByUid(String uid);
}
