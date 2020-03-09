package stanislav.tun.novinomad.picasso.exceptions;

public class EntityHoldException extends Exception {

    private String userName;

    @Override
    public String getMessage() {
        return assembleMessage();
    }

    private String assembleMessage() {
        var msg = String.format("This entity currently edited by user: "+userName
                +". Try again later or request user: "+userName
                +" to release this entity");
        return msg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
