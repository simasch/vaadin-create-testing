package ch.martinelli.edu.vaadin.testing.views.helloworld;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.html.testbench.ParagraphElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.BrowserTest;
import com.vaadin.testbench.BrowserTestBase;
import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.api.Assertions.assertThat;

class HelloWorldViewTestbenchE2eIT extends BrowserTestBase {
    @BeforeEach
    public void beforeEach() {
        getDriver().get("http://localhost:8080");
    }

    @BrowserTest
    public void clickButton() {
        var name = $(TextFieldElement.class).first();
        name.setValue("Test");

        $(ButtonElement.class).first().click();

        var greeting = $(ParagraphElement.class).first();

        assertThat(greeting.getText()).isEqualTo("Hello Test");
    }
}