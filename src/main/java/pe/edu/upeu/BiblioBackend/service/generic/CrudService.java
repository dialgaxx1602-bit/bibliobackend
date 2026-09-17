package pe.edu.upeu.BiblioBackend.service.generic;

import java.util.List;

public interface CrudService<REQ, RES, ID> {
    RES create(REQ requestDTO);
    RES read(ID id);
    RES update(ID id, REQ requestDTO);
    void delete(ID id);
    List<RES> readAll();
}
