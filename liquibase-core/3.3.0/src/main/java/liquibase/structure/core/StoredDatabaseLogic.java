package liquibase.structure.core;

import liquibase.structure.AbstractDatabaseObject;
import liquibase.structure.DatabaseObject;

import java.util.Date;

public abstract class StoredDatabaseLogic<T extends StoredDatabaseLogic> extends AbstractDatabaseObject {
    @Override
    public DatabaseObject[] getContainingObjects() {
        return new DatabaseObject[]{
                getSchema()
        };
    }

    @Override
    public String getName() {
        return getAttribute("name", String.class);
    }

    @Override
    public T setName(String name) {
        setAttribute("name", name);
        return (T) this;
    }

    @Override
    public Schema getSchema() {
        return getAttribute("schema", Schema.class);
    }

    public T setSchema(Schema schema) {
        setAttribute("schema", schema);
        return (T) this;
    }

    public Boolean isValid() {
        return getAttribute("valid", Boolean.class);
    }

    public T setValid(Boolean valid) {
        setAttribute("valid", valid);
        return (T) this;
    }

    public String getBody() {
        return getAttribute("body", String.class);
    }

    public T setBody(String body) {
        setAttribute("body", body);
        return (T) this;
    }
}
