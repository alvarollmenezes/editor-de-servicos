package br.gov.servicos.editor.frontend;

import br.gov.servicos.editor.Main;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class UITest {

    WebDriver driver;

    @Value("${local.server.port}")
    int port;

    String baseUrl;

    @Before
    public void setUp() throws Exception {
        baseUrl = "http://localhost:" + port;

        driver = new HtmlUnitDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @After
    public void tearDown() throws Exception {
        driver.close();
    }

    @Test
    public void deveSeguirSequenciaSimplesDeEdicao() throws Exception {
        driver.get(baseUrl + "/exemplo");
        assertThat(driver.getTitle(), is("Editor de Serviços - Acessar o editor de serviços"));

        driver.findElement(By.id("usuario")).sendKeys("user");
        driver.findElement(By.id("senha")).sendKeys("user");
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        assertThat(driver.getTitle(), is("Editor de Serviços - Principal"));

        driver.findElement(By.id("nome")).sendKeys("Carteira Nacional de Habilitação (CNH)");

        driver.findElement(By.id("salvar")).click();
    }


}
