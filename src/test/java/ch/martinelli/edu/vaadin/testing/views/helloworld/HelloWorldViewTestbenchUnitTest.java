package ch.martinelli.edu.vaadin.testing.views.helloworld;

import ch.martinelli.edu.vaadin.testing.views.KaribuTest;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.testbench.unit.UIUnitTest;
import org.junit.jupiter.api.Test;

import static com.github.mvysny.kaributesting.v10.LocatorJ.*;
import static org.assertj.core.api.Assertions.assertThat;

class HelloWorldViewTestbenchUnitTest extends UIUnitTest {

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