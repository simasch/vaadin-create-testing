package ch.martinelli.edu.vaadin.testing.views.masterdetail;

import ch.martinelli.edu.vaadin.testing.domain.Person;
import ch.martinelli.edu.vaadin.testing.domain.PersonRepository;
import ch.martinelli.edu.vaadin.testing.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("Master-Detail")
@Route(value = "master-detail/:" + MasterDetailView.PERSON_ID + "?/:action?(edit)", layout = MainLayout.class)
public class MasterDetailView extends SplitLayout implements BeforeEnterObserver {

    public static final String PERSON_ID = "personID";
    private final Grid<Person> grid = new Grid<>(Person.class, false);

    private final transient PersonRepository personRepository;
    private PersonForm personForm;

    public MasterDetailView(PersonRepository personRepository) {
        this.personRepository = personRepository;

        setSizeFull();
        setSplitterPosition(70);

        createGrid();
        createPersonForm();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        event.getRouteParameters().get(PERSON_ID).map(Long::parseLong)
                .ifPresent(aLong -> {
                    var person = personRepository.findById(aLong);
                    if (person.isPresent()) {
                        personForm.setPerson(person.get());
                    } else {
                        Notification.show(String.format("The requested person was not found, ID = %s", aLong), 3000,
                                Position.BOTTOM_START);
                        // when a row is selected but the data is no longer available, refresh grid
                        refreshGrid();
                        event.forwardTo(MasterDetailView.class);
                    }
                });
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void createGrid() {
        grid.setSizeFull();

        grid.addColumn("firstName").setAutoWidth(true);
        grid.addColumn("lastName").setAutoWidth(true);
        grid.addColumn("email").setAutoWidth(true);
        grid.setItems(query -> personRepository.findAll(
                        PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(MasterDetailView.class, new RouteParam(PERSON_ID, event.getValue().getId().toString()));
            } else {
                personForm.setPerson(null);
                UI.getCurrent().navigate(MasterDetailView.class);
            }
        });

        addToPrimary(grid);
    }

    private void createPersonForm() {
        personForm = new PersonForm(
                person -> {
                    try {
                        personRepository.save(person);
                        personForm.setPerson(null);
                        refreshGrid();

                        Notification.show("Data updated");
                        UI.getCurrent().navigate(MasterDetailView.class);
                    } catch (ObjectOptimisticLockingFailureException exception) {
                        var notification = Notification.show(
                                "Error updating the data. Somebody else has updated the record while you were making changes.");
                        notification.setPosition(Position.MIDDLE);
                        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    }
                },
                () -> {
                    personForm.setPerson(null);
                    refreshGrid();
                });

        addToSecondary(personForm);
    }
}
