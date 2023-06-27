package msUsers.domain.responses;

public class ResponsePostEntityCreation {
    private long id;
    private String descripcion;

    public ResponsePostEntityCreation(long id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public ResponsePostEntityCreation(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
