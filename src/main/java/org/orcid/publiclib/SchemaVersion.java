package org.orcid.publiclib;

public enum SchemaVersion {
    V2_0("record_2.0/record-2.0.xsd"), V2_1("record_2.1/record-2.1.xsd"), V3_0RC1("record_3.0_rc1/record-3.0_rc1.xsd");
    
    public final String location;
    
    SchemaVersion(String loc){
        this.location=loc;
    }
}
