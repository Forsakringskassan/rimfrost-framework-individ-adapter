package se.fk.rimfrost.adapter.individ.adapter;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.component.QuarkusComponentTest;
import java.util.UUID;

import jakarta.ws.rs.WebApplicationException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import se.fk.rimfrost.adapter.individ.model.ImmutableIndivid;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

@QuarkusComponentTest(value =
{
      IndividMapper.class
})
public class IndividAdapterTest
{
   private static WireMockServer server;

   @BeforeAll
   public static void setup()
   {
      server = new WireMockServer(
            options()
                  .dynamicPort()
                  .usingFilesUnderDirectory("src/test/resources"));
      server.start();

      System.setProperty("individ.api.base-url", server.baseUrl());
   }

   @AfterAll
   public static void teardown()
   {
      if (server != null)
      {
         server.stop();
      }
   }

   @Test
   void testGetIndivid200(IndividAdapter individAdapter)
   {
      UUID id = UUID.fromString("ed7a3aba-7ba7-4639-b92f-87a8bb26989b");
      var expectedResponse = ImmutableIndivid.builder()
            .id(id)
            .typ("Pnr")
            .varde("19990101-9999")
            .build();

      var response = individAdapter.getIndivid(id);
      assertEquals(expectedResponse, response);
   }

   @Test
   void testGetIndivid404(IndividAdapter individAdapter)
   {
      UUID id = UUID.fromString("0f7f6cd3-4a2a-4244-890e-a3020e08a579");
      var response = individAdapter.getIndivid(id);
      assertNull(response);
   }

   @Test
   void testGetIndivid500(IndividAdapter individAdapter)
   {
      UUID id = UUID.fromString("c4cf4609-fbb1-44cf-915f-13e716eee82d");

      try
      {
         var response = individAdapter.getIndivid(id);
         fail();
      }
      catch (Exception e)
      {
         assertInstanceOf(WebApplicationException.class, e);
      }
   }
}
