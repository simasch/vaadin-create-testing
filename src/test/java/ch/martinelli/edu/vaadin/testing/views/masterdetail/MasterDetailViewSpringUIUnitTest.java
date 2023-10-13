package ch.martinelli.edu.vaadin.testing.views.masterdetail;

import ch.martinelli.edu.vaadin.testing.TestApplication;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridTester;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.testbench.unit.SpringUIUnitTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestApplication.class)
@SpringBootTest
class MasterDetailViewSpringUIUnitTest extends SpringUIUnitTest {

    @Test
    void selectFirstRowAndEditFirstName() {
        navigate(MasterDetailView.class);

        var grid = $(Grid.class).first();
        var gridTester = test(GridTester.class, grid);
        gridTester.sortByColumn(0);

        var firstName = gridTester.getCellText(0, 0);
        assertThat(firstName).isEqualTo("Aaron");

        gridTester.clickRow(0);

        var firstNameInput  = $(TextField.class).withId("first-name").first();
        test(firstNameInput).setValue("Aaron Peter");

        var save = $(Button.class).withId("save").first();
        test(save).click();

        var notification = $(Notification.class).first();
        assertThat(test(notification).getText()).isEqualTo("Data updated");

        firstName = gridTester.getCellText(0, 0);
        assertThat(firstName).isEqualTo("Aaron Peter");
    }
}