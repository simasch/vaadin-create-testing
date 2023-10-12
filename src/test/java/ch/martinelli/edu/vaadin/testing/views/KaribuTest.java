package ch.martinelli.edu.vaadin.testing.views;

import ch.martinelli.edu.vaadin.testing.Application;
import ch.martinelli.edu.vaadin.testing.TestApplication;
import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.github.mvysny.kaributesting.v10.spring.MockSpringServlet;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.SpringServlet;
import kotlin.jvm.functions.Function0;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

@Import(TestApplication.class)
@SpringBootTest
public abstract class KaribuTest {

    private static final Routes routes = new Routes().autoDiscoverViews(Application.class.getPackageName());

    @Autowired
    private ApplicationContext ctx;

    @BeforeEach
    public void setup() {
        final Function0<UI> uiFactory = UI::new;
        final SpringServlet servlet = new MockSpringServlet(routes, ctx, uiFactory);
        MockVaadin.setup(uiFactory, servlet);
    }

    @AfterEach
    public void tearDown() {
        MockVaadin.tearDown();
    }
}
