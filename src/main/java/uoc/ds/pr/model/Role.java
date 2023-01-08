package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.traversal.Iterator;

public class Role {
    private String roleId;
    private String description;

    private LinkedList<Worker> workers;

    public Role(String roleId, String description) {
        this.roleId = roleId;
        this.description = description;
        this.workers = new LinkedList<Worker>();
    }

    public Iterator<Worker> getWorkers() {
        return workers.values();
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addWorker(Worker worker) {
        this.workers.insertEnd(worker);
    }

    public void removeWorker(String dni) {
        //juanra aqui
        LinkedList<Worker> workersNew = new LinkedList<>();

        Iterator<Worker> it = workers.values();
        while (it.hasNext()) {
            Worker w = it.next();
            if (dni != w.getDni()) {
                workersNew.insertEnd(w);
            }
        }
        this.workers = workersNew;
    }

    public int numWorkers() {
        return this.workers.size();
    }


}