/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import dao.controllers.NpfJpaController;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.persistence.EntityManager;
import models.pzfiles.ContentFile;
import models.pzfiles.Filestore;
import models.pzfiles.Npf;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author 003-0818
 */
public class ReportGenerator {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        NpfJpaController njc = new NpfJpaController();
        ContentFile cf = new ContentFile ();
        cf.setNpfName("ТЕСТ");
        cf.setNpfInn("ТЕСТ");
        cf.setNpfKpp("ТЕСТ");
        Npf newNpf = new Npf();
                        newNpf.setName(cf.getNpfName());
                        newNpf.setInn(cf.getNpfInn());
                        newNpf.setKpp(cf.getNpfKpp());
                        newNpf.setType(1);
                        njc.create(newNpf);
    }

}
/*


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.persistence.EntityManager;
import models.pzfiles.Filestore;
import report.QueryGenerate;


public class ReportGenerator {

    
    private static Connection connectionWF;
    private static Statement stmt;
    private static ResultSet rs;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String fio = "КОЛЕСНИК АНАТОЛИЙ ПЕТРОВИЧ";
        String snils = "";
        String uc = "";
        String npf = "";
        String file = "";
        Class.forName("com.ibm.db2.jcc.DB2Driver");
        connectionWF = DriverManager.getConnection("jdbc:db2://10.3.30.82:50000/PZ_FILES", "db2admin", "db2admin");
        String query = "SELECT DB2ADMIN.CONTENT_FILE.SNILS, "
                + "DB2ADMIN.CONTENT_FILE.FIO, "
                + "DB2ADMIN.CONTENT_FILE.BIRTH_DATE, "
                + "DB2ADMIN.CONTENT_FILE.FILE_NAME, "
                + "(SELECT NAME FROM DB2ADMIN.CLIENT WHERE PF_NOM = DB2ADMIN.CONTENT_FILE.REG_NUM), "
                + "DB2ADMIN.CONTENT_FILE.NPF_NAME, "
                + "DB2ADMIN.CONTENT_FILE.COMPL_DATE, "
                + "DB2ADMIN.CONTENT_FILE.DOC_TYPE "
                + "FROM "
                + "DB2ADMIN.FILESTORE INNER JOIN DB2ADMIN.CONTENT_FILE ON DB2ADMIN.CONTENT_FILE.ID = DB2ADMIN.FILESTORE.CONTENT_ID ";
        String query_by_fio = "DB2ADMIN.CONTENT_FILE.FIO = ";
        String query_by_snils = "DB2ADMIN.CONTENT_FILE.SNILS = ";
        String query_by_uc = "(SELECT NAME FROM DB2ADMIN.CLIENT WHERE PF_NOM = DB2ADMIN.CONTENT_FILE.REG_NUM) = ";
        String query_by_npf = "DB2ADMIN.CONTENT_FILE.NPF_NAME = ";
        String query_by_file = "DB2ADMIN.CONTENT_FILE.FILE_NAME = ";
        if (!fio.equals("") || !snils.equals("") || !uc.equals("") || !npf.equals("") || !file.equals("")) {
            query += "WHERE ";
        }
        boolean first = true;
        if (!fio.equals("")) {
            query += query_by_fio + "'" + fio + "' ";
            first = false;
        }
        if (!snils.equals("")) {
            if (first) {
                query += query_by_snils + "'" + snils + "' ";
                first = false;
            } else {
                query += "AND " + query_by_snils + "'" + snils + "' ";
            }
        }
        if (!uc.equals("")) {
            if (first) {
                query += query_by_uc + "'" + uc + "' ";
                first = false;
            } else {
                query += "AND " + query_by_uc + "'" + uc + "' ";
            }
        }

        if (!npf.equals("")) {
            if (first) {
                query += query_by_npf + "'" + npf + "' ";
                first = false;
            } else {
                query += "AND " + query_by_npf + "'" + npf + "' ";
            }
        }
        if (!file.equals("")) {
            if (first) {
                query += query_by_file + "'" + file + "' ";
                first = false;
            } else {
                query += "AND " + query_by_file + "'" + file + "' ";
            }
        }
        query += "ORDER BY DB2ADMIN.CONTENT_FILE.SNILS, DB2ADMIN.CONTENT_FILE.COMPL_DATE";

        System.out.println(query);
        stmt = connectionWF.createStatement();
        rs = stmt.executeQuery(query);
        List<Filestore> fs = (List<Filestore>) stmt.executeQuery(query);
        System.out.println(fs);
        rs.close();
        stmt.close();
        connectionWF.close();
    }

}

*/
