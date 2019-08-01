/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;
import control.MainViewController;
import control.generatedlink.DownloadLinkColumn;
import control.pagedfiltertable.PagedFilterControlConfig;
import control.pagedfiltertable.PagedFilterTable;
import control.pagedfiltertable.PagedFilterTableButtons;
import dao.controllers.ParamJpaController;
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
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import models.pzfiles.ContentFile;
import models.pzfiles.Filestore;
import static view.Main.mainMenu;

/**
 *
 * @author 003-0823
 */
public class MainView extends VerticalLayout implements View {

    private PagedFilterTable<IndexedContainer> table;
    private IndexedContainer contentContainer;
    private final ParamJpaController pjc = new ParamJpaController();
    private final String store_address = pjc.findParam("filestorepath").getDescription();
    private final HorizontalLayout dateLayout = new HorizontalLayout();
    private DateField bdate = new DateField("Дата заполнения заявления");
    private DateField edate = new DateField();
    private DateField bdatesync = new DateField("Дата поступления заявления в ПК БПИ");
    private DateField edatesync = new DateField();
    private TextField buffersize = new TextField("Размер буфера");
    private final Button dateButton = new Button("Поиск");
    private final Button recheckButton = new Button("Обработать снова");
    private final Button exitButton = new Button("Выйти");
    private final HorizontalLayout sizeLayout = new HorizontalLayout();
    private final HorizontalLayout pageLayout = new HorizontalLayout();
    private final Label tsize = new Label();
    private final Button sizeNextButton = new Button("Следующие заявления >");
    private final Button sizePreviousButton = new Button("< Предыдущие следующие");

    private int maxResults;
    private int firstResult;
    Label userLabel = new Label();
    private final Calendar c = Calendar.getInstance();

    public MainView() {

        contentContainer = new IndexedContainer();
        contentContainer.addContainerProperty("Снилс", String.class, "");
        contentContainer.addContainerProperty("Фамилия", String.class, "");
        contentContainer.addContainerProperty("Имя", String.class, "");
        contentContainer.addContainerProperty("Отчество", String.class, "");
        contentContainer.addContainerProperty("УЦ", String.class, "");
        contentContainer.addContainerProperty("НПФ", String.class, "");
        contentContainer.addContainerProperty("Дата регистрации", String.class, "");
        contentContainer.addContainerProperty("Тип заявления", String.class, "");
        contentContainer.addContainerProperty("xml_path", String.class, "");
        contentContainer.addContainerProperty("p7s_path", String.class, "");
        contentContainer.addContainerProperty("birthDate", String.class, "");
        contentContainer.addContainerProperty("npfName", String.class, "");
        contentContainer.addContainerProperty("crPackName", String.class, "");
        contentContainer.addContainerProperty("crPackInn", String.class, "");
        contentContainer.addContainerProperty("complDate", String.class, "");
        contentContainer.addContainerProperty("docType", String.class, "");
        contentContainer.addContainerProperty("Результат проверок", String.class, "");
        contentContainer.addContainerProperty("chkEp", String.class, "");
        contentContainer.addContainerProperty("chkFio", String.class, "");
        contentContainer.addContainerProperty("chkSkpep", String.class, "");
        contentContainer.addContainerProperty("chkConas", String.class, "");
        contentContainer.addContainerProperty("chkXml", String.class, "");
        contentContainer.addContainerProperty("id", Long.class, "");
        contentContainer.addContainerProperty("idMessage", Filestore.class, "");

        Date referenceDate = new Date();
        c.setTime(referenceDate);
        c.add(Calendar.WEEK_OF_YEAR, -1);

        setSizeFull();
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        dateLayout.setWidth("100%");
        dateLayout.addComponent(bdate);
        dateLayout.addComponent(edate);
        dateLayout.addComponent(bdatesync);
        dateLayout.addComponent(edatesync);
        dateLayout.addComponent(buffersize);
        dateLayout.addComponent(dateButton);
        dateLayout.addComponent(recheckButton);
        dateLayout.addComponent(userLabel);
        dateLayout.addComponent(exitButton);

        dateLayout.setComponentAlignment(bdate, Alignment.MIDDLE_LEFT);
        dateLayout.setComponentAlignment(edate, Alignment.BOTTOM_LEFT);
        dateLayout.setComponentAlignment(bdatesync, Alignment.BOTTOM_LEFT);
        dateLayout.setComponentAlignment(edatesync, Alignment.BOTTOM_LEFT);
        dateLayout.setComponentAlignment(dateButton, Alignment.BOTTOM_LEFT);
        dateLayout.setComponentAlignment(recheckButton, Alignment.BOTTOM_LEFT);
        dateLayout.setComponentAlignment(userLabel, Alignment.MIDDLE_RIGHT);
        dateLayout.setComponentAlignment(exitButton, Alignment.MIDDLE_RIGHT);

        bdate.setWidth("50%");
        edate.setWidth("50%");
        bdatesync.setWidth("50%");
        edatesync.setWidth("50%");
        bdate.setValue(c.getTime());
        edate.setValue(referenceDate);
        buffersize.setWidth("50%");
        buffersize.setValue("1000");
        dateButton.setWidth("50%");
        recheckButton.setWidth("100%");
        exitButton.setWidth("50%");
        userLabel.setWidth("60%");
        exitButton.setWidth("50%");
        dateLayout.setSpacing(true);
        dateLayout.setMargin(true);
        exitButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                // getUI().getSession().close();
                getSession().setAttribute("user", null);
                mainMenu.setEnabled(false);
                getUI().getNavigator().navigateTo("LoginView");
            }
        }
        );
        dateButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                contentContainer.removeAllItems();
                EntityManager em = Factory.getInstance().getPzFilesEmf().createEntityManager();
                try {
                    if(buffersize.getValue() == null || buffersize.getValue() == "")
                        buffersize.setValue("1000");
                    maxResults = Integer.parseInt(buffersize.getValue());
                    firstResult = 0;
                    boolean messageIsFound = false;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                    CriteriaBuilder cb = em.getCriteriaBuilder();
                    CriteriaQuery<ContentFile> cq = cb.createQuery(ContentFile.class);
                    Root rC = cq.from(ContentFile.class);
                    Path<Date> receiveTime = rC.get("idMessage").get("receiveTime");
                    if (bdate.getValue() != null && edate.getValue() != null && bdatesync.getValue() != null && edatesync.getValue() != null) {
                        Calendar c = Calendar.getInstance();
                        c.setTime(edatesync.getValue());
                        c.add(Calendar.DATE, 1);
                        c.add(Calendar.MILLISECOND, -1);
                        cq.where(cb.and(cb.greaterThanOrEqualTo(rC.get("complDate"), sdf.format(bdate.getValue())),
                                cb.lessThanOrEqualTo(rC.get("complDate"), sdf.format(edate.getValue())),
                                cb.greaterThanOrEqualTo(receiveTime, bdatesync.getValue()),
                                cb.lessThanOrEqualTo(receiveTime, c.getTime())));
                        messageIsFound = true;
                    }
                    if (bdate.getValue() != null && edate.getValue() != null && bdatesync.getValue() == null && edatesync.getValue() == null) {
                        cq.where(cb.and(cb.greaterThanOrEqualTo(rC.get("complDate"), sdf.format(bdate.getValue())),
                                cb.lessThanOrEqualTo(rC.get("complDate"), sdf.format(edate.getValue()))));
                        messageIsFound = true;
                    }
                    if (bdate.getValue() == null && edate.getValue() == null && bdatesync.getValue() != null && edatesync.getValue() != null) {
                        Calendar c = Calendar.getInstance();
                        c.setTime(edatesync.getValue());
                        c.add(Calendar.DATE, 1);
                        c.add(Calendar.MILLISECOND, -1);
                        cq.where(cb.and(cb.greaterThanOrEqualTo(receiveTime, bdatesync.getValue()),
                                cb.lessThanOrEqualTo(receiveTime, c.getTime())));
                        messageIsFound = true;
                    }
                    if (messageIsFound) {
                        cq.orderBy(cb.asc(rC.get("id")));
                        Query qC = em.createQuery(cq);
                        qC.setMaxResults(maxResults);
                        qC.setFirstResult(firstResult);
                        queryToContentContainer(qC);
                        tsize.setCaption("Отображено заявлений с учетом фильтров(MAX" +maxResults +"): " + firstResult + "-" + (firstResult + contentContainer.size() + "  "));
                        table.refresh();
                        table.resetFilters();
                        if (contentContainer.size() >= maxResults) {
                            sizeNextButton.setEnabled(true);
                        } else {
                            sizeNextButton.setEnabled(false);
                        }
                        sizePreviousButton.setEnabled(false);
                    }
                } finally {
                    em.close();
                }
            }
        }
        );

        
        table = new PagedFilterTable<>();
        table.setContainerDataSource(contentContainer);
        table.setSizeFull();
        table.setSelectable(true);
        table.setFilterBarVisible(true);
        table.setPageLength(25);
        table.addGeneratedColumn("XML", new DownloadLinkColumn("xml_path", store_address));
        table.addGeneratedColumn("P7S", new DownloadLinkColumn("p7s_path", store_address));
        table.setVisibleColumns(new Object[]{"Снилс", "Фамилия", "Имя", "Отчество", "УЦ",
            "НПФ", "Дата регистрации", "Тип заявления", "Результат проверок", "XML", "P7S"});
        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    ItemCard itemCard = new ItemCard(table, table.getItem(event.getItemId()), event.getItemId());
                    UI.getCurrent().addWindow(itemCard);
                }
            }
        });
        mainLayout.setHeight("97%");
        mainLayout.addComponent(dateLayout);
        mainLayout.addComponent(table);
        mainLayout.addComponent(table.createControls(new PagedFilterControlConfig()));
        mainLayout.addComponent(PagedFilterTableButtons.buildButtons(table));
        mainLayout.setExpandRatio(table, 1);
        tsize.setCaption("Отображено заявлений с учетом фильтров(MAX "+maxResults+"):   ");
        sizeNextButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                contentContainer.removeAllItems();
                EntityManager em = Factory.getInstance().getPzFilesEmf().createEntityManager();
                try {
                    firstResult += maxResults;
                    boolean messageIsFound = false;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                    CriteriaBuilder cb = em.getCriteriaBuilder();
                    CriteriaQuery<ContentFile> cq = cb.createQuery(ContentFile.class);
                    Root rC = cq.from(ContentFile.class);
                    Path<Date> receiveTime = rC.get("idMessage").get("receiveTime");
                    if (bdate.getValue() != null && edate.getValue() != null && bdatesync.getValue() != null && edatesync.getValue() != null) {
                        Calendar c = Calendar.getInstance();
                        c.setTime(edatesync.getValue());
                        c.add(Calendar.DATE, 1);
                        c.add(Calendar.MILLISECOND, -1);
                        cq.where(cb.and(cb.greaterThanOrEqualTo(rC.get("complDate"), sdf.format(bdate.getValue())),
                                cb.lessThanOrEqualTo(rC.get("complDate"), sdf.format(edate.getValue())),
                                cb.greaterThanOrEqualTo(receiveTime, bdatesync.getValue()),
                                cb.lessThanOrEqualTo(receiveTime, c.getTime())));
                        messageIsFound = true;
                    }
                    if (bdate.getValue() != null && edate.getValue() != null && bdatesync.getValue() == null && edatesync.getValue() == null) {
                        cq.where(cb.and(cb.greaterThanOrEqualTo(rC.get("complDate"), sdf.format(bdate.getValue())),
                                cb.lessThanOrEqualTo(rC.get("complDate"), sdf.format(edate.getValue()))));
                        messageIsFound = true;
                    }
                    if (bdate.getValue() == null && edate.getValue() == null && bdatesync.getValue() != null && edatesync.getValue() != null) {
                        Calendar c = Calendar.getInstance();
                        c.setTime(edatesync.getValue());
                        c.add(Calendar.DATE, 1);
                        c.add(Calendar.MILLISECOND, -1);
                        cq.where(cb.and(cb.greaterThanOrEqualTo(receiveTime, bdatesync.getValue()),
                                cb.lessThanOrEqualTo(receiveTime, c.getTime())));
                        messageIsFound = true;
                    }
                    if (messageIsFound) {
                        cq.orderBy(cb.asc(rC.get("id")));
                        Query qC = em.createQuery(cq);
                        qC.setMaxResults(maxResults);
                        qC.setFirstResult(firstResult);
                        queryToContentContainer(qC);
                        tsize.setCaption("Отображено заявлений с учетом фильтров(MAX "+ maxResults+"): " + firstResult + "-" + (firstResult + contentContainer.size() + "  "));
                        sizePreviousButton.setEnabled(
                                true);
                        if (contentContainer.size() >= maxResults) {
                            sizeNextButton.setEnabled(true);
                        } else {
                            sizeNextButton.setEnabled(false);
                        }
                    }
                } finally {
                    em.close();
                }
            }
        }
        );
        sizePreviousButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                contentContainer.removeAllItems();
                EntityManager em = Factory.getInstance().getPzFilesEmf().createEntityManager();
                try {
                    if (firstResult >= maxResults) {
                        firstResult -= maxResults;
                    }
                    boolean messageIsFound = false;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                    CriteriaBuilder cb = em.getCriteriaBuilder();
                    CriteriaQuery<ContentFile> cq = cb.createQuery(ContentFile.class);
                    Root rC = cq.from(ContentFile.class);
                    Path<Date> receiveTime = rC.get("idMessage").get("receiveTime");
                    if (bdate.getValue() != null && edate.getValue() != null && bdatesync.getValue() != null && edatesync.getValue() != null) {
                        Calendar c = Calendar.getInstance();
                        c.setTime(edatesync.getValue());
                        c.add(Calendar.DATE, 1);
                        c.add(Calendar.MILLISECOND, -1);
                        cq.where(cb.and(cb.greaterThanOrEqualTo(rC.get("complDate"), sdf.format(bdate.getValue())),
                                cb.lessThanOrEqualTo(rC.get("complDate"), sdf.format(edate.getValue())),
                                cb.greaterThanOrEqualTo(receiveTime, bdatesync.getValue()),
                                cb.lessThanOrEqualTo(receiveTime, c.getTime())));
                        messageIsFound = true;
                    }
                    if (bdate.getValue() != null && edate.getValue() != null && bdatesync.getValue() == null && edatesync.getValue() == null) {
                        cq.where(cb.and(cb.greaterThanOrEqualTo(rC.get("complDate"), sdf.format(bdate.getValue())),
                                cb.lessThanOrEqualTo(rC.get("complDate"), sdf.format(edate.getValue()))));
                        messageIsFound = true;
                    }
                    if (bdate.getValue() == null && edate.getValue() == null && bdatesync.getValue() != null && edatesync.getValue() != null) {
                        Calendar c = Calendar.getInstance();
                        c.setTime(edatesync.getValue());
                        c.add(Calendar.DATE, 1);
                        c.add(Calendar.MILLISECOND, -1);
                        cq.where(cb.and(cb.greaterThanOrEqualTo(receiveTime, bdatesync.getValue()),
                                cb.lessThanOrEqualTo(receiveTime, c.getTime())));
                        messageIsFound = true;
                    }
                    if (messageIsFound) {
                        cq.orderBy(cb.asc(rC.get("id")));
                        Query qC = em.createQuery(cq);
                        qC.setMaxResults(maxResults);
                        qC.setFirstResult(firstResult);
                        queryToContentContainer(qC);
                        tsize.setCaption("Отображено заявлений с учетом фильтров(MAX "+ maxResults +"): " + firstResult + "-" + (firstResult + contentContainer.size() + "  "));
                        table.refresh();
                        table.resetFilters();

                        if (firstResult == 0) {
                            sizePreviousButton.setEnabled(false);
                        } else {
                            sizePreviousButton.setEnabled(true);
                        }
                        sizeNextButton.setEnabled(true);
                    }
                } finally {
                    em.close();
                }
            }
        }
        );
        recheckButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                MainViewController.checkPack(table);
                contentContainer.removeAllItems();
                table.refresh();
                table.resetFilters();
            }
        });
        sizeNextButton.setStyleName(BaseTheme.BUTTON_LINK);
        sizeNextButton.setEnabled(false);
        sizePreviousButton.setStyleName(BaseTheme.BUTTON_LINK);
        sizePreviousButton.setEnabled(false);
        sizeLayout.addComponent(tsize);
        pageLayout.addComponent(sizePreviousButton);
        pageLayout.addComponent(sizeNextButton);
        pageLayout.setWidth("30%");
        mainLayout.addComponent(sizeLayout);
        mainLayout.addComponent(pageLayout);
        addComponent(mainLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

    private void queryToContentContainer(Query q) {
        List<ContentFile> out = q.getResultList();
        for (ContentFile content : out) {
            Item item = contentContainer.addItem(content.getId());
            item.getItemProperty("Снилс").setValue(content.getSnils());
            item.getItemProperty("Фамилия").setValue(content.getSurname());
            item.getItemProperty("Имя").setValue(content.getFirstname());
            item.getItemProperty("Отчество").setValue(content.getSecondname());
            item.getItemProperty("УЦ").setValue(content.getCrPackName());
            item.getItemProperty("НПФ").setValue(content.getNpfName());
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            item.getItemProperty("Дата регистрации").setValue(sdf.format(content.getCrDate()));
            item.getItemProperty("Тип заявления").setValue(content.getDocType());
            item.getItemProperty("xml_path").setValue(content.getXmlPath());
            item.getItemProperty("p7s_path").setValue(content.getP7sPath());
            item.getItemProperty("birthDate").setValue(sdf.format(content.getBirthDate()));
            item.getItemProperty("npfName").setValue(content.getNpfName());
            item.getItemProperty("crPackName").setValue(content.getCrPackName());
            item.getItemProperty("crPackInn").setValue(content.getCrPackInn());
            item.getItemProperty("complDate").setValue(sdf.format(content.getComplDate()));
            item.getItemProperty("docType").setValue(content.getDocType());
            item.getItemProperty("Результат проверок").setValue(content.getCommonChk());
            item.getItemProperty("chkEp").setValue(content.getChkEp());
            item.getItemProperty("chkFio").setValue(content.getChkFio());
            item.getItemProperty("chkSkpep").setValue(content.getChkSkpep());
            item.getItemProperty("chkConas").setValue(content.getChkConas());
            item.getItemProperty("chkXml").setValue(content.getChkXml());
            item.getItemProperty("id").setValue(content.getId());
            item.getItemProperty("idMessage").setValue(content.getIdMessage());
        }
    }

    @Override
    public void attach() {
        super.attach(); // Must call.
        // Now we know who ultimately owns us.
        String userLogin = "";
        if (getSession() == null || getSession().getAttribute("user") == null) {
            userLogin = "Гость";
        } else {
            userLogin = (String) getSession().getAttribute("user");
        }
        userLabel.setValue("Пользователь: " + userLogin);
    }

}
