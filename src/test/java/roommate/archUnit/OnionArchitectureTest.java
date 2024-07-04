package roommate.archUnit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roommate.RoomMateApplication;

import static com.tngtech.archunit.library.Architectures.onionArchitecture;

public class OnionArchitectureTest {
    private final JavaClasses klassen = new ClassFileImporter().importPackagesOf(RoomMateApplication.class);

    @Test
    @DisplayName("Die RoomMate Anwendung hat eine Onion Architektur")
    public void test_1() throws Exception {
        ArchRule rule = onionArchitecture()
                .domainModels("roommate.domain.model..")
                .domainServices("roommate.domain.service..")
                .applicationServices("roommate.application.service..")
                .adapter("web", "roommate.web..");

        rule.check(klassen);
    }
}
