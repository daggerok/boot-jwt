package daggerok.data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

import static daggerok.data.ItemRestRepository.*;

@RepositoryRestResource
@PreAuthorize(CAN_READ)
public interface ItemRestRepository extends MongoRepository<Item, String> {

    String CAN_READ = "hasAuthority('REST_READ_AUTH')";
    String CAN_WRITE = "hasAuthority('REST_WRITE_AUTH')";

    @Override
    @PreAuthorize(CAN_WRITE)
    <S extends Item> List<S> save(Iterable<S> entites);

    @Override
    @PreAuthorize(CAN_WRITE)
    <S extends Item> S insert(S entity);

    @Override
    @PreAuthorize(CAN_WRITE)
    <S extends Item> List<S> insert(Iterable<S> entities);

    @Override
    @PreAuthorize(CAN_WRITE)
    <S extends Item> S save(S entity);

    @Override
    @PreAuthorize(CAN_WRITE)
    void delete(String s);

    @Override
    @PreAuthorize(CAN_WRITE)
    void delete(Item entity);

    @Override
    @PreAuthorize(CAN_WRITE)
    void delete(Iterable<? extends Item> entities);

    @Override
    @PreAuthorize(CAN_WRITE)
    void deleteAll();
}
