package su.radmi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import su.radmi.model.Departments;
import static org.assertj.core.api.Assertions.assertThat;


import java.io.InputStream;
import java.util.List;

public class FileJsonTest {

    ClassLoader classLoader = FileJsonTest.class.getClassLoader();

    @Test
    @DisplayName("Check Json file")
    void jsonFile() throws Exception{
        String nameJson = "Departments.json";

        try(InputStream inputStream = classLoader.getResourceAsStream(nameJson)){
            ObjectMapper objectMapper = new ObjectMapper();
            List<Departments> departments = objectMapper.readValue(inputStream, new TypeReference<>() {});
            assertThat(departments.get(0).organizationId).isEqualTo(29);
            assertThat(departments.get(0).parent.parentId).isEqualTo(0);
            assertThat(departments.get(2).parent.parentTitle).isEqualTo("Security Department");
        }

    }
}
