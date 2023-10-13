package ch.martinelli.edu.vaadin.testing.views.masterdetail;

import ch.martinelli.edu.vaadin.testing.TestApplication;
import ch.martinelli.edu.vaadin.testing.domain.Person;
import ch.martinelli.edu.vaadin.testing.views.KaribuTest;
import com.github.mvysny.kaributesting.v10.GridKt;
import com.github.mvysny.kaributesting.v10.NotificationsKt;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.QuerySortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static com.github.mvysny.kaributesting.v10.LocatorJ.*;
import static org.assertj.core.api.Assertions.assertThat;

@Import(TestApplication.class)
@SpringBootTest
class MasterDetailViewKaribuTest extends KaribuTest {

    @Test
    void selectFirstRowAndEditFirstName() {
        UI.getCurrent().navigate(MasterDetailView.class);

        Grid<Person> grid = _get(Grid.class);
        GridKt._sort(grid, QuerySortOrder.asc("firstName").build().getFirst());

        var row = GridKt._get(grid, 0);
        assertThat(row.getFirstName()).isEqualTo("Aaron");

        GridKt._clickItem(grid, 0);

        var firstNameInput = _get(TextField.class, spec -> spec.withId("first-name"));
        _setValue(firstNameInput, "Aaron Peter");

        var save = _get(Button.class, spec -> spec.withId("save"));
        _click(save);

        NotificationsKt.expectNotifications("Data updated");

        row = GridKt._get(grid, 0);
        assertThat(row.getFirstName()).isEqualTo("Aaron Peter");
    }
}