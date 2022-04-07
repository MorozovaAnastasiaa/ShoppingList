package com.shoppinglist.application.views.shoppinglist;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class ShopListCommand {
    HorizontalLayout hl = new HorizontalLayout();
    Button add = new Button(new Icon(VaadinIcon.PLUS));

    public ShopListCommand() {
        hl.add(add);
    }

    public HorizontalLayout getPanel(){
        return hl;
    }

}
