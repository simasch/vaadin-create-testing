package ch.martinelli.edu.vaadin.testing.views.masterdetail;

import ch.martinelli.edu.vaadin.testing.domain.Person;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;

import java.util.function.Consumer;

public class PersonForm extends VerticalLayout {

    private final Binder<Person> binder = new BeanValidationBinder<>(Person.class);
    private transient Person person;

    public PersonForm(Consumer<Person> onSave, Runnable onCancel) {
        setClassName("editor-layout");

        var form = new FormLayout();
        add(form);

        var firstName = new TextField("First Name");
        firstName.setId("first-name");
        binder.forField(firstName).asRequired().bind("firstName");

        var lastName = new TextField("Last Name");
        lastName.setId("last-name");
        binder.forField(lastName).asRequired().bind("lastName");

        var email = new EmailField("Email");
        email.setId("email");
        binder.forField(email).asRequired().bind("email");

        form.add(firstName, lastName, email);

        var cancel = new Button("Cancel");
        cancel.setId("cancel");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancel.addClickListener(e -> onCancel.run());

        var save = new Button("Save");
        save.setId("save");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(e -> {
            if (this.person == null) {
                this.person = new Person();
            }
            if (binder.validate().isOk()) {
                binder.writeBeanIfValid(this.person);

                onSave.accept(person);
            }
        });

        var buttons = new HorizontalLayout(cancel, save);

        add(buttons);
    }

    public void setPerson(Person person) {
        this.person = person;
        binder.readBean(person);
    }
}
