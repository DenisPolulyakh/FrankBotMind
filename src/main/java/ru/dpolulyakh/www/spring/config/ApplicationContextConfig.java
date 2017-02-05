package ru.dpolulyakh.www.spring.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import ru.dpolulyakh.www.dao.message.MessageDataBaseDAO;
import ru.dpolulyakh.www.dao.message.MessageDataBaseDAOImpl;
import ru.dpolulyakh.www.entity.Message;
import ru.dpolulyakh.www.model.KeyQuestion;
import ru.dpolulyakh.www.model.ValueAnswer;
import ru.dpolulyakh.www.model.MemoryProcessTable;
import ru.dpolulyakh.www.pattern.factory.ProcessorFactory;
import ru.dpolulyakh.www.process.BotProcess;
import ru.dpolulyakh.www.process.MemoryProcessor;
import javax.sql.DataSource;

/**
 * @author Denis Polulyakh
 *         15.01.2017.
 */
@Configuration
@ComponentScan("ru.dpolulyakh.www")
@EnableTransactionManagement
public class ApplicationContextConfig {

    @Bean(name = "viewResolver")
    public InternalResourceViewResolver getViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/view/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Bean(name = "dataSource")
    public DataSource getDataSource() {
        /*BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/bot");
        dataSource.setUsername("root");
        dataSource.setPassword("root");

        return dataSource;*/

        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        String username = System.getenv("JDBC_DATABASE_USERNAME");
        String password = System.getenv("JDBC_DATABASE_PASSWORD");

        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(dbUrl);
        basicDataSource.setUsername(username);
        basicDataSource.setPassword(password);

        return basicDataSource;


    }

    @Bean(name = "message")
    public Message getMessage() {
        return new Message();
    }

    @Bean(name = "botProcess")
    public BotProcess getBotProcess() {
        return new BotProcess();
    }

    @Bean(name = "ProcessorFactory")
    public ProcessorFactory getProcessorFactory() {
        return new ProcessorFactory();
    }

    @Bean(name = "memoryProcessor")
    public MemoryProcessor getMemoryProcessor() {
        return new MemoryProcessor();
    }

    //    @Bean(name = "memoryProcessor")
//    public MemoryProcessor getMemoryProcessor() {
//        return new MemoryProcessor();
//    }
//    @Bean(name = "courseProcessor")
//    public CurrencyProcessor getCourseProcessor() {
//        return new CurrencyProcessor();
//    }
//    @Bean(name="phraseProcessor")
//    public PhraseProcessor gePhraseProcessor(){ return new PhraseProcessor();}
//    @Bean(name = "wordExpression")
//    public WordExpression getWordCourseExpression() {
//        return new WordExpression();
//    }
//    @Bean(name = "currencyExpression")
//    public CodeOrNameCurrencyExpression getCodeOrNameCurrencyExpression() {
//        return new CodeOrNameCurrencyExpression();
//    }
//
//    @Bean(name = "dateExpression")
//    public DateExpression getDateExpression() {
//        return new DateExpression();
//    }
    /*
    @Bean(name = "codeCurrency")
    public KeyPhrase getCodeCurrency() {
        return new KeyPhrase();
    }
    @Bean(name = "variantsNameCurrency")
    public ValueAnswer getVariantsNameCurrency() {
        return new ValueAnswer();
    }
*/
    @Autowired
    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory(DataSource dataSource) {

        LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);

        sessionBuilder.addAnnotatedClasses(KeyQuestion.class, ValueAnswer.class, MemoryProcessTable.class);

        sessionBuilder.setProperty("hibernate.hbm2ddl.auto", "create");
        sessionBuilder.setProperty("hibernate.show_sql", "true");
        sessionBuilder.setProperty("hibernate.format_sql", "true");
        //sessionBuilder.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
       // sessionBuilder.setProperty("hibernate.jdbc.use_streams_for_binary","true");
        sessionBuilder.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        sessionBuilder.setProperty("hibernate.enable_lazy_load_no_trans", "true");
        return sessionBuilder.buildSessionFactory();
    }

    @Autowired
    @Bean(name = "messageDataBaseDAO")
    public MessageDataBaseDAO getCurrencySemanticDAO(SessionFactory sessionFactory) {
        return new MessageDataBaseDAOImpl(sessionFactory);
    }

    @Autowired
    @Bean(name = "transactionManager")
    public HibernateTransactionManager getTransactionManager(
            SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager(
                sessionFactory);

        return transactionManager;
    }
}
