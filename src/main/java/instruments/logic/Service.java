package instruments.logic;

import instruments.data.Data;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Service {
    private static Service theInstance;

    public static Service instance(){
        if (theInstance == null) theInstance = new Service();
        return theInstance;
    }
    private Data data;

    private Service(){
        data = new Data();
    }

    public void create(TipoInstrumento e) throws Exception {
        TipoInstrumento result = data.getTipos().stream()
                .filter(i -> i.getCodigo().equals(e.getCodigo()))
                .findFirst()
                .orElse(null);
        if (result == null) {
            data.getTipos().add(e);
        } else {
            throw new Exception("Tipo ya existe");
        }

    }


    public TipoInstrumento read(TipoInstrumento e) throws Exception{
        TipoInstrumento result = data.getTipos().stream()
                .filter(i->i.getCodigo().equals(e.getCodigo())).findFirst().orElse(null);
        if (result!=null) return result;
        else throw new Exception("Tipo no existe");
    }

    public void update(TipoInstrumento e) throws Exception{
        TipoInstrumento result;
        try{
            result = this.read(e);
            data.getTipos().remove(result);
            data.getTipos().add(e);
        }catch (Exception ex) {
            throw new Exception("Tipo no existe");
        }
    }

    public void delete(TipoInstrumento e) throws Exception{
        data.getTipos().remove(e);
     }

    public List<TipoInstrumento> search(TipoInstrumento e){
        return data.getTipos().stream()
                .filter(i->i.getNombre().contains(e.getNombre()))
                .sorted(Comparator.comparing(TipoInstrumento::getNombre))
                .collect(Collectors.toList());
    }



 }
