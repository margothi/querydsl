package com.querydsl.sql;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.JoinType;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.testutil.Performance;
import com.querydsl.sql.domain.QSurvey;

@Category(Performance.class)
public class PerformanceTest {

    private QueryMetadata md;

    private SQLTemplates templates;

    private Configuration configuration;

    int iterations;

    @Before
    public void setUp() {
        QSurvey survey = QSurvey.survey;
        md = new DefaultQueryMetadata();
        md.addJoin(JoinType.DEFAULT, survey);
        md.addWhere(survey.id.eq(10));
        md.setProjection(survey.name);

        templates = new H2Templates();
        configuration = new Configuration(templates);

        iterations =  1000000;
    }

    @Test
    public void nonNormalized() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            SQLSerializer serializer = new SQLSerializer(configuration);
            serializer.setNormalize(false);
            serializer.serialize(md, false);
            serializer.getConstants();
            serializer.getConstantPaths();
            assertNotNull(serializer.toString());
        }
        System.err.println("non-normalized " + (System.currentTimeMillis() - start));
    }

    @Test
    public void default_() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            SQLSerializer serializer = new SQLSerializer(configuration);
            serializer.serialize(md, false);
            serializer.getConstants();
            serializer.getConstantPaths();
            assertNotNull(serializer.toString());
        }
        System.err.println("default " + (System.currentTimeMillis() - start));
    }

}
