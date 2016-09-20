package daggerok.config.clientdetails;

import daggerok.data.client.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Repository
@RequiredArgsConstructor
public class ClientDetailsServiceImpl implements ClientDetailsService {

    final ClientRepository clientRepository;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

        val client = clientRepository.findByClientId(clientId)
                .orElseThrow(() -> new NoSuchClientException(format("= client with id '%s' wasn't found =", clientId)));
        val clientDetails = new BaseClientDetails(
                client.getClientId(),
                client.getResourceId(),
                client.getScopes(),
                client.getGrantTypes(),
                client.getAuthorities(),
                client.getRedirectUris()
        );

        clientDetails.setAutoApproveScopes(Stream.of(client.getScopes().split(",")).collect(toList()));
        return clientDetails;
    }
}
