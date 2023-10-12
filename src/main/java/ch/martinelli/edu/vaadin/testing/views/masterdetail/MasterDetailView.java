package ch.martinelli.edu.vaadin.testing.views.masterdetail;

import ch.martinelli.edu.vaadin.testing.data.Person;
import ch.martinelli.edu.vaadin.testing.services.PersonService;
import ch.martinelli.edu.vaadin.testing.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;

import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("Master-Detail")
@Route(value = "master-detail/:" + MasterDetailView.PERSON_ID + "?/:action?(edit)", layout = MainLayout.class)
public class MasterDetailView extends Div implements BeforeEnterObserver {

    public static final String PERSON_ID = "personID";
    private final Grid<Person> grid = new Grid<>(Person.class, false);

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final Binder<Person> binder = new BeanValidationBinder<>(Person.class);

    private Person person;

    private final PersonService personService;

    public MasterDetailView(PersonService personService) {
        this.personService = personService;

        addClassNames("master-detail-view");

        // Create UI
        var splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("firstName").setAutoWidth(true);
        grid.addColumn("lastName").setAutoWidth(true);
        grid.addColumn("email").setAutoWidth(true);
        grid.setItems(query -> personService.list(
                        PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(MasterDetailView.class, new RouteParam(PERSON_ID, event.getValue().getId().toString()));
            } else {
                clearForm();
                UI.getCurrent().navigate(MasterDetailView.class);
            }
        });

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.person == null) {
                    this.person = new Person();
                }
                binder.writeBean(this.person);
                personService.update(this.person);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(MasterDetailView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show("Failed to update the data. Check again that all values are valid");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        event.getRouteParameters().get(PERSON_ID).map(Long::parseLong)
                .ifPresent(aLong -> {
                    var personFromBackend = personService.get(aLong);
                    if (personFromBackend.isPresent()) {
                        populateForm(personFromBackend.get());
                    } else {
                        Notification.show(String.format("The requested person was not found, ID = %s", aLong), 3000,
                                Position.BOTTOM_START);
                        // when a row is selected but the data is no longer available, refresh grid
                        refreshGrid();
                        event.forwardTo(MasterDetailView.class);
                    }
                });
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        var editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        var editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        var formLayout = new FormLayout();

        var firstName = new TextField("First Name");
        binder.forField(firstName).asRequired().bind("firstName");

        var lastName = new TextField("Last Name");
        binder.forField(lastName).asRequired().bind("lastName");

        var email = new EmailField("Email");
        binder.forField(email).asRequired().bind("email");

        formLayout.add(firstName, lastName, email);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        var buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");

        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        buttonLayout.add(save, cancel);

        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        var wrapper = new Div();
        wrapper.setClassName("grid-wrapper");

        splitLayout.addToPrimary(wrapper);

        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Person value) {
        this.person = value;

        binder.readBean(this.person);
    }
}
