/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.jpa.codegen;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.cfg.Configuration;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.querydsl.codegen.SerializerConfig;
import com.querydsl.codegen.SimpleSerializerConfig;
import com.querydsl.core.annotations.Config;
import com.querydsl.core.util.FileUtils;
import com.querydsl.jpa.domain.Domain;
import com.querydsl.jpa.domain2.Domain2;

public class HibernateDomainExporterTest {

    private final SerializerConfig serializerConfig = SimpleSerializerConfig.getConfig(Domain.class
            .getPackage().getAnnotation(Config.class));

    @Test
    public void execute_myEntity() throws IOException {
        FileUtils.delete(new File("target/gen6"));
        File myEntity = new File("src/test/resources/entity.hbm.xml");
        Configuration config = new Configuration();
        config.addFile(myEntity);
        HibernateDomainExporter exporter = new HibernateDomainExporter("Q",
                new File("target/gen6"), config);
        exporter.execute();

        File targetFile = new File("target/gen6/com/querydsl/jpa/codegen/QMyEntity.java");
        assertContains(targetFile, "StringPath pk1", "StringPath pk2", "StringPath prop1");

        CompileUtils.compile("target/gen6");
    }

    @Test
    public void execute_contact() throws IOException {
        FileUtils.delete(new File("target/gen1"));
        File contact = new File("src/test/resources/contact.hbm.xml");
        Configuration config = new Configuration();
        config.addFile(contact);
        HibernateDomainExporter exporter = new HibernateDomainExporter("Q",
                new File("target/gen1"), config);
        exporter.execute();

        File targetFile = new File("target/gen1/com/querydsl/jpa/domain2/QContact.java");
        assertContains(targetFile, "StringPath email", "StringPath firstName",
                "NumberPath<Long> id", "StringPath lastName");

        CompileUtils.compile("target/gen1");
    }

    @Test
    public void execute_contact_with_suffix() throws IOException {
        FileUtils.delete(new File("target/gen1"));
        File contact = new File("src/test/resources/contact.hbm.xml");
        Configuration config = new Configuration();
        config.addFile(contact);
        HibernateDomainExporter exporter = new HibernateDomainExporter("", "Type", new File(
                "target/gen1"), config);
        exporter.execute();

        File targetFile = new File("target/gen1/com/querydsl/jpa/domain2/ContactType.java");
        assertContains(targetFile, "StringPath email", "StringPath firstName",
                "NumberPath<Long> id", "StringPath lastName");

        CompileUtils.compile("target/gen1");
    }

    @Test
    public void execute_contact2() throws IOException {
        FileUtils.delete(new File("target/gen2"));
        File contact = new File("src/test/resources/contact2.hbm.xml");
        Configuration config = new Configuration();
        config.addFile(contact);
        HibernateDomainExporter exporter = new HibernateDomainExporter("Q",
                new File("target/gen2"), config);
        exporter.execute();

        File targetFile = new File("target/gen2/com/querydsl/jpa/domain2/QContact.java");
        assertContains(targetFile, "StringPath email", "StringPath firstName",
                "NumberPath<Long> id", "StringPath lastName");

        CompileUtils.compile("target/gen2");
    }

    @Test
    public void execute_multiple() throws IOException {
        FileUtils.delete(new File("target/gen3"));
        Configuration config = new Configuration();
        for (Class<?> cl : Domain.classes) {
            config.addAnnotatedClass(cl);
        }
        HibernateDomainExporter exporter = new HibernateDomainExporter("Q",
                new File("target/gen3"), serializerConfig, config);
        exporter.execute();

        List<String> failures = new ArrayList<String>();
        for (File file : new File("target/gen3/com/querydsl/jpa/domain").listFiles()) {
            String result1 = Files.toString(file, Charsets.UTF_8);
            String result2 = Files
                    .toString(
                            new File(
                                    "../querydsl-jpa/target/generated-test-sources/java/com/querydsl/jpa/domain",
                                    file.getName()), Charsets.UTF_8);
            if (!result1.equals(result2)) {
                System.err.println(file.getName());
                failures.add(file.getName());
            }
        }

        if (!failures.isEmpty()) {
            fail("Failed with " + failures.size() + " failures");
        }

        CompileUtils.compile("target/gen3");
    }

    @Test
    public void execute_multiple2() throws IOException {
        FileUtils.delete(new File("target/gen4"));
        Configuration config = new Configuration();
        for (Class<?> cl : Domain2.classes) {
            config.addAnnotatedClass(cl);
        }
        HibernateDomainExporter exporter = new HibernateDomainExporter("Q",
                new File("target/gen4"), serializerConfig, config);
        exporter.execute();

        List<String> failures = new ArrayList<String>();
        for (File file : new File("target/gen4/com/querydsl/jpa/domain2").listFiles()) {
            String result1 = Files.toString(file, Charsets.UTF_8);
            String result2 = Files
                    .toString(
                            new File(
                                    "../querydsl-jpa/target/generated-test-sources/java/com/querydsl/jpa/domain2",
                                    file.getName()), Charsets.UTF_8);
            if (!result1.equals(result2)) {
                System.err.println(file.getName());
                failures.add(file.getName());
            }
        }

        if (!failures.isEmpty()) {
            fail("Failed with " + failures.size() + " failures");
        }

        CompileUtils.compile("target/gen4");

    }

    @Test
    public void execute_store() throws IOException {
        FileUtils.delete(new File("target/gen5"));
        File contact = new File("src/test/resources/store.hbm.xml");
        Configuration config = new Configuration();
        config.addFile(contact);
        HibernateDomainExporter exporter = new HibernateDomainExporter("Q",
                new File("target/gen5"), config);
        exporter.execute();

        File targetFile = new File("target/gen5/com/querydsl/jpa/domain3/QStore.java");
        assertContains(targetFile, "StringPath code", "StringPath address");

        targetFile = new File("target/gen5/com/querydsl/jpa/domain3/QHardwareStore.java");
        assertContains(targetFile, "StringPath code = _super.code;", "StringPath address");

        CompileUtils.compile("target/gen5");
    }

    @Test
    public void execute_compositeKey() throws IOException {
       // See https://github.com/querydsl/querydsl/issues/1459

        FileUtils.delete(new File("target/gen18"));

        Configuration config = new Configuration();
        config.addFile(new File("src/test/resources/route1.hbm.xml"));
        config.addFile(new File("src/test/resources/route2.hbm.xml"));
        HibernateDomainExporter exporter = new HibernateDomainExporter("Q",
                new File("target/gen18"), serializerConfig, config);
        exporter.execute();

        CompileUtils.compile("target/gen18");
    }

    private static void assertContains(File file, String... strings) throws IOException {
        assertTrue(file.getPath() + " doesn't exist", file.exists());
        String result = Files.toString(file, Charsets.UTF_8);
        for (String str : strings) {
            assertTrue(str + " was not contained", result.contains(str));
        }
    }

}
