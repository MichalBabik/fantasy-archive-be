package sk.babik.fantasyarchive;

public enum Role {
    USER(1),
    MODERATOR(2),
    ADMIN(3);

    private int id;

    Role(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


}
