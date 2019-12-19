package testClass;

public class Employe implements Comparable<Employe> {
    String name;
    int dni;
    int id;
    public Employe(String name, int dni){
        this.name = name;
        this.dni = dni;
    }
    @Override
    public int compareTo(Employe o) {
        if(id == o.id) return 0;
        return (id>o.id)?1:-1;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return  id;
    }
}
