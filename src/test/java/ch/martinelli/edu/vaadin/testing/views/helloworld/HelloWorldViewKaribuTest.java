package ch.martinelli.edu.vaadin.testing.views.helloworld;

import ch.martinelli.edu.vaadin.testing.views.KaribuTest;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.textfield.TextField;
import org.junit.jupiter.api.Test;

import static com.github.mvysny.kaributesting.v10.LocatorJ.*;
import static org.assertj.core.api.Assertions.assertThat;

class HelloWorldViewKaribuTest extends KaribuTest {

    @Test
    void sayHello() {
        UI.getCurrent().navigate(HelloWorldView.class);

        var name = _get(TextField.class, spec -> spec.withCaption("Your name"));
        _setValue(name, "Test");

        var button = _get(Button.class, spec -> spec.withCaption("Say hello"));
        _click(button);

        var greeting = _get(Paragraph.class, spec -> spec.withId("hello-greeting"));

        assertThat(greeting.getText()).isEqualTo("Hello Test");
    }

}