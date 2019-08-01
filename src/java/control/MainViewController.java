/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.CustomTable;
import dao.controllers.ContentFileJpaController;
import dao.controllers.ParamJpaController;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import models.pzfiles.ContentFile;
import module.AgentBpi;
import verifier.SecondarilyVerificationResult;
import verifier.PrimarilyVerificationResult;
import verifier.VerifyApp;

/**
 *
 * @author 003-0823
 */
public class MainViewController {

    private static final ParamJpaController pjc = new ParamJpaController();
    private static final ContentFileJpaController cfjc = new ContentFileJpaController();
    private static final String store_path = pjc.findParam("filestorepath").getDescription();
    private static final int BUFSIZE = 150;

    /**
     * Перепроверка всех сообщений из таблицы CustomTable
     *
     * @param source - Таблица с сообщениями
     */
    public static void checkPack(final CustomTable source) {
        Logger.getLogger(MainViewController.class.getName()).log(Level.INFO, "Начало перепроверки файлов");
        String checkxmlpath = pjc.findParam("checkxmlpath").getDescription();
        String sysnameAS = pjc.findParam("sysnameAS").getDescription();
        String userAS = pjc.findParam("userAS").getDescription();
        String passwordAS = pjc.findParam("passwordAS").getDescription();
        VerifyApp va = new VerifyApp(checkxmlpath, sysnameAS, userAS, passwordAS);
        List<ContentFile> buffer = new ArrayList<>();//Буфер для сущностей проходящих проверку
        for (Iterator j = source.getItemIds().iterator(); j.hasNext();) {
            Long itemId = (Long) j.next();
            Long id = (Long) source.getItem(itemId).getItemProperty("id").getValue();
            ContentFile content = cfjc.findContentFile(id);
            int index = buffer.size();
            buffer.add(index, content);
            check(index, content, va);
            if (buffer.size() >= BUFSIZE) {
                updateBuffer(buffer, va);
            }
        }
        updateBuffer(buffer, va);
        Logger.getLogger(MainViewController.class.getName()).log(Level.INFO, "Конец перепроверки файлов");
    }

    /**
     * Перепроверка одного сообщения из таблицы CustomTable с ид: itemId
     *
     * @param source - Таблица с сообщениями
     * @param itemId - ид проверяемого сообщения
     * @return Сущность ContentFile для заполнения результатов проверки
     * вызывающей функцией
     */
    public static ContentFile checkOne(final CustomTable source, Long itemId) {
        String checkxmlpath = pjc.findParam("checkxmlpath").getDescription();
        String sysnameAS = pjc.findParam("sysnameAS").getDescription();
        String userAS = pjc.findParam("userAS").getDescription();
        String passwordAS = pjc.findParam("passwordAS").getDescription();
        VerifyApp va = new VerifyApp(checkxmlpath, sysnameAS, userAS, passwordAS);
        Long id = (Long) source.getItem(itemId).getItemProperty("id").getValue();
        ContentFile content = cfjc.findContentFile(id);
        check(0, content, va);
        source.getItem(itemId).getItemProperty("chkFio").setValue(content.getChkFio());
        source.getItem(itemId).getItemProperty("chkEp").setValue(content.getChkEp());
        source.getItem(itemId).getItemProperty("chkSkpep").setValue(content.getChkSkpep());
        if (!content.getChkConas().equals("Успешно")
                || !content.getChkXml().equals("Успешно")) {
            List<SecondarilyVerificationResult> secondarilyVerificationResults = va.getSecondarilyVerificationResults();
            SecondarilyVerificationResult secondarilyVerificationResult = secondarilyVerificationResults.get(0);
            source.getItem(itemId).getItemProperty("chkConas").setValue(secondarilyVerificationResult.getConasResult());
            source.getItem(itemId).getItemProperty("chkXml").setValue(secondarilyVerificationResult.getCheckXMLResult());
            content.setChkConas(secondarilyVerificationResult.getConasResult());
            content.setChkXml(secondarilyVerificationResult.getCheckXMLResult());
        }
        //Определяем общий итог проверки
        if (content.getChkFio().equals("Успешно")
                && content.getChkSkpep().equals("Успешно")
                && content.getChkEp().equals("Успешно")
                && content.getChkConas().equals("Успешно")
                && content.getChkXml().equals("Успешно")) {
            source.getItem(itemId).getItemProperty("Результат проверок").setValue("Успешно");
            content.setCommonChk("Успешно");
        } else {
            source.getItem(itemId).getItemProperty("Результат проверок").setValue("Ошибка");
            content.setCommonChk("Ошибка");
        }
        Logger.getLogger(AgentBpi.class.getName()).log(Level.INFO, "Сохранение в базу данных " + id);
        try {
            cfjc.edit(content);
        } catch (Exception ex) {
            Logger.getLogger(AgentBpi.class.getName()).log(Level.ERROR, null, ex);
        }
        return content;
    }

    /**
     * Непосредственная функция вызова проверки через специальный класс
     * VerifyApp
     */
    private static void check(int index, ContentFile content, VerifyApp verifyApp) {
        try {
            String xmlPath = content.getXmlPath();
            String p7sPath = content.getP7sPath();
            String xmlName = content.getFileName();
            byte[] xml;
            byte[] p7s;
            try (InputStream isXml = new FileInputStream(new File(store_path + "\\" + xmlPath));
                    InputStream isP7s = new FileInputStream(new File(store_path + "\\" + p7sPath));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
                int count;
                byte[] b = new byte[1024];
                while ((count = isXml.read(b, 0, 1024)) != -1) {
                    baos.write(b, 0, count);
                }
                baos.flush();
                xml = baos.toByteArray();
                baos.reset();
                while ((count = isP7s.read(b, 0, 1024)) != -1) {
                    baos.write(b, 0, count);
                }
                baos.flush();
                p7s = baos.toByteArray();
            } catch (Exception e) {
                throw e;
            }
            PrimarilyVerificationResult primarilyVerificationResult = verifyApp.primarilyVerify(xml, p7s);
            content.setChkException(primarilyVerificationResult.getVerifyException());
            content.setChkFio(primarilyVerificationResult.getVerifyFIO());
            content.setChkEp(primarilyVerificationResult.getVerifySignature());
            content.setChkSkpep(primarilyVerificationResult.getVerifyCertificate());
            if (!content.getChkConas().equals("Успешно")
                    || !content.getChkXml().equals("Успешно")) {
                verifyApp.secondarilyVerify(index, xml, xmlName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateBuffer(List<ContentFile> buffer, VerifyApp va) {        
        List<SecondarilyVerificationResult> secondarilyVerificationResults = va.getSecondarilyVerificationResults();
        for (SecondarilyVerificationResult secondarilyVerificationResult : secondarilyVerificationResults) {
            int id = secondarilyVerificationResult.getId();
            ContentFile content = buffer.get(id);
            content.setChkConas(secondarilyVerificationResult.getConasResult());
            content.setChkXml(secondarilyVerificationResult.getCheckXMLResult());            
        }
        for (ContentFile content : buffer) {
            //Определяем общий итог проверки
            if (content.getChkFio().equals("Успешно")
                    && content.getChkSkpep().equals("Успешно")
                    && content.getChkEp().equals("Успешно")
                    && content.getChkConas().equals("Успешно")
                    && content.getChkXml().equals("Успешно")) {
                content.setCommonChk("Успешно");
            } else {
                content.setCommonChk("Ошибка");
            }
            Logger.getLogger(AgentBpi.class.getName()).log(Level.INFO, "Сохранение в базу данных ");
            try {
                cfjc.edit(content);
            } catch (Exception ex) {
                Logger.getLogger(AgentBpi.class.getName()).log(Level.ERROR, null, ex);
            }
        }
        buffer.clear();
    }
}
