package com.dali3a215.aduiduidui.bee;

import org.springframework.stereotype.Component;
import org.teasoft.honey.osql.name.NameRegistry;
import org.teasoft.honey.osql.name.UnderScoreAndCamelName;

@Component
public class MySQLKeywordTranslate extends UnderScoreAndCamelName {

    public MySQLKeywordTranslate() {
        NameRegistry.registerNameTranslate(this);
    }

    @Override
    public String toColumnName(String fieldName) {
        return '`' + super.toColumnName(fieldName) + '`';
    }
}
