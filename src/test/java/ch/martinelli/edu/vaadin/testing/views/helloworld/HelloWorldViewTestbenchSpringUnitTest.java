package ch.martinelli.edu.vaadin.testing.views.helloworld;

import ch.martinelli.edu.vaadin.testing.TestApplication;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.testbench.unit.SpringUIUnitTest;
import com.vaadin.testbench.unit.UIUnitTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestApplication.class)
@SpringBootTest
class HelloWorldViewTestbenchSpringUnitTest extends SpringUIUnitTest {

    @Test
    void sayHello() {
        navigate(HelloWorldView.class);

        var name = $(TextField.class).withCaption("Your name").first();
        test(name).setValue("Test");

        test($(Button.class).withCaption("Say hello").first()).click();

        var greeting = $(Paragraph.class).withId("hello-greeting").first();

        assertThat(greeting.getText()).isEqualTo("Hello Test");
    }

}