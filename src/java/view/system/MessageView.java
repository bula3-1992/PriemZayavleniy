/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.system;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomTable;
import com.vaadin.ui.CustomTable.CellStyleGenerator;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;
import control.pagedfiltertable.PagedFilterControlConfig;
import control.pagedfiltertable.PagedFilterTable;
import control.pagedfiltertable.PagedFilterTableButtons;
import dao.factory.Factory;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import javax.persistence.EntityManager;
import view.forms.ItemCard;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import models.pzfiles.Filestore;
import module.AgentBpi;
import view.MainView;

/**
 *
 * @author 003-0823
 */
public class MessageView extends VerticalLayout implements View {

    private PagedFilterTable<IndexedContainer> table;
    private IndexedContainer contentContainer;
    private final HorizontalLayout dateLayout = new HorizontalLayout();
    private DateField bdatesync = new DateField("Дата получения БПИ");
    private DateField edatesync = new DateField();
    private final Button dateButton = new Button("Поиск");
    private final Button recheckButton = new Button("Обработать снова");
    private final HorizontalLayout sizeLayout = new HorizontalLayout();
    private final HorizontalLayout pageLayout = new HorizontalLayout();
    private final Label tsize = new Label();
    private final Button sizeNextButton = new Button("Следующие 1000 >");
    private final Button sizePreviousButton = new Button("< Предыдущие 1000");
    private int maxResults;
    private int firstResult;
    private long DAYSALLOWED = 2;//сколько дней разрешено отправлять на повторную обрабоку

    private final Calendar c = Calendar.getInstance();

    public MessageView() {

        contentContainer = new IndexedContainer();
        contentContainer.addContainerProperty("Время отправления", String.class, "");
        contentContainer.addContainerProperty("Время получения", String.class, "");
        contentContainer.addContainerProperty("От кого", String.class, "");
        contentContainer.addContainerProperty("Кому", String.class, "");
        contentContainer.addContainerProperty("Рег № страхователя в УПФР", String.class, "");
        contentContainer.addContainerProperty("Направление", String.class, "");
        contentContainer.addContainerProperty("Статус", String.class, "");
        contentContainer.addContainerProperty("Примечание", String.class, "");

        Date referenceDate = new Date();
        c.setTime(referenceDate);
        c.add(Calendar.WEEK_OF_YEAR, -1);

        setSizeFull();
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        dateLayout.setWidth("45%");
        dateLayout.addComponent(bdatesync);
        dateLayout.addComponent(edatesync);
        dateLayout.addComponent(dateButton);
        dateLayout.addComponent(recheckButton);
        dateLayout.setComponentAlignment(bdatesync, Alignment.BOTTOM_CENTER);
        dateLayout.setComponentAlignment(edatesync, Alignment.BOTTOM_CENTER);
        dateLayout.setComponentAlignment(dateButton, Alignment.BOTTOM_CENTER);
        dateLayout.setComponentAlignment(recheckButton, Alignment.BOTTOM_CENTER);
        bdatesync.setSizeFull();
        edatesync.setSizeFull();
        bdatesync.setValue(c.getTime());
        edatesync.setValue(referenceDate);
        dateButton.setSizeFull();
        recheckButton.setSizeFull();

        dateButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                contentContainer.removeAllItems();
                EntityManager em = Factory.getInstance().getPzFilesEmf().createEntityManager();
                try {
                    maxResults = 1000;
                    firstResult = 0;

                    CriteriaBuilder cb = em.getCriteriaBuilder();
                    CriteriaQuery cqF = cb.createQuery();
                    Root rF = cqF.from(Filestore.class);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    cqF.where(cb.and(cb.greaterThanOrEqualTo(rF.get("receiveTime"), sdf.format(bdatesync.getValue()) + " 00:00:00.000"),
                            cb.lessThanOrEqualTo(rF.get("receiveTime"), sdf.format(edatesync.getValue()) + " 23:59:59.999")));
                    cqF.orderBy(cb.asc(rF.get("receiveTime")));
                    Query qF = em.createQuery(cqF);
                    qF.setMaxResults(maxResults);
                    qF.setFirstResult(firstResult);
                    queryToContentContainer(qF);

                    tsize.setCaption("Отображено заявлений с учетом фильтров(MAX 1000): " + firstResult + "-" + (firstResult + contentContainer.size() + "  "));
                    table.refresh();
                    table.resetFilters();
                    if (contentContainer.size() >= maxResults) {
                        sizeNextButton.setEnabled(true);
                    } else {
                        sizeNextButton.setEnabled(false);
                    }
                    sizePreviousButton.setEnabled(false);
                } finally {
                    em.close();
                }
            }
        }
        );

        recheckButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Date before = bdatesync.getValue();
                Date after = edatesync.getValue();
                if (AgentBPIService.agentBpi.isAlive()) {
                    Window subWindow = new Window("Сообщение");
                    subWindow.setContent(new Label("В данный момент выполняется загрузка данных"));
                    subWindow.center();
                    UI.getCurrent().addWindow(subWindow);
                } else if (before.after(after)) {
                    Window subWindow = new Window("Сообщение");
                    subWindow.setContent(new Label("Даты в неправильном хронологическом порядке"));
                    subWindow.center();
                    UI.getCurrent().addWindow(subWindow);
                } else if (after.getTime() - before.getTime() > DAYSALLOWED * 86400000) {
                    Window subWindow = new Window("Сообщение");
                    subWindow.setContent(new Label("Выберите рабочий промежуток поменьше (MAX: " + DAYSALLOWED + " дней)"));
                    subWindow.center();
                    UI.getCurrent().addWindow(subWindow);
                } else {
                    AgentBPIService.agentBpi = new Thread(new AgentBpi(before, after));
                    AgentBPIService.agentBpi.start();
                }
            }
        }
        );

       
        table = new PagedFilterTable<>();

        table.setContainerDataSource(contentContainer);

        table.setCellStyleGenerator(
                new CellStyleGenerator() {
                    @Override
                    public String getStyle(CustomTable source, Object itemId, Object propertyId
                    ) {
                        if (propertyId == null) {
                            // Styling for row
                            Item item = table.getItem(itemId);
                            if (item.getItemProperty("Статус").getValue().equals("ошибка")) {
                                return "highlight-red";
                            }
                        }
                        return null;
                    }
                }
        );
        table.setSizeFull();

        table.setSelectable(
                true);
        table.setFilterBarVisible(
                true);
        table.setPageLength(
                25);
        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    ItemCard itemCard = new ItemCard(table, table.getItem(event.getItemId()), event.getItemId());
                    UI.getCurrent().addWindow(itemCard);
                }
            }
        }
        );
        mainLayout.setHeight(
                "97%");
        mainLayout.addComponent(dateLayout);

        mainLayout.addComponent(table);

        mainLayout.addComponent(table.createControls(new PagedFilterControlConfig()));
        mainLayout.addComponent(PagedFilterTableButtons.buildButtons(table));
        mainLayout.setExpandRatio(table,
                1);
        tsize.setCaption(
                "Отображено заявлений с учетом фильтров(MAX 1000):   ");
        sizeNextButton.addListener(
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event
                    ) {
                        contentContainer.removeAllItems();
                        EntityManager em = Factory.getInstance().getPzFilesEmf().createEntityManager();
                        try {
                            firstResult += 1000;
                            CriteriaBuilder cb = em.getCriteriaBuilder();
                            CriteriaQuery cqF = cb.createQuery();
                            Root eF = cqF.from(Filestore.class
                            );
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                            cqF.where(cb.and(cb.greaterThanOrEqualTo(eF.get("receiveTime"), sdf.format(bdatesync.getValue()) + " 00:00:00.000"),
                                            cb.lessThanOrEqualTo(eF.get("receiveTime"), sdf.format(edatesync.getValue()) + " 23:59:59.999")));
                            cqF.orderBy(cb.asc(eF.get("receiveTime")));
                            Query qF = em.createQuery(cqF);

                            qF.setMaxResults(maxResults);

                            qF.setFirstResult(firstResult);

                            queryToContentContainer(qF);

                            tsize.setCaption(
                                    "Отображено заявлений с учетом фильтров(MAX 1000): " + firstResult + "-" + (firstResult + contentContainer.size()));
                            table.refresh();

                            table.resetFilters();

                            sizePreviousButton.setEnabled(
                                    true);
                            if (contentContainer.size() >= maxResults) {
                                sizeNextButton.setEnabled(true);
                            } else {
                                sizeNextButton.setEnabled(false);
                            }
                        } finally {
                            em.close();
                        }
                    }
                }
        );
        sizePreviousButton.addListener(
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event
                    ) {
                        contentContainer.removeAllItems();
                        EntityManager em = Factory.getInstance().getPzFilesEmf().createEntityManager();
                        try {
                            if (firstResult >= 1000) {
                                firstResult -= 1000;
                            }
                            CriteriaBuilder cb = em.getCriteriaBuilder();
                            CriteriaQuery cqF = cb.createQuery();
                            Root eF = cqF.from(Filestore.class
                            );
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                            cqF.where(cb.and(cb.greaterThanOrEqualTo(eF.get("receiveTime"), sdf.format(bdatesync.getValue()) + " 00:00:00.000"),
                                            cb.lessThanOrEqualTo(eF.get("receiveTime"), sdf.format(edatesync.getValue()) + " 23:59:59.999")));
                            cqF.orderBy(cb.asc(eF.get("receiveTime")));
                            Query qF = em.createQuery(cqF);

                            qF.setMaxResults(maxResults);

                            qF.setFirstResult(firstResult);

                            queryToContentContainer(qF);

                            tsize.setCaption(
                                    "Отображено заявлений с учетом фильтров(MAX 1000): " + firstResult + "-" + (firstResult + contentContainer.size()));
                            table.refresh();

                            table.resetFilters();
                            if (firstResult == 0) {
                                sizePreviousButton.setEnabled(false);
                            } else {
                                sizePreviousButton.setEnabled(true);
                            }
                            sizeNextButton.setEnabled(true);
                        } finally {
                            em.close();
                        }
                    }
                }
        );
        sizeNextButton.setStyleName(BaseTheme.BUTTON_LINK);
        sizeNextButton.setEnabled(false);
        sizePreviousButton.setStyleName(BaseTheme.BUTTON_LINK);
        sizePreviousButton.setEnabled(false);
        sizeLayout.addComponent(tsize);
        pageLayout.addComponent(sizePreviousButton);
        pageLayout.addComponent(sizeNextButton);
        pageLayout.setWidth("20%");
        mainLayout.addComponent(sizeLayout);
        mainLayout.addComponent(pageLayout);
        addComponent(mainLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

    private List<Filestore> queryToContentContainer(Query q) {
        List<Filestore> out = q.getResultList();
        for (Filestore filestore : out) {
            Item item = contentContainer.addItem(filestore.getId());
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            if (filestore.getSendTime() != null) {
                item.getItemProperty("Время отправления").setValue(sdf.format(filestore.getSendTime()));
            }
            if (filestore.getReceiveTime() != null) {
                item.getItemProperty("Время получения").setValue(sdf.format(filestore.getReceiveTime()));
            }
            item.getItemProperty("От кого").setValue(filestore.getSenderId().getName());
            item.getItemProperty("Кому").setValue(filestore.getReceiverId().getName());
            item.getItemProperty("Рег № страхователя в УПФР").setValue(filestore.getSenderId().getPfNom());
            item.getItemProperty("Направление").setValue(filestore.getDirection().getDirectionType());
            item.getItemProperty("Статус").setValue(filestore.getStatus().getDescription());
            item.getItemProperty("Примечание").setValue(filestore.getComm());
        }
        return out;
    }
}
