package ru.dpolulyakh.www.dao.message;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcMessageBotDAO implements MessageBotDAO
{
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public void insert(MessageBotStorage messageBot){

        String sql = "INSERT INTO MESSAGE_BOT_STORAGE " +
                "(QUESTION, ANSWER, COMMAND) VALUES (?, ?, ?)";
        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, messageBot.getQuestion());
            ps.setString(2, messageBot.getAnswer());
            ps.setString(3, messageBot.getCommand());
            ps.executeUpdate();
            ps.close();

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

    @Override
    public MessageBotStorage findByQuestion(String question) {
        String sql = "SELECT * FROM MESSAGE_BOT_STORAGE WHERE QUESTION = ?";
        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, question);
            MessageBotStorage messageBot = null;
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                messageBot = new MessageBotStorage(
                        rs.getString("QUESTION"),
                        rs.getString("ANSWER"),
                        rs.getString("COMMAND")

                );
            }
            rs.close();
            ps.close();
            return messageBot;
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



