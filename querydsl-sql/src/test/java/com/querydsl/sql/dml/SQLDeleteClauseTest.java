package com.querydsl.sql.dml;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.querydsl.sql.KeyAccessorsTest.QEmployee;
import com.querydsl.sql.SQLBindings;
import com.querydsl.sql.SQLTemplates;

public class SQLDeleteClauseTest {

    @Test(expected = IllegalArgumentException.class)
    @Ignore
    public void error() {
        QEmployee emp1 = new QEmployee("emp1");
        QEmployee emp2 = new QEmployee("emp2");
        SQLDeleteClause delete = new SQLDeleteClause(null, SQLTemplates.DEFAULT, emp1);
        delete.where(emp2.id.eq(1));
    }

    @Test
    public void getSQL() {
        QEmployee emp1 = new QEmployee("emp1");
        SQLDeleteClause delete = new SQLDeleteClause(null, SQLTemplates.DEFAULT, emp1);
        delete.where(emp1.id.eq(1));

        SQLBindings sql = delete.getSQL().get(0);
        assertEquals("delete from EMPLOYEE\nwhere EMPLOYEE.ID = ?", sql.getSQL());
        assertEquals(ImmutableList.of(1), sql.getBindings());
    }

}
