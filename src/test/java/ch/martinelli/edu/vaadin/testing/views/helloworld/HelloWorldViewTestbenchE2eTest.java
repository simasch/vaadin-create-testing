package ch.martinelli.edu.vaadin.testing.views.helloworld;

import ch.martinelli.edu.vaadin.testing.TestApplication;
import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.html.testbench.ParagraphElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.BrowserTest;
import com.vaadin.testbench.BrowserTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestApplication.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HelloWorldViewTestbenchE2eTest extends BrowserTestBase {

    @LocalServerPort
    private Integer port;

    @BeforeEach
    public void beforeEach() {
        getDriver().get("http://localhost:" + port);
    }

    @BrowserTest
    public void sayHello() {
        var name = $(TextFieldElement.class).first();
        name.setValue("Test");

        $(ButtonElement.class).first().click();

        var greeting = $(ParagraphElement.class).first();

        assertThat(greeting.getText()).isEqualTo("Hello Test");
    }
}