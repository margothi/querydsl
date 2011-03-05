/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import com.mysema.query.types.Ops;

/**
 * HSQLDBTemplates is an SQL dialect for HSQLDB
 *
 * @author tiwe
 *
 */
public class HSQLDBTemplates extends SQLTemplates {

    public HSQLDBTemplates(){
        this('\\', false);
    }
    
    public HSQLDBTemplates(boolean quote){
        this('\\',quote);
    }

    public HSQLDBTemplates(char escape, boolean quote){
        super("\"", escape, quote);
        setAutoIncrement(" identity");
        add(Ops.MathOps.ROUND, "round({0},0)");
        add(Ops.TRIM, "trim(both from {0})");
        add(Ops.NEGATE, "{0} * -1", 7);
    }

}
