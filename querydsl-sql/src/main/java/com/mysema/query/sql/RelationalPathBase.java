package com.mysema.query.sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.path.BeanPath;

/**
 * RelationalPathBase is a base class for RelationPath implements
 * 
 * @author tiwe
 *
 * @param <T> entity type
 */
public class RelationalPathBase<T> extends BeanPath<T> implements RelationalPath<T> {
    
    private static final long serialVersionUID = -7031357250283629202L;
    
    @Nullable
    private PrimaryKey<T> primaryKey;
    
    @Nullable
    private Path<?>[] all;
    
    private final List<Path<?>> columns = new ArrayList<Path<?>>();
    
    private final List<ForeignKey<?>> foreignKeys = new ArrayList<ForeignKey<?>>();
    
    private final List<ForeignKey<?>> inverseForeignKeys = new ArrayList<ForeignKey<?>>();
    
    private final String schema, table;
    
    private transient FactoryExpression<T> projection;

    public RelationalPathBase(Class<? extends T> type, String variable, String schema, String table) {
        this(type, PathMetadataFactory.forVariable(variable), schema, table);
    }

    public RelationalPathBase(Class<? extends T> type, PathMetadata<?> metadata, String schema, String table) {
        super(type, metadata);
        this.schema = schema;
        this.table = table;
    }

    protected PrimaryKey<T> createPrimaryKey(Path<?>... columns) {
        primaryKey = new PrimaryKey<T>(this, columns);
        return primaryKey;
    }
    
    protected <F> ForeignKey<F> createForeignKey(Path<?> local, String foreign) {
        ForeignKey<F> foreignKey = new ForeignKey<F>(this, local, foreign);
        foreignKeys.add(foreignKey);
        return foreignKey;
    }
    
    protected <F> ForeignKey<F> createForeignKey(List<? extends Path<?>> local, List<String> foreign) {
        ForeignKey<F> foreignKey = new ForeignKey<F>(this, local, foreign);
        foreignKeys.add(foreignKey);
        return foreignKey;
    }
    
    protected <F> ForeignKey<F> createInvForeignKey(Path<?> local, String foreign) {
        ForeignKey<F> foreignKey = new ForeignKey<F>(this, local, foreign);
        inverseForeignKeys.add(foreignKey);
        return foreignKey;
    }
    
    protected <F> ForeignKey<F> createInvForeignKey(List<? extends Path<?>> local, List<String> foreign) {
        ForeignKey<F> foreignKey = new ForeignKey<F>(this, local, foreign);
        inverseForeignKeys.add(foreignKey);
        return foreignKey;
    }
    
    @Override
    public FactoryExpression<T> getProjection() {
        if (projection == null) {
            projection = RelationalPathUtils.createProjection(this);  
        }
        return projection;
        
    }
    
    public Path<?>[] all() {
        if (all == null || all.length != columns.size()) {
            all = new Path[columns.size()];
            columns.toArray(all);
        }
        return all;
    }
    
    @Override
    protected <P extends Path<?>> P add(P path) {
        columns.add(path);
        return path;
    }
    
    @Override
    public List<Path<?>> getColumns() {
        return columns;
    }

    @Override
    public Collection<ForeignKey<?>> getForeignKeys() {
        return foreignKeys;
    }

    @Override
    public Collection<ForeignKey<?>> getInverseForeignKeys() {
        return inverseForeignKeys;
    }

    @Override
    public PrimaryKey<T> getPrimaryKey() {
        return primaryKey;
    }

    @Override
    public String getSchemaName() {
        return schema;
    }
    
    @Override
    public String getTableName() {
        return table;
    }

}
