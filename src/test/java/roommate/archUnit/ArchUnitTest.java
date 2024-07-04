package roommate.archUnit;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.GeneralCodingRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import roommate.RoomMateApplication;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

@AnalyzeClasses(packagesOf = RoomMateApplication.class, importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchUnitTest {

    @ArchTest
    ArchRule keineMemberSollenAutowiredSein = GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION;

    @ArchTest
    ArchRule alleKlassenInApplicationServiceSolltenAnnotiertSeinMitService = classes().
            that()
            .resideInAPackage("..application.service..")
            .should()
            .beAnnotatedWith(Service.class);

    @ArchTest
    ArchRule variablenInControllernSolltenPrivatSein = fields()
            .that()
            .areDeclaredInClassesThat()
            .areAnnotatedWith(Controller.class)
            .should()
            .bePrivate();

    @ArchTest
    ArchRule nurControllerSolltenAufControllerZugreifen = noClasses()
            .that()
            .resideOutsideOfPackage("..roommate.web.controller..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("..roommate.web.controller..");
}
