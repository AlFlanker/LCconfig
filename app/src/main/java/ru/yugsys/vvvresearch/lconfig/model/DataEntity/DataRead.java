package ru.yugsys.vvvresearch.lconfig.model.DataEntity;


public class DataRead {
    /**
     * класс для хранения вычитанных данных из блока флеши
     * block XX ->{0xXX, 0xXX, 0xXX, 0xXX};
     */
    @Override
    public String toString() {
        return "DataRead{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    private String name;
    private String value;

    public DataRead(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    public void setPhone(String value) {
        this.value = value;
    }
}