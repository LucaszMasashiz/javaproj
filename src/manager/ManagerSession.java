/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package manager;

import model.Usuario;


/**
 *
 * @author Masashi
 */
public class ManagerSession {

    private static ManagerSession instance = new ManagerSession();
    private Usuario currentUser;
    private ManagerSession() { }
    public static ManagerSession getInstance() {
        return instance;
    }
    public void setCurrentUser(Usuario u) {
        this.currentUser = u;
    }
    public Usuario getCurrentUser() {
        return currentUser;
    }
    public void clear() {
        this.currentUser = null;
    }
}