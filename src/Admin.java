public class Admin {
    String admin_username;
    String password;


    public Admin(String admin_usermane, String password){
        this.admin_username = admin_usermane;
        this.password = password;
    }

    public void changename(String newname){
        this.admin_username = newname;
    }
    public void changepassword(String password){
        this.password = password;
    }


}
