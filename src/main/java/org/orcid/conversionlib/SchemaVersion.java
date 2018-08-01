package org.orcid.conversionlib;

public enum SchemaVersion {
    V2_0("record_2.0/record-2.0.xsd", org.orcid.jaxb.model.record_v2.Record.class), 
    V2_1("record_2.1/record-2.1.xsd", org.orcid.jaxb.model.record_v2.Record.class), 
    V3_0RC1("record_3.0_rc1/record-3.0_rc1.xsd", org.orcid.jaxb.model.v3.rc1.record.Record.class);

    public final String location;
    public final Class<?> modelClass;

    SchemaVersion(String loc, Class<?> clazz) {
        this.location = loc;
        this.modelClass = clazz;
    }
}
