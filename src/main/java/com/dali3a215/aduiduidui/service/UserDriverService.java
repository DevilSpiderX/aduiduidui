package com.dali3a215.aduiduidui.service;

import com.dali3a215.aduiduidui.entity.Driver;
import com.dali3a215.aduiduidui.entity.User;
import io.vavr.Tuple2;

import java.util.List;

public interface UserDriverService {
    List<Tuple2<Driver, String>> getDriverByUid(String uid);

    List<Tuple2<User, String>> getUserByDriveId(int driveId);

    boolean allocateSpace(String uid, int driverID, String physicalPath);

    void checkAllSpace();

    void checkUserSpace(String uid);
}
