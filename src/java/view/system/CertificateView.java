/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.system;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import dao.controllers.ParamJpaController;
import module.AgentBpi;
import module.CertCRLUpdater;

/**
 *
 * @author 003-0823
 */
public class CertificateView extends VerticalLayout implements View {

    ParamJpaController pjc = new ParamJpaController();

    public CertificateView() {
        setSizeFull();
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        Button openCAstoreButton = new Button("Обновить сертификаты");        
        mainLayout.addComponent(openCAstoreButton);
        final TextArea area = new TextArea("Окно вывода");
        area.setSizeFull();
        openCAstoreButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                AgentBpi ab = new AgentBpi();
                ab.updateCertAndCRL();
                CertCRLUpdater ccu = new CertCRLUpdater();
                String list = ccu.listCertificate();
                area.setValue(list);
            }
        }
        );
        mainLayout.addComponent(area);
        mainLayout.setExpandRatio(area, 1);
        addComponent(mainLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        /*this.removeAllComponents();
         Table table = new Table("Сертификаты");
         table.setSizeFull();
         table.addContainerProperty("Название", String.class, null);
         table.addContainerProperty("Статус",  String.class, null);
         Date signingTime = Calendar.getInstance().getTime();
         File certDir = new File(pjc.findParam("certpath").getDescription());
         File[] listOfCertFile = certDir.listFiles();
         int i=0;
         for (File tempFile : listOfCertFile) {
         i++;
         String filename = tempFile.getName();
         if (filename.length() < 4) {
         continue;
         }
         if (filename.substring(filename.length() - 4, filename.length()).toLowerCase().equals(".cer")) {
         try {
         RandomAccessFile raf;
         raf = new RandomAccessFile(tempFile, "r");
         byte[] cert = new byte[(int) raf.length()];
         raf.read(cert);
         raf.close();
         ByteArrayInputStream bais = new ByteArrayInputStream(cert);
         CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
         X509Certificate x509 = (X509Certificate) certFactory.generateCertificate(bais);
         X500Name x500name = new X500Name(x509.getSubjectX500Principal().getName());
         RDN cn = x500name.getRDNs(BCStyle.CN)[0];
         VerifyCertificate vc = new VerifyCertificate();
         long signingTimeFormatted = (signingTime.getTime() + 11644473600000L) * 10000L;
         table.addItem(new Object[]{IETFUtils.valueToString(cn.getFirst().getValue()), VerifyCertificate.translateErrorMessage(vc.verify(cert, signingTimeFormatted))},i);
         } catch (Exception ex) {
         Logger.getLogger(CertificateView.class.getName()).log(Level.SEVERE, null, ex);
         }
         }
         }
         mainLayout.addComponent(table);
         mainLayout.setExpandRatio(table, 1);*/
    }
}
