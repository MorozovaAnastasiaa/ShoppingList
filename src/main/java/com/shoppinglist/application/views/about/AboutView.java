package com.shoppinglist.application.views.about;

import com.shoppinglist.application.views.MainLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("About")
@Route(value = "about", layout = MainLayout.class)
public class AboutView extends VerticalLayout {

    public AboutView() {
        setSpacing(false);

        Image img = new Image("images/shoppinglist.png", "placeholder plant");
        img.setWidth("200px");
        add(img);

        add(new H2("Список покупок"));
        add(new Paragraph("Разработчик: Морозова Анастасия 🤗"));
        add(new Paragraph("email: nastemorozovoi@gmail.com"));

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

}
