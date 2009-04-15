/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import static com.mysema.query.grammar.GrammarWithAlias.$;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

/**
 * StringHandlingTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class StringHandlingTest extends AbstractQueryTest {

    private List<String> data1 = Arrays.asList("petER", "THomas", "joHAN");
    
    private List<String> data2 = Arrays.asList("PETer", "thOMAS", "JOhan");
    
    private List<String> data = Arrays.asList("abc","aBC","def");
    
    @Test
    public void equalsIgnoreCase(){
        Iterator<String> res = Arrays.asList("petER - PETer","THomas - thOMAS", "joHAN - JOhan").iterator();
        for (Object[] arr : query().from($("a"), data1).from($("b"), data2).where($("a").equalsIgnoreCase($("b"))).list($("a"),$("b"))){
            assertEquals(res.next(), arr[0]+" - "+arr[1]);
        }
    }
    
    @Test
    public void startsWithIgnoreCase(){        
        assertEquals(2, MiniApi.from($("a"), data).where($("a").startsWithIgnoreCase("AB")).count());
        assertEquals(2, MiniApi.from($("a"), data).where($("a").startsWithIgnoreCase("ab")).count());
    }
    
    @Test
    public void endsWithIgnoreCase(){
        assertEquals(2, MiniApi.from($("a"), data).where($("a").endsWithIgnoreCase("BC")).count());
        assertEquals(2, MiniApi.from($("a"), data).where($("a").endsWithIgnoreCase("bc")).count());
    }
    
}
