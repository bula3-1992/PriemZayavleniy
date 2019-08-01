///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package control.generatedlink;
//
//import com.vaadin.ui.Button;
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.InputStream;
//import utils.CommonUtils;
//
///**
// *
// * @author 003-0823
// */
//public class OnDemandDownloadButton extends Button implements OnDemandFileDownloader.OnDemandStreamResource {
//
//    private String filename = null;
//    byte[] blob = null;
//    File file;
//
//    OnDemandDownloadButton(String filename, byte[] blob) {
//        this.filename = filename;
//        this.blob = blob;
//    }
//    OnDemandDownloadButton(String filename, File file) {
//        this.filename = file.getName();
//        this.file = file;
//    }
//
//    @Override
//    public String getFilename() {
//        return filename;
//    }
//
//    @Override
//    public InputStream getStream() {
//        try {
////            byte[] fileBlob = CommonUtils.getFileFromByteArray(blob, filename);
////            ByteArrayOutputStream bos = new ByteArrayOutputStream();
////            bos.write(fileBlob);
//            return new FileInputStream("Скачать");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//}

package control.generatedlink;

import com.vaadin.ui.Button;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 *
 * @author 003-0823
 */
public class OnDemandDownloadButton extends Button implements OnDemandFileDownloader.OnDemandStreamResource {

    private String filename = null;
    File file;

    public OnDemandDownloadButton(String filename, File file) {
        this.filename = file.getName();
        this.file = file;
    }

    @Override
    public String getFilename() {
        return filename;
    }

    @Override
    public InputStream getStream() {
        try {
//            byte[] fileBlob = CommonUtils.getFileFromByteArray(blob, filename);
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            bos.write(fileBlob);

            return new FileInputStream(file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
