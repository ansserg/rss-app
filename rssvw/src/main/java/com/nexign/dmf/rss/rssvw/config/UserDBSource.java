package com.nexign.dmf.rss.rssvw.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.nexign.dmf.rss.rssvw.model.User;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.SQLStateSQLExceptionTranslator;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import javax.ws.rs.NotFoundException;
import java.beans.PropertyVetoException;
import java.sql.*;
import java.util.Properties;

@Data
@Slf4j
public class UserDBSource {
    private User user;
    private NamedParameterJdbcTemplate jdbc;
    private ComboPooledDataSource dataSource;

    public UserDBSource() {
    }

    public UserDBSource(String userName, String userPass, DBConnectConfig cfg) {
        this.user = new User(userName, userPass);
        jdbc = new NamedParameterJdbcTemplate(dataSource(cfg));
    }
    private DataSource dataSource(DBConnectConfig cfg) {
        dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass(cfg.getDriver());
            dataSource.setJdbcUrl(cfg.getUrl());
            dataSource.setUser(user.getName());
            dataSource.setPassword(user.getPass());

            dataSource.setAcquireIncrement(cfg.getAcquireIncrement());
            dataSource.setMaxIdleTime(cfg.getMaxIdleTime());
            dataSource.setInitialPoolSize(cfg.getInitialPoolSize());
            dataSource.setMinPoolSize(cfg.getMinPoolSize());
            dataSource.setMaxPoolSize(cfg.getMaxPoolSize());
            dataSource.setIdleConnectionTestPeriod(60); //sec
            dataSource.setUnreturnedConnectionTimeout(cfg.getUnreturnedConnectionTimeout());
            dataSource.setCheckoutTimeout(cfg.getCheckoutTimeout());
            dataSource.setPreferredTestQuery("select 1 from dual");
        } catch (PropertyVetoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "username or password incorrect");
        }
        try {
            Connection c = dataSource.getConnection();
            c.close();
        } catch (SQLException e) {
            log.info("\ndataSource:close");
            dataSource.close();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "username or password incorrect");
        }
        return dataSource;
    }

    public int changePasswd(String user,String oldPass,String newPass,DBConnectConfig cfg) {
        log.info("changePasswd:start");
        Properties prop=new Properties();
        Connection con=null;
        Statement stm=null;
        prop.put("user",user);
        prop.put("password",oldPass);
        prop.put("newPassword",newPass);
        try {
            con=DriverManager.getConnection(cfg.getUrl(),prop);
            stm =con.createStatement();
            stm.executeUpdate("alter user "+user+" identified by "+newPass
                    +" replace "+oldPass);
            log.info("changePassword:"+"successful change password");
            con.close();
        } catch(SQLException ex) {
            if(ex.getErrorCode()==28001) {
                log.info("SQLException-28001");
                throw new ResponseStatusException(HttpStatus.LOCKED, ex.getMessage());
            } else {
                log.info("SQLException:"+ex.getErrorCode()+","+ex.getMessage());
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
            }
        }
        return 0;
    }
    public void closeDataSource() {
        this.jdbc=null;
        this.dataSource.close();
    }
}
