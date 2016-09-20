package daggerok.data.client;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
public class Client implements Serializable {

    private static final long serialVersionUID = 6122282607226311163L;

    @Id String id;
    @NonNull String clientId;
    @NonNull String resourceId;
    @NonNull String scopes;
    @NonNull String grantTypes;
    @NonNull String authorities;
    String redirectUris;
}
