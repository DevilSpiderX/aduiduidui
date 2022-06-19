package com.dali3a215.aduiduidui.service.impl;

import com.dali3a215.aduiduidui.entity.SystemConfig;
import com.dali3a215.aduiduidui.service.SystemConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.teasoft.bee.osql.IncludeType;
import org.teasoft.bee.osql.SuidRich;
import org.teasoft.honey.osql.core.BeeFactoryHelper;

import javax.servlet.http.HttpSession;
import java.util.LinkedList;
import java.util.List;

@Service("systemConfigService")
public class SystemConfigServiceImpl implements SystemConfigService {
    private final static Logger logger = LoggerFactory.getLogger(SystemConfigServiceImpl.class);
    private final SuidRich dao = BeeFactoryHelper.getSuidRich();
    private final List<String> baseKeys = List.of("adminName", "adminUid", "adminPassword", "searchCacheKeepTime");
    private final List<String> baseValues = List.of("admin", "admin", "123456", String.valueOf(24 * 60 * 60 * 1000));
    private final List<String> baseRemarks = List.of("管理员名", "管理员账号", "管理员密码", "搜索缓存保留时间");

    @Override
    public void init() {
        List<SystemConfig> insertList = new LinkedList<>();
        for (int i = 0; i < baseKeys.size(); i++) {
            String key = baseKeys.get(i);
            String value = baseValues.get(i);
            String remark = baseRemarks.get(i);
            SystemConfig config = new SystemConfig();
            config.setId(i + 1);
            config.setKey(key);
            if (!dao.exist(config)) {
                config.setValue(value);
                config.setRemark(remark);
                insertList.add(config);
            }
        }
        if (insertList.isEmpty()) {
            logger.info("系统参数无需初始化");
        } else {
            int n = dao.insert(insertList);
            logger.info("已初始化{}个系统参数", n);
        }
    }

    @Override
    public String getValue(String key) {
        SystemConfig qConfig = new SystemConfig();
        qConfig.setKey(key);
        List<SystemConfig> list = dao.select(qConfig);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0).getValue();
    }

    @Override
    public void setValue(String key, String value, String remark) {
        SystemConfig config = new SystemConfig();
        config.setKey(key);
        if (dao.exist(config)) {
            config.setValue(value);
            config.setRemark(remark);
            dao.updateBy(config, "key", IncludeType.INCLUDE_EMPTY);
        } else {
            config.setValue(value);
            config.setRemark(remark);
            dao.insert(config, IncludeType.INCLUDE_EMPTY);
        }
    }

    @Override
    public String getAdminUid() {
        return getValue("adminUid");
    }

    @Override
    public void setAdminUid(String uid) {
        setValue("adminUid", uid, null);
    }

    @Override
    public String getAdminPassword() {
        return getValue("adminPassword");
    }

    @Override
    public void setAdminPassword(String password) {
        setValue("adminPassword", password, null);
    }

    @Override
    public String getAdminName() {
        return getValue("adminName");
    }

    @Override
    public void setAdminName(String name) {
        setValue("adminName", name, null);
    }

    @Override
    public boolean isAdmin(HttpSession session) {
        return session.getAttribute("admin") != null && (Boolean) session.getAttribute("admin");
    }

    @Override
    public long getSearchCacheKeepTime() {
        return Long.parseLong(getValue("searchCacheKeepTime"));
    }

    @Override
    public void setSearchCacheKeepTime(long keepTime) {
        setValue("searchCacheKeepTime", String.valueOf(keepTime), null);
    }


}
