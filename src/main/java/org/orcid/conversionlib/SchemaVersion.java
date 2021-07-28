package org.orcid.conversionlib;
public enum SchemaVersion {
    V2_0("record_2.0/record-2.0.xsd", org.orcid.jaxb.model.record_v2.Record.class,org.orcid.jaxb.model.error_v2.OrcidError.class), 
    V2_1("record_2.1/record-2.1.xsd", org.orcid.jaxb.model.record_v2.Record.class,org.orcid.jaxb.model.error_v2.OrcidError.class), 
    V3_0RC1("record_3.0_rc1/record-3.0_rc1.xsd", org.orcid.jaxb.model.v3.rc1.record.Record.class,org.orcid.jaxb.model.v3.rc1.error.OrcidError.class),
    V3_0("record_3.0/record-3.0.xsd", org.orcid.jaxb.model.v3.release.record.Record.class,org.orcid.jaxb.model.v3.release.error.OrcidError.class);

    public final String location;
    public final Class<?> modelClass;
    public final Class<?> errorClass;

    SchemaVersion(String loc, Class<?> modelClass, Class<?> errorClass) {
        this.location = loc;
        this.modelClass = modelClass;
        this.errorClass = errorClass;
    }
}
