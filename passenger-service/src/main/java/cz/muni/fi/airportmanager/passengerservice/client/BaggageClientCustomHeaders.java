package cz.muni.fi.airportmanager.passengerservice.client;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;

import java.util.Base64;

@ApplicationScoped
public class BaggageClientCustomHeaders implements ClientHeadersFactory {

    @ConfigProperty(name = "baggage-service.rest.username")
    String username;

    @ConfigProperty(name = "baggage-service.rest.password")
    String password;


    @Override
    public MultivaluedMap<String, String> update(MultivaluedMap<String, String> incomingHeaders, MultivaluedMap<String, String> clientOutgoingHeaders) {
//        TODO return a new MultivaluedMap with the Authorization header basic header
//        TODO use Base64.getEncoder().encodeToString to encode the username and password separeted by a colon "username:password"
        MultivaluedMap<String, String> result = new MultivaluedHashMap<>();
        String authHeaderValue = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
        result.add("Authorization", authHeaderValue);
        return result;
    }
}
