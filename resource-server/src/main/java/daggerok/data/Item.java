package daggerok.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document
@NoArgsConstructor
@Accessors(chain = true)
public class Item implements Serializable {
    private static final long serialVersionUID = -5119307267031500317L;

    @Id String id;
    String content;
}
