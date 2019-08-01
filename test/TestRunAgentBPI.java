/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import module.AgentBpi;

/**
 *
 * @author 003-0823
 */
public class TestRunAgentBPI {

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:S");
        Date dateFrom = dt.parse("2016-03-17 05:00:00:000");
        Thread agentBpi = new Thread(new AgentBpi(dateFrom));
        agentBpi.start();
    };
}
