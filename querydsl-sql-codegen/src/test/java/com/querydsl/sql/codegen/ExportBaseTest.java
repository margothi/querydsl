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
package com.querydsl.sql.codegen;

import java.io.File;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.Test;

import com.querydsl.sql.Connections;

public abstract class ExportBaseTest {

    @Test
    public void export() throws SQLException {
        File folder = new File("target", getClass().getSimpleName());
        folder.mkdirs();
        NamingStrategy namingStrategy = new DefaultNamingStrategy();
        MetaDataExporter exporter = new MetaDataExporter();
        exporter.setSpatial(true);
        exporter.setSchemaPattern(getSchemaPattern());
        exporter.setPackageName("test");
        exporter.setTargetFolder(folder);
        exporter.setNamingStrategy(namingStrategy);
        exporter.export(Connections.getConnection().getMetaData());
    }

    protected String getSchemaPattern() {
        return null;
    }

    @AfterClass
    public static void tearDownAfterClass() throws SQLException {
        Connections.close();
    }

}
