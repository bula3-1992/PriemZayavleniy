package control.generatedlink;

import com.vaadin.data.Item;
import com.vaadin.ui.CustomTable;
import dao.controllers.ParamJpaController;
import java.io.File;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author 003-0823
 */
public class DownloadLinkColumn implements CustomTable.ColumnGenerator {

    private final String field;
    private final ParamJpaController pjc = new ParamJpaController();
    private final String path;

    public DownloadLinkColumn(String field, String path) {
        this.field = field;
        this.path = path;
    }

    @Override
    public Object generateCell(CustomTable source, Object itemId, Object columnId) {
        Item contentFile = source.getItem(itemId);
        if (contentFile != null) {
            if (field.equals("xml_path")) {
                File file = new File(path + "\\" + contentFile.getItemProperty(field).getValue());
                if (file.exists()) {
                    final OnDemandDownloadButton downloadButton = new OnDemandDownloadButton(contentFile.getItemProperty(field).getValue().toString(), file);
                    downloadButton.setCaption("Скачать");
                    OnDemandFileDownloader fileDownloader = new OnDemandFileDownloader(downloadButton);
                    fileDownloader.extend(downloadButton);

                    return downloadButton;
                }
            }
            if (field.equals("p7s_path")) {
                File file = new File(path + "\\" + contentFile.getItemProperty(field).getValue());
                if (file.exists()) {
                    final OnDemandDownloadButton downloadButton = new OnDemandDownloadButton(contentFile.getItemProperty(field).getValue().toString(), file);
                    downloadButton.setCaption("Скачать");
                    OnDemandFileDownloader fileDownloader = new OnDemandFileDownloader(downloadButton);
                    fileDownloader.extend(downloadButton);

                    return downloadButton;
//                } else {
//                    String temp = contentFile.getItemProperty(field).getValue().toString();
//                    temp = StringUtils.substringBeforeLast(temp, ".") + ".XML.p7s";
//                    file = new File(path + "\\" + temp);
//                    if (file.exists()) {
//                        final OnDemandDownloadButton downloadButton = new OnDemandDownloadButton(contentFile.getItemProperty(field).getValue().toString(), file);
//                        downloadButton.setCaption("Скачать");
//                        OnDemandFileDownloader fileDownloader = new OnDemandFileDownloader(downloadButton);
//                        fileDownloader.extend(downloadButton);
//
//                        return downloadButton;
//                    } else {
//                        temp = contentFile.getItemProperty(field).getValue().toString();
//                        temp = StringUtils.substringBeforeLast(temp, ".") + ".xml.p7s";
//                        file = new File(path + "\\" + temp);
//                        if (file.exists()) {
//                            final OnDemandDownloadButton downloadButton = new OnDemandDownloadButton(contentFile.getItemProperty(field).getValue().toString(), file);
//                            downloadButton.setCaption("Скачать");
//                            OnDemandFileDownloader fileDownloader = new OnDemandFileDownloader(downloadButton);
//                            fileDownloader.extend(downloadButton);
//
//                            return downloadButton;
//                        } else {
//                            temp = contentFile.getItemProperty(field).getValue().toString();
//                            temp = temp.replace(".xml", ".p7s");
//                            file = new File(path + "\\" + temp);
//                            if (file.exists()) {
//                                final OnDemandDownloadButton downloadButton = new OnDemandDownloadButton(contentFile.getItemProperty(field).getValue().toString(), file);
//                                downloadButton.setCaption("Скачать");
//                                OnDemandFileDownloader fileDownloader = new OnDemandFileDownloader(downloadButton);
//                                fileDownloader.extend(downloadButton);
//
//                                return downloadButton;
//                            }
//                        }
//                    }
                }
            }
        }

        return null;
    }
}
