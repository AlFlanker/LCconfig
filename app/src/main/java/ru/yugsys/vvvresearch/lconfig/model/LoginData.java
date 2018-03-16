package ru.yugsys.vvvresearch.lconfig.model;

public class LoginData {
    private String login;
    private String server;
    private String typeOfServer;
    private String[] listTypeOfServer;

    public LoginData(String login, String server, String typeOfServer, String[] listTypeOfServer) {
        this.login = login;
        this.server = server;
        this.typeOfServer = typeOfServer;
        this.listTypeOfServer = listTypeOfServer;
    }

    public String getLogin() {
        return login;
    }

    public String getServer() {
        return server;
    }

    public String getTypeOfServer() {
        return typeOfServer;
    }

    public String[] getArrayTypeOfServer() {
        return listTypeOfServer;
    }
}
