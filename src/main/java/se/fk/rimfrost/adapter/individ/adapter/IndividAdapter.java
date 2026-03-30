package se.fk.rimfrost.adapter.individ.adapter;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import se.fk.github.jaxrsclientfactory.JaxrsClientFactory;
import se.fk.github.jaxrsclientfactory.JaxrsClientOptionsBuilders;
import se.fk.rimfrost.adapter.individ.model.Individ;
import se.fk.rimfrost.api.individ.jaxrsspec.controllers.generatedsource.IndividControllerApi;

import java.util.UUID;

@SuppressWarnings("unused")
@ApplicationScoped
public class IndividAdapter
{
   @ConfigProperty(name = "individ.api.base-url")
   String individBaseUrl;

   @Inject
   IndividMapper mapper;

   private IndividControllerApi individClient;

   @PostConstruct
   void init()
   {
      this.individClient = new JaxrsClientFactory()
            .create(JaxrsClientOptionsBuilders.createClient(individBaseUrl, IndividControllerApi.class)
                  .build());
   }

   public Individ getIndivid(UUID individId)
   {
      try
      {
         var apiResponse = individClient.individIdGet(individId);
         return mapper.toIndivid(apiResponse);
      }
      catch (WebApplicationException ex)
      {
         if (ex.getResponse().getStatus() == 404)
         {
            // return null or an empty response instead of throwing
            return null;
         }
         throw ex; // rethrow other exceptions
      }
   }
}
