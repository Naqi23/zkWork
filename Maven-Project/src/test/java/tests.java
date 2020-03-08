import demo.getting_started.tutorial.Car;
import demo.getting_started.tutorial.CarService;
import demo.getting_started.tutorial.CarServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class tests {

    CarServiceImpl carService = null;

    @Before
    public void setUpService() {
        carService = new CarServiceImpl();
    }

    @Test
    public void testAddCar() {
        Car car = new Car()
    }

    @Test
    public void testDelCar() {

    }
}
