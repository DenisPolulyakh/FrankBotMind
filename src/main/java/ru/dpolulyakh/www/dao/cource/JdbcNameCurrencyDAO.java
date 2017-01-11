package ru.dpolulyakh.www.dao.cource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class JdbcNameCurrencyDAO implements NameCurrencyDAO
{
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public CodeNameCurrency load() {
        String sql = "SELECT * FROM CODE_NAME_CURRENCY";
        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            CodeNameCurrency codeNameCurrency = new CodeNameCurrency();
            ArrayList<String> code = new ArrayList<String>();
            ArrayList<String> nameCurrency = new ArrayList<String>();
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
               code.add(rs.getString("CODE"));
               nameCurrency.add(rs.getString("VARIANTS_NAME"));
            }
            rs.close();
            ps.close();
            codeNameCurrency.setCode(code);
            codeNameCurrency.setNames(nameCurrency);
            return codeNameCurrency;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {}
            }
        }
    }
}



