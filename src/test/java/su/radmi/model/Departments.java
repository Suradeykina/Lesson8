package su.radmi.model;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import static org.apache.commons.lang3.RandomUtils.nextInt;

public class Departments {

    public Parent parent;
    public int organizationId;
    public boolean isActive;
    public String code;
    public int departmentId;
    public String name;

    public static class Parent {
        public String parentTitle;
        public int parentId;
    }

}
