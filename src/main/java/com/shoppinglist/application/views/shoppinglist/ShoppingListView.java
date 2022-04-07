package com.shoppinglist.application.views.shoppinglist;

import com.shoppinglist.application.Application;
import com.shoppinglist.application.views.MainLayout;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.model.style.Color;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@PageTitle("Shopping List")
@Route(value = "shoppinglist", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class ShoppingListView extends Div implements AfterNavigationObserver {

    private ComboBox<Icon> comboBox;
    private TextField name;
    private NumberField number;
    private Button save;
    private Button cancel;
    private ShopLine newLine;
    private ArrayList<ShopLine> undoneLine = new ArrayList();
    private ArrayList<ShopLine> doneLine = new ArrayList();
    private RadioButtonGroup<Category> radioGroup = new RadioButtonGroup<>();

    private Connection conn;

    VerticalLayout addForm = new VerticalLayout();
    HorizontalLayout addLine = new HorizontalLayout();
    HorizontalLayout commandButton = new HorizontalLayout();
    VerticalLayout mainBody = new VerticalLayout();
    HorizontalLayout separator = new HorizontalLayout();

    public ShoppingListView() {

        addClassName("card-list-view");
        setSizeFull();

        separator.setHeight(15, Unit.PIXELS);

        CommandButton();
        AddForm();
        add(mainBody);
        //папа добавил
        mainBody.setSpacing(false);
        mainBody.getThemeList().add("spacing-xs");

        try {
            conn = Application.getConnection();
            getDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void newLine(int id, int idCategory, String name, double number, boolean status){
        newLine = new ShopLine(id, idCategory, name, number, undoneLine, doneLine, this, status);
        refresh();
    }

    public void refresh(){
        mainBody.removeAll();

        sortLine(undoneLine);

        for(int i = 0; i < undoneLine.size(); i++) {
            mainBody.add(undoneLine.get(i).getPanel());
            undoneLine.get(i).setCheckBox(false);
            undoneLine.get(i).hl.getStyle().set("background", undoneLine.get(i).getColor());
        }

        mainBody.add(separator);

        for(int i = doneLine.size() - 1; i >= 0; i--) {
            mainBody.add(doneLine.get(i).getPanel());
            doneLine.get(i).setCheckBox(true);
            doneLine.get(i).hl.getStyle().set("background", "#D9D9D9");
        }
    }

    private void CommandButton(){
        Button add = new Button(new Icon(VaadinIcon.PLUS));
        add.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        commandButton.add(add);

        add.addClickListener(e -> {
            if (this.addForm.isVisible())
                this.addForm.setVisible(false);
            else
                this.addForm.setVisible(true);
            name.setValue("");
            number.setValue(0.0);
        });
        this.add(commandButton);
    }

    private void AddForm() {

        name = new TextField("Наименование");
        number = new NumberField("Количество");
        number.setStep(0.5);
        number.setHasControls(true);
        save = new Button(new Icon(VaadinIcon.CHECK));
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        //addLine.getVerticalComponentAlignment(save);
        addLine.setVerticalComponentAlignment(FlexComponent.Alignment.END, save);
        cancel = new Button(new Icon(VaadinIcon.CLOSE));
        cancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        //addLine.getVerticalComponentAlignment(cancel);
        addLine.setVerticalComponentAlignment(FlexComponent.Alignment.END, cancel);

        addLine.add(name);
        addLine.add(number);
        addLine.add(save);
        addLine.add(cancel);

        cancel.addClickListener(e -> {
            this.addForm.setVisible(false);
        });

        save.addClickListener(e -> {
            if (name.getValue() != null & name.getValue() != "" & number.getValue() != null) {

                Statement stmt = null;
                try {
                    stmt = Application.getConnection().createStatement();
                    ResultSet rs = stmt.executeQuery("insert into shoppinglists (\"idCategory\", name, number, userslists_id) values ("+ radioGroup.getValue().id +
                            ", '" + name.getValue() + "', " + number.getValue().toString() + ", " + Application.getUser_id() +") returning id;");
                    while(rs.next()){
                        newLine(rs.getInt("id"), radioGroup.getValue().id, name.getValue(), number.getValue(), true);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                name.setValue("");
                number.setValue(0.0);
            }
            else
            {
                Notification.show("Неправильно указаны параметры");
            }
        });

        radioGroup.setLabel("Категория");

        List<Category> category = new ArrayList<>();
        category.add(new Category(1, new Icon(VaadinIcon.TAG), "#FFCCCC"));
        category.add(new Category(2, new Icon(VaadinIcon.TAG), "#FFCC99"));
        category.add(new Category(3, new Icon(VaadinIcon.TAG), "#FFFFCC"));
        category.add(new Category(4, new Icon(VaadinIcon.TAG), "#CCFFCC"));
        category.add(new Category(5, new Icon(VaadinIcon.TAG), "#99FFCC"));
        category.add(new Category(6, new Icon(VaadinIcon.TAG), "#99CCFF"));
        category.add(new Category(7, new Icon(VaadinIcon.TAG), "#CCCCFF"));
        category.add(new Category(8, new Icon(VaadinIcon.TAG), "#FFCCFF"));

        radioGroup.setItems(category);
        radioGroup.setValue(category.get(0));

        radioGroup.setRenderer(new ComponentRenderer<>(Category -> {
            HorizontalLayout hll = new HorizontalLayout(Category.icon);
            hll.getStyle().set("color", Category.color);
            return new Div(hll);
        }));

        addForm.add(addLine);
        addForm.add(radioGroup);
        this.add(addForm);
        addForm.setVisible(false);


    }

    private void sortLine(ArrayList<ShopLine> undoneLine) {
        boolean needIteration = true;
        while (needIteration) {
            needIteration = false;
            for (int i = 1; i < undoneLine.size(); i++) {
                if (undoneLine.get(i).getIdCategory() < undoneLine.get(i - 1).getIdCategory()) {
                    swap(undoneLine, i, i - 1);
                    needIteration = true;
                }
            }
        }
    }

    private void swap(ArrayList<ShopLine> undoneLine, int ind1, int ind2) {
        ShopLine tmp = undoneLine.get(ind1);
        undoneLine.set(ind1, undoneLine.get(ind2));
        undoneLine.set(ind2, tmp);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {

    }

    private  void getDB() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM ShoppingLists where userslists_id = " + Application.getUser_id());

        while(rs.next()){
            newLine(rs.getInt("id"), rs.getInt("idCategory"), rs.getString("name"), rs.getDouble("number"), rs.getBoolean("status"));
        }

    }
}

class Category {
    public Icon icon;
    public String color;
    public int id;

    public Category(int id, Icon icon, String color) {
        this.id = id;
        this.icon = icon;
        this.color = color;
    }
}