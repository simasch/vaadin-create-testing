package ch.martinelli.edu.vaadin.testing.views.helloworld;

import ch.martinelli.edu.vaadin.testing.views.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@PageTitle("Hello World")
@Route(value = "hello", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class HelloWorldView extends VerticalLayout {

    public HelloWorldView() {
        var name = new TextField("Your name");
        var sayHello = new Button("Say hello");
        var greeting = new Paragraph();
        greeting.setId("hello-greeting");

        sayHello.addClickListener(e -> {
            greeting.setText("Hello %s".formatted(name.getValue()));
        });
        sayHello.addClickShortcut(Key.ENTER);

        var form = new HorizontalLayout(name, sayHello);
        form.setVerticalComponentAlignment(Alignment.END, name, sayHello);

        add(form, greeting);
    }
}
