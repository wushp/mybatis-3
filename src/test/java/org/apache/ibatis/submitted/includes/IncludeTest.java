/**
 * Copyright 2009-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.ibatis.submitted.includes;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.Reader;
import java.sql.Connection;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;

public class IncludeTest {

    private static SqlSessionFactory sqlSessionFactory;

    @BeforeClass
    public static void setUp() throws Exception {
        // create a SqlSessionFactory
        Reader reader = Resources.getResourceAsReader("org/apache/ibatis/submitted/includes/MapperConfig.xml");
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        reader.close();

        // populate in-memory database
        SqlSession session = sqlSessionFactory.openSession();
        Connection conn = session.getConnection();
        reader = Resources.getResourceAsReader("org/apache/ibatis/submitted/includes/CreateDB.sql");
        ScriptRunner runner = new ScriptRunner(conn);
        runner.setLogWriter(null);
        runner.runScript(reader);
        reader.close();
        session.close();
    }

    @Test
    public void testIncludes() throws Exception {
        final SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            final Integer result =
                    sqlSession.selectOne("org.apache.ibatis.submitted.includes.mapper.selectWithProperty");
            Assert.assertEquals(Integer.valueOf(1), result);
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testParametrizedIncludes() throws Exception {
        final SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            final Map<String, Object> result =
                    sqlSession.selectOne("org.apache.ibatis.submitted.includes.mapper.select");
            // Assert.assertEquals(Integer.valueOf(1), result);
        } finally {
            sqlSession.close();
        }
    }

}
