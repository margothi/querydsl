package com.querydsl.sql;

import static com.querydsl.sql.Constants.employee;
import static com.querydsl.sql.RelationalPathExtractor.extract;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.sql.domain.QEmployee;

public class RelationalPathExtractorTest {

    private SQLQuery<?> query() {
        return new SQLQuery<Void>();
    }

    @Test
    public void simpleQuery() {
        QEmployee employee2 = new QEmployee("employee2");
        SQLQuery<?> query = query().from(employee, employee2);

        assertEquals(ImmutableSet.of(employee, employee2), extract(query.getMetadata()));
    }

    @Test
    public void joins() {
        QEmployee employee2 = new QEmployee("employee2");
        SQLQuery<?> query = query().from(employee)
                .innerJoin(employee2).on(employee.superiorId.eq(employee2.id));

        assertEquals(ImmutableSet.of(employee, employee2), extract(query.getMetadata()));
    }

    @Test
    public void subQuery() {
        SQLQuery<?> query = query().from(employee)
                .where(employee.id.eq(query().from(employee).select(employee.id.max())));
        assertEquals(ImmutableSet.of(employee), extract(query.getMetadata()));
    }

    @Test
    public void subQuery2() {
        QEmployee employee2 = new QEmployee("employee2");
        SQLQuery<?> query = query().from(employee)
            .where(Expressions.list(employee.id, employee.lastname)
                .in(query().from(employee2).select(employee2.id, employee2.lastname)));

        assertEquals(ImmutableSet.of(employee, employee2), extract(query.getMetadata()));
    }

}
