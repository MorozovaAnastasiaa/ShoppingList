package com.shoppinglist.application.views.shoppinglist;

import com.shoppinglist.application.Application;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class ShopLine {
    private int id;
    private int idCategory;
    private String name;
    private double number;
    private Checkbox checkBox;
    private Button closeButton = new Button(new Icon(VaadinIcon.CLOSE_SMALL));
    private boolean status;
    private String color;


    HorizontalLayout hl = new HorizontalLayout();

    public ShopLine(int id, int idCategory, String name, double number, ArrayList<ShopLine> undoneLine, ArrayList<ShopLine> doneLine, ShoppingListView sLV, boolean status) {
        this.id = id;
        this.idCategory = idCategory;
        this.name = name;
        this.number = number;
        this.status = status;

        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Вы уверенны?");
        dialog.setText("Удаленную строку невозможно восстановить");

        dialog.setCancelable(false);

        dialog.setRejectable(true);
        dialog.setRejectText("Нет");

        dialog.setConfirmText("Да");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(event -> setStatus(true, doneLine, undoneLine, sLV));

        hl.addClassName("card");
        hl.setSpacing(false);
        hl.getThemeList().add("spacing-s");
        hl.setHeight(50, Unit.PIXELS);

        Span nameLabel = new Span(this.name);
        nameLabel.setWidth(95, Unit.PERCENTAGE);
        nameLabel.addClassName("name");
        hl.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, nameLabel);

        Span numberLabel = new Span(Double.toString(this.number));
        numberLabel.setWidth(20, Unit.PIXELS);
        numberLabel.addClassName("name");
        hl.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, numberLabel);

        //Span idLabel = new Span(Integer.toString(this.idCategory));

        switch (idCategory) {
            case(1):
                hl.getStyle().set("background", "#FFCCCC");
                break;
            case(2):
                hl.getStyle().set("background", "#FFCC99");
                break;
            case(3):
                hl.getStyle().set("background", "#FFFFCC");
                break;
            case(4):
                hl.getStyle().set("background", "#CCFFCC");
                break;
            case(5):
                hl.getStyle().set("background", "#99FFCC");
                break;
            case(6):
                hl.getStyle().set("background", "#99CCFF");
                break;
            case(7):
                hl.getStyle().set("background", "#CCCCFF");
                break;
            case(8):
                hl.getStyle().set("background", "#FFCCFF");
                break;
            default:
                break;
        }
        this.color = hl.getStyle().get("background");

        checkBox = new Checkbox();
        hl.addClickListener(e -> {
            if (undoneLine.contains(this)) {
                undoneLine.remove(this);
                doneLine.add(this);
            } else {
                doneLine.remove(this);
                undoneLine.add(this);
            }
            setStatusDB();
            sLV.refresh();
        });

        closeButton.addThemeVariants(ButtonVariant.LUMO_SMALL);

        closeButton.addClickListener(e -> {
            dialog.open();
        });

        hl.setWidthFull();
        hl.getStyle().set("border","1px solid black");
        //hl.add(checkBox);
        hl.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, checkBox);
        hl.add(nameLabel);
        hl.add(numberLabel);
        hl.add(closeButton);
        hl.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, closeButton);

        hl.setMaxWidth(600, Unit.PIXELS);


        if (status)
            undoneLine.add(this);
        else
            doneLine.add(this);

    }

    private void setStatus(boolean discarded, ArrayList<ShopLine> undoneLine, ArrayList<ShopLine> doneLine, ShoppingListView sLV) {
        if(discarded) {
            if (undoneLine.contains(this))
                undoneLine.remove(this);
            else
                doneLine.remove(this);
            Statement stmt = null;
            try {
                stmt = Application.getConnection().createStatement();
                stmt.executeQuery("delete from shoppinglists where id = " + this.id);
            } catch (SQLException ex) {
                if (!ex.getSQLState().equals("02000"))
                    ex.printStackTrace();
            }
            sLV.refresh();
        }
    }

    public HorizontalLayout getPanel(){
        return hl;
    }

    public boolean Checked ()
    {
        return checkBox.getValue();
    }

    public HorizontalLayout DeleteShopLine()
    {
        return hl;
    }

    public Checkbox getCheckBox(){
        return checkBox;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setCheckBox(boolean boo) {
        this.checkBox.setValue(boo);
    }

    private void setStatusDB() {
        Statement stmt = null;
        try {
            stmt = Application.getConnection().createStatement();
            stmt.executeQuery("update shoppinglists set status = " + !this.status + " where id = " + this.id);
        } catch (SQLException ex) {
            if (!ex.getSQLState().equals("02000"))
                ex.printStackTrace();
        }
    }

    public String getColor() {
        return color;
    }

}
