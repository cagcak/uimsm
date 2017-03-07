package iaau.mas.uimsm.util;

/**
 * Created by cagri on 02.02.2014.
 */
public class User
{
    private String idnumber;
    private String password;

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User [idnumber=" + idnumber + ", password=" + password + "]";
    }


}