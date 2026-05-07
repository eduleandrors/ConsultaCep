package repository;

import java.util.ArrayList;
import java.util.List;

public class Repository<Cep> {

    private List<Cep> lista = new ArrayList<>();

    public void salvar(Cep obj) {
        lista.add(obj); 
    }

    public List<Cep> listar() {
        return lista;
    }
    
}