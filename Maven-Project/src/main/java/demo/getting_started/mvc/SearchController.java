package demo.getting_started.mvc;


import java.util.List;
import java.util.Set;

import javafx.scene.control.ListCell;
import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;
import org.zkoss.zul.ext.Selectable;

import demo.getting_started.tutorial.Car;
import demo.getting_started.tutorial.CarService;
import demo.getting_started.tutorial.CarServiceImpl;

public class SearchController extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;
	
	@Wire
	private Textbox keywordBox;
	@Wire
	private Listbox carListbox;
	@Wire
	private Label modelLabel;
	@Wire
	private Label makeLabel;
	@Wire
	private Label priceLabel;
	@Wire
	private Label descriptionLabel;
	@Wire
	private Image previewImage;
	@Wire
	private Component detailBox;

	// Add Car Components
	@Wire
	private Textbox carModelTextBox;
	@Wire
	private Textbox carMakeTextBox;
	@Wire
	private Textbox carDescTextBox;
	@Wire
	private Textbox carPriceTextBox;
	@Wire
	private Textbox carColorTextBox;

	// Delete Car components
	@Wire
	private Button delCarButton;
	
	private CarService carService;
	private ListModel<Car> carsModel;
	
	public SearchController() {
		carService = new CarServiceImpl();
		carsModel = new ListModelList<Car>(carService.findAll());
	}

	@Listen("onClick = #delCarButton")
	public void delCarConfirmation() {
		Messagebox.show("Delete?", "Prompt",
				Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener<Event>() {
					@Override
					public void onEvent(Event event) throws Exception {
						switch (((Integer)event.getData()).intValue()) {
							case Messagebox.YES:
								delCar();
								System.out.println("Yes Del");
								break;
							case Messagebox.NO:
								System.out.println("No Del");
								break;
						}
					}
				});
	}

	public void delCar() {
		int index = carListbox.getSelectedIndex();
		Listitem selectCar = carListbox.getItemAtIndex(index);
		Car car = (Car) selectCar.getValue();
		carService.deleteCar(car);
		carListbox.setModel(new ListModelList<Car>(carService.findAll()));
		Messagebox.show("Car Deleted");
	}

	@Listen("onClick = #addCarSubmitButton")
	public void addCar() {

		// Validation done at the front end so not needed here :)
		String modelVal = carModelTextBox.getValue();
		String makeVal = carMakeTextBox.getValue();
		String descVal = carDescTextBox.getValue();
		Integer priceVal = Integer.parseInt(carPriceTextBox.getValue());
		String colorVal = carColorTextBox.getValue();

		carService.addCar(modelVal, makeVal, descVal, null, priceVal, colorVal);
		carListbox.setModel(new ListModelList<Car>(carService.findAll()));
		Messagebox.show("New Car Added");
	}
	
	public ListModel<Car> getCarsModel() {
		return carsModel;
	}
	
	@Listen("onClick = #searchButton")
	public void search(){
		String keyword = keywordBox.getValue();
		List<Car> result = carService.search(keyword);
		if (result.isEmpty()) {
			detailBox.setVisible(false);
		}
		carListbox.setModel(new ListModelList<Car>(result));
	}
	
	@Listen("onSelect = #carListbox")
	public void showDetail(){
		detailBox.setVisible(true);
		delCarButton.setVisible(true);
		
		Set<Car> selection = ((Selectable<Car>)carListbox.getModel()).getSelection();
		if (selection!=null && !selection.isEmpty()){
			Car selected = selection.iterator().next();
			previewImage.setSrc(selected.getPreview());
			modelLabel.setValue(selected.getModel());
			makeLabel.setValue(selected.getMake());
			priceLabel.setValue(selected.getPrice().toString());
			descriptionLabel.setValue(selected.getDescription());
		}
	}
}
